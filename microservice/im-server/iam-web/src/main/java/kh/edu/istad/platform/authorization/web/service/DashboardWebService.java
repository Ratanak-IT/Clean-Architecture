package kh.edu.istad.platform.authorization.web.service;

import kh.edu.istad.platform.authorization.domain.dto.user.result.UserAuthoritiesResult;
import kh.edu.istad.platform.authorization.domain.dto.user.result.UserResult;
import kh.edu.istad.platform.authorization.domain.usecase.user.GetMyProfileUseCase;
import kh.edu.istad.platform.authorization.domain.usecase.user.GetUserAuthoritiesUseCase;
import kh.edu.istad.platform.authorization.web.dto.DashboardView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DashboardWebService {

    private static final Logger log = LoggerFactory.getLogger(DashboardWebService.class);

    private final GetMyProfileUseCase getMyProfileUseCase;
    private final GetUserAuthoritiesUseCase getUserAuthoritiesUseCase;

    public DashboardWebService(
            GetMyProfileUseCase getMyProfileUseCase,
            GetUserAuthoritiesUseCase getUserAuthoritiesUseCase
    ) {
        this.getMyProfileUseCase = getMyProfileUseCase;
        this.getUserAuthoritiesUseCase = getUserAuthoritiesUseCase;
    }

    public DashboardView getDashboard(Authentication authentication) {
        UserResult userResult = null;
        Set<String> roles = Collections.emptySet();
        Set<String> permissions = Collections.emptySet();

        try {
            userResult = getMyProfileUseCase.execute();
            if (userResult != null && userResult.uuid() != null) {
                UserAuthoritiesResult authResult = getUserAuthoritiesUseCase.execute(userResult.uuid());
                if (authResult != null) {
                    if (authResult.roles() != null) {
                        roles = authResult.roles().stream()
                                .map(UserAuthoritiesResult.RoleAuthority::name)
                                .collect(Collectors.toSet());
                    }
                    if (authResult.effectivePermissions() != null) {
                        permissions = authResult.effectivePermissions();
                    }
                }
            }
        } catch (Exception ex) {
            log.warn("Could not retrieve domain user profile for {}", authentication.getName(), ex);
        }

        Map<String, Object> claims = Collections.emptyMap();
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
            claims = oAuth2User.getAttributes();
        }

        String username = authentication != null ? authentication.getName() : "Anonymous";
        return new DashboardView(userResult, roles, permissions, claims, username);
    }
}

