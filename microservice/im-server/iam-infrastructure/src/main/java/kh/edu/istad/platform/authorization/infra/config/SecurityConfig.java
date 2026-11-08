package kh.edu.istad.platform.authorization.infra.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import kh.edu.istad.platform.authorization.infra.security.CustomOauth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JdbcTemplate jdbcTemplate;
    private final CustomOauth2Service customOauth2Service;
    private final kh.edu.istad.platform.authorization.infra.security.CustomOidcUserService customOidcUserService;
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, (authorizationServer) ->
                        authorizationServer
                                .authorizationEndpoint(authorizationEndpoint ->
                                        authorizationEndpoint.consentPage("/oauth2/consent")
                                )
                                .oidc(Customizer.withDefaults())
                )
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .anyRequest().authenticated()
                )
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(
            HttpSecurity http,
            Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter
    )
            throws Exception {
        org.springframework.security.web.savedrequest.HttpSessionRequestCache requestCache =
                new org.springframework.security.web.savedrequest.HttpSessionRequestCache();
        requestCache.setRequestMatcher(new MediaTypeRequestMatcher(MediaType.TEXT_HTML));

        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .requestCache(cache -> cache.requestCache(requestCache))
                .authorizeHttpRequests((authorize) -> authorize
                        // 1. Static and Public Endpoints
                        .requestMatchers("/", "/home", "/login", "/register", "/callback", "/.well-known/**", "/error", "/favicon.ico", "/images/**", "/css/**", "/js/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()

                        // 2. Current User Endpoints (/api/me/**)
                        .requestMatchers("/api/me/**").authenticated()

                        // 3. User Create
                        .requestMatchers(HttpMethod.POST, "/api/users").hasAuthority("user:create")

                        // 4. User Read
                        .requestMatchers(HttpMethod.GET, "/api/users").hasAuthority("user:read")
                        .requestMatchers(HttpMethod.GET, "/api/users/*").hasAuthority("user:read")
                        .requestMatchers(HttpMethod.GET, "/api/users/*/authorities").hasAuthority("user:read")
                        .requestMatchers(HttpMethod.GET, "/api/users/*/identities").hasAuthority("user:read")

                        // 5. User Update & Delete
                        .requestMatchers(HttpMethod.PATCH, "/api/users/*").hasAuthority("user:update")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/*").hasAuthority("user:delete")
                        .requestMatchers(HttpMethod.POST, "/api/users/*/enable", "/api/users/*/disable", "/api/users/*/lock", "/api/users/*/unlock").hasAuthority("user:update")
                        .requestMatchers(HttpMethod.PUT, "/api/users/*/password").hasAuthority("user:update")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/*/identities/*").hasAuthority("user:update")

                        // 6. User Assign & Revoke (Roles & Permissions)
                        .requestMatchers(HttpMethod.POST, "/api/users/*/roles", "/api/users/*/permissions").hasAuthority("user:assign")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/*/roles/*", "/api/users/*/permissions/*").hasAuthority("user:assign")

                        // 7. Permissions
                        .requestMatchers(HttpMethod.POST, "/api/permissions").hasAuthority("permission:create")
                        .requestMatchers(HttpMethod.GET, "/api/permissions", "/api/permissions/*").hasAuthority("permission:read")
                        .requestMatchers(HttpMethod.PATCH, "/api/permissions/*").hasAuthority("permission:update")
                        .requestMatchers(HttpMethod.DELETE, "/api/permissions/*").hasAuthority("permission:delete")
                        .requestMatchers(HttpMethod.POST, "/api/permissions/*/activate", "/api/permissions/*/deactivate").hasAuthority("permission:update")

                        // 8. All other requests
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt ->
                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)
                ))
                .formLogin(form -> form.loginPage("/login").permitAll())
                .oauth2Login(oauth2 -> oauth2.loginPage("/login").permitAll()
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOauth2Service)
                                .oidcUserService(customOidcUserService))
                        .defaultSuccessUrl("/"));

        return http.build();
    }

    /**
     * Keeps the standard SCOPE_* authorities and also restores the application's
     * roles and permissions from the access token's authorities claim.
     */
    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter scopeAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        return jwt -> {
            Collection<GrantedAuthority> authorities = new HashSet<>(scopeAuthoritiesConverter.convert(jwt));
            List<String> tokenAuthorities = jwt.getClaimAsStringList("authorities");
            if (tokenAuthorities != null) {
                tokenAuthorities.stream()
                        .filter(authority -> authority != null && !authority.isBlank())
                        .map(SimpleGrantedAuthority::new)
                        .forEach(authorities::add);
            }

            return new JwtAuthenticationToken(jwt, authorities, jwt.getSubject());
        };
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    @Bean
    public OAuth2AuthorizationService authorizationService() {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository());
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService() {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository());
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }
}

