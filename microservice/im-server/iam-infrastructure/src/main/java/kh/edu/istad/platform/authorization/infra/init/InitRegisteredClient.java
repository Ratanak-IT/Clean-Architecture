package kh.edu.istad.platform.authorization.infra.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class InitRegisteredClient {

    private final RegisteredClientRepository registeredClientRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        TokenSettings tokenSettings = TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.ofHours(5))
                .refreshTokenTimeToLive(Duration.ofDays(7))
                .authorizationCodeTimeToLive(Duration.ofMinutes(3))
                .reuseRefreshTokens(false)
                .build();

        ClientSettings clientSettings = ClientSettings.builder()
                .requireProofKey(false)
                .requireAuthorizationConsent(true)
                .build();

        RegisteredClient.Builder builder;
        RegisteredClient existing = registeredClientRepository.findByClientId("oidc-client");
        if (existing == null) {
            builder = RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId("oidc-client");
        } else {
            builder = RegisteredClient.from(existing);
        }

        RegisteredClient oidcClient = builder
                .clientSecret(passwordEncoder.encode("secret"))
                .clientName("ISTAD Client Portal")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                // Port 9000 Browser Test Callback
                .redirectUri("http://127.0.0.1:9000/callback")
                .redirectUri("http://localhost:9000/callback")
                // Port 9000 (Current Server Port)
                .redirectUri("http://127.0.0.1:9000/login/oauth2/code/oidc-client")
                .redirectUri("http://localhost:9000/login/oauth2/code/oidc-client")
                // Port 8080 (Alternative/Default)
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/oidc-client")
                .redirectUri("http://localhost:8080/login/oauth2/code/oidc-client")
                // Postman
                .redirectUri("https://oauth.pstmn.io/v1/callback")
                .postLogoutRedirectUri("http://localhost:9000/")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope(OidcScopes.EMAIL)
                .clientSettings(clientSettings)
                .tokenSettings(tokenSettings)
                .build();

        registeredClientRepository.save(oidcClient);
        log.info("RegisteredClient 'oidc-client' configured with requireAuthorizationConsent=true and port 9000/8080 redirect URIs");
    }
}
