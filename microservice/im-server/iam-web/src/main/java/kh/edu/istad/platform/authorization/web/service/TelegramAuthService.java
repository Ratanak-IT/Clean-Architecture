package kh.edu.istad.platform.authorization.web.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kh.edu.istad.platform.authorization.domain.dto.identity.command.FederatedUserCommand;
import kh.edu.istad.platform.authorization.domain.dto.identity.result.AuthUserResult;
import kh.edu.istad.platform.authorization.domain.usecase.identity.FindOrProvisionFederatedUserUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TelegramAuthService {

    private static final Logger log = LoggerFactory.getLogger(TelegramAuthService.class);

    private final FindOrProvisionFederatedUserUseCase findOrProvisionFederatedUserUseCase;
    private final UserDetailsService userDetailsService;

    public TelegramAuthService(
            FindOrProvisionFederatedUserUseCase findOrProvisionFederatedUserUseCase,
            UserDetailsService userDetailsService
    ) {
        this.findOrProvisionFederatedUserUseCase = findOrProvisionFederatedUserUseCase;
        this.userDetailsService = userDetailsService;
    }

    @Value("${telegram.bot-token:}")
    private String botToken;

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final org.springframework.security.web.savedrequest.RequestCache requestCache = new org.springframework.security.web.savedrequest.HttpSessionRequestCache();

    public String authenticateAndGetRedirect(Map<String, String> allParams, HttpServletRequest request, HttpServletResponse response) {
        boolean authenticated = authenticate(allParams, request, response);
        if (!authenticated) {
            return "redirect:/login?error=telegram_auth_failed";
        }

        org.springframework.security.web.savedrequest.SavedRequest savedRequest = requestCache.getRequest(request, response);
        return savedRequest != null ? "redirect:" + savedRequest.getRedirectUrl() : "redirect:/";
    }

    public boolean authenticate(Map<String, String> allParams, HttpServletRequest request, HttpServletResponse response) {
        if (botToken == null || botToken.isBlank()) {
            log.error("Telegram bot token is not configured in application properties");
            return false;
        }

        if (!verifyTelegramHash(allParams, botToken)) {
            log.warn("Telegram hash verification failed");
            return false;
        }

        if (isExpired(allParams.get("auth_date"))) {
            log.warn("Telegram auth data has expired");
            return false;
        }

        // 1. Build Command & Provision User
        FederatedUserCommand command = buildCommand(allParams);
        AuthUserResult authUser = findOrProvisionFederatedUserUseCase.execute(command);

        // 2. Establish Spring Security Context
        establishSecurityContext(authUser.username(), request, response);

        log.info("Telegram authentication successful for user: {}", authUser.username());
        return true;
    }

    private boolean isExpired(String authDateStr) {
        if (authDateStr == null) {
            return false;
        }
        try {
            long authDate = Long.parseLong(authDateStr);
            return (Instant.now().getEpochSecond() - authDate) > 86400; // 24 hours
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private FederatedUserCommand buildCommand(Map<String, String> params) {
        String id = params.get("id");
        String firstName = params.get("first_name");
        String lastName = params.get("last_name");
        String username = params.get("username");
        String photoUrl = params.get("photo_url");

        String fullName = Stream.of(firstName, lastName)
                .filter(s -> s != null && !s.isBlank())
                .collect(Collectors.joining(" "));

        return FederatedUserCommand.builder()
                .provider("TELEGRAM")
                .providerSubject(id)
                .username(username != null && !username.isBlank() ? username : "tg_" + id)
                .name(!fullName.isBlank() ? fullName : (username != null ? username : "Telegram User"))
                .pictureUrl(photoUrl)
                .build();
    }

    private void establishSecurityContext(String username, HttpServletRequest request, HttpServletResponse response) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        securityContextRepository.saveContext(securityContext, request, response);
    }

    private boolean verifyTelegramHash(Map<String, String> params, String token) {
        String hash = params.get("hash");
        if (hash == null || token == null || token.isBlank()) {
            return false;
        }

        List<String> dataCheckList = params.entrySet().stream()
                .filter(entry -> !"hash".equals(entry.getKey()) && entry.getValue() != null && !entry.getValue().isBlank())
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .toList();

        String dataCheckString = String.join("\n", dataCheckList);

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] secretKey = digest.digest(token.getBytes(StandardCharsets.UTF_8));

            Mac hmac = Mac.getInstance("HmacSHA256");
            hmac.init(new SecretKeySpec(secretKey, "HmacSHA256"));
            byte[] rawHmac = hmac.doFinal(dataCheckString.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : rawHmac) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return MessageDigest.isEqual(hexString.toString().getBytes(StandardCharsets.UTF_8), hash.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("Error during Telegram hash calculation", e);
            return false;
        }
    }
}

