package kh.edu.istad.platform.authorization.infra.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityConfigJwtAuthenticationConverterTest {

    @Test
    void exposesApplicationAuthoritiesAlongsideScopes() {
        Jwt jwt = Jwt.withTokenValue("test-token")
                .header("alg", "RS256")
                .claim("sub", "user-id")
                .claim("scope", "openid profile")
                .claim("authorities", Set.of("ROLE_ADMIN", "permission:create"))
                .build();

        AbstractAuthenticationToken authentication = new SecurityConfig(null, null, null)
                .jwtAuthenticationConverter()
                .convert(jwt);

        Set<String> authorities = authentication.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(java.util.stream.Collectors.toSet());

        assertTrue(authorities.contains("SCOPE_openid"));
        assertTrue(authorities.contains("SCOPE_profile"));
        assertTrue(authorities.contains("ROLE_ADMIN"));
        assertTrue(authorities.contains("permission:create"));
    }
}
