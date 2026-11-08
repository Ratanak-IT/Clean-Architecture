package kh.edu.istad.platform.authorization.infra.config;

import kh.edu.istad.platform.authorization.persistence.entity.UserEntity;
import kh.edu.istad.platform.authorization.persistence.repository.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.*;

@Configuration
public class TokenConfig {

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer(
            UserJpaRepository userJpaRepository,
            UserDetailsService userDetailsService
    ) {
        return context -> {
            Authentication authentication = context.getPrincipal();
            if (authentication == null) {
                return;
            }

            if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(context.getAuthorizationGrantType())) {
                if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                    context.getClaims().claims(claims -> {
                        claims.put("client_id", context.getRegisteredClient().getClientId());
                    });
                }
                return;
            }

            UserDetails user;
            try {
                user = userDetailsService.loadUserByUsername(authentication.getName());
            } catch (Exception ex) {
                return;
            }
            if (user == null) {
                return;
            }

            Optional<UserEntity> userOpt = userJpaRepository.findByUsername(user.getUsername());
            if (userOpt.isEmpty()) {
                return;
            }
            UserEntity userEntity = userOpt.get();

            List<String> roles = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(auth -> auth.startsWith("ROLE_"))
                    .map(auth -> auth.substring(5))
                    .distinct()
                    .toList();

            List<String> permissions = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(auth -> !auth.startsWith("ROLE_"))
                    .distinct()
                    .toList();

            List<String> allAuthorities = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                context.getClaims().claims(claims -> {
                    claims.put("sub", userEntity.getUuid().toString());
                    claims.put("user_uuid", userEntity.getUuid().toString());
                    claims.put("preferred_username", userEntity.getUsername());
                    claims.put("roles", roles);
                    claims.put("permissions", permissions);
                    claims.put("authorities", allAuthorities);
                });
            }

            // ID TOKEN CUSTOMIZATION (OIDC Standard Claims)
            if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
                Set<String> authorizedScopes = context.getAuthorizedScopes();

                context.getClaims().claims(claims -> {
                    // openid scope: sub (= users.uuid)
                    claims.put("sub", userEntity.getUuid().toString());

                    // profile scope
                    if (authorizedScopes.contains(OidcScopes.PROFILE)) {
                        String fullName = String.format("%s %s",
                                userEntity.getGivenName() != null ? userEntity.getGivenName() : "",
                                userEntity.getFamilyName() != null ? userEntity.getFamilyName() : "").trim();
                        claims.put("name", fullName);
                        claims.put("given_name", userEntity.getGivenName());
                        claims.put("family_name", userEntity.getFamilyName());
                        if (userEntity.getMiddleName() != null) claims.put("middle_name", userEntity.getMiddleName());
                        if (userEntity.getNickname() != null) claims.put("nickname", userEntity.getNickname());
                        claims.put("preferred_username", userEntity.getUsername());
                        if (userEntity.getProfileUrl() != null) claims.put("profile", userEntity.getProfileUrl());
                        if (userEntity.getPictureUrl() != null) claims.put("picture", userEntity.getPictureUrl());
                        if (userEntity.getWebsiteUrl() != null) claims.put("website", userEntity.getWebsiteUrl());
                        if (userEntity.getGender() != null) claims.put("gender", userEntity.getGender());
                        if (userEntity.getBirthdate() != null) claims.put("birthdate", userEntity.getBirthdate().toString());
                        if (userEntity.getZoneinfo() != null) claims.put("zoneinfo", userEntity.getZoneinfo());
                        if (userEntity.getLocale() != null) claims.put("locale", userEntity.getLocale());
                        if (userEntity.getUpdatedAt() != null) claims.put("updated_at", userEntity.getUpdatedAt().getEpochSecond());
                    }

                    // email scope
                    if (authorizedScopes != null && authorizedScopes.contains(OidcScopes.EMAIL)) {
                        claims.put("email", userEntity.getEmail());
                        claims.put("email_verified", userEntity.isEmailVerified());
                    }

                    // phone scope
                    if (authorizedScopes != null && authorizedScopes.contains(OidcScopes.PHONE)) {
                        if (userEntity.getPhoneNumber() != null) {
                            claims.put("phone_number", userEntity.getPhoneNumber());
                            claims.put("phone_number_verified", userEntity.isPhoneNumberVerified());
                        }
                    }

                    // address scope
                    if (authorizedScopes != null && authorizedScopes.contains(OidcScopes.ADDRESS)) {
                        Map<String, Object> address = new LinkedHashMap<>();
                        if (userEntity.getAddressStreetAddress() != null) address.put("street_address", userEntity.getAddressStreetAddress());
                        if (userEntity.getAddressLocality() != null) address.put("locality", userEntity.getAddressLocality());
                        if (userEntity.getAddressRegion() != null) address.put("region", userEntity.getAddressRegion());
                        if (userEntity.getAddressPostalCode() != null) address.put("postal_code", userEntity.getAddressPostalCode());
                        if (userEntity.getAddressCountry() != null) address.put("country", userEntity.getAddressCountry());
                        if (!address.isEmpty()) {
                            claims.put("address", address);
                        }
                    }

                    claims.put("roles", roles);
                    claims.put("permissions", permissions);
                });
            }
        };
    }
}