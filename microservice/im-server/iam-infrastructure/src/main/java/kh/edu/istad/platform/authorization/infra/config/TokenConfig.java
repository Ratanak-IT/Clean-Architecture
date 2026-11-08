package kh.edu.istad.platform.authorization.infra.config;

import kh.edu.istad.platform.authorization.persistence.entity.UserEntity;
import kh.edu.istad.platform.authorization.persistence.repository.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.List;
import java.util.Optional;

@Configuration
public class TokenConfig {

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer(
            UserJpaRepository userJpaRepository,
            UserDetailsService userDetailsService
    ) {
        return context -> {
            if (!OAuth2ParameterNames.ACCESS_TOKEN.equals(context.getTokenType().getValue())) {
                return;
            }

            // During token issuance, the authorization-server request can run in
            // a different security context. JwtEncodingContext is the reliable
            // source of the authenticated resource owner.
            Authentication authentication = context.getPrincipal();
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                // Reload at token-issue time. The browser's authorization session
                // may predate an admin changing roles or permissions in the DB.
                UserDetails user = userDetailsService.loadUserByUsername(authentication.getName());
                List<String> authorities = user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList();

                Optional<UserEntity> userOpt = userJpaRepository.findByUsername(user.getUsername());

                context.getClaims()
                        .claims(claim -> {
                            userOpt.ifPresent(u -> {
                                claim.put("sub", u.getUuid().toString());
                                claim.put("user_uuid", u.getUuid().toString());
                            });
                            claim.put("preferred_username", user.getUsername());
                            claim.put("authorities", authorities);
                        });
            }
        };
    }
}
