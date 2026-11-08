package kh.edu.istad.platform.authorization.infra.adapter;

import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.platform.authorization.domain.port.out.identity.CurrentActorProvider;
import kh.edu.istad.platform.authorization.infra.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import kh.edu.istad.platform.authorization.persistence.entity.UserEntity;
import kh.edu.istad.platform.authorization.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrentActorProviderAdapter implements CurrentActorProvider {

    private final UserJpaRepository userJpaRepository;

    @Override
    public UUID getCurrentUserUuid() {
        return findCurrentUserUuid()
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found in security context"));
    }

    @Override
    public Optional<UUID> findCurrentUserUuid() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        Object principal = auth.getPrincipal();

        // 1. Session / Form Login: UserDetails / CustomUserDetails
        if (principal instanceof CustomUserDetails customUserDetails) {
            if (customUserDetails.getUuid() != null) {
                return Optional.of(customUserDetails.getUuid());
            }
        } else if (principal instanceof UserDetails user) {
            Optional<UserEntity> userOpt = userJpaRepository.findByUsername(user.getUsername());
            if (userOpt.isPresent()) {
                return Optional.of(userOpt.get().getUuid());
            }
        }

        // 2. JWT Resource Server
        if (principal instanceof Jwt jwt) {
            // Check user_uuid claim
            String userUuidClaim = jwt.getClaimAsString("user_uuid");
            if (userUuidClaim != null && !userUuidClaim.isBlank()) {
                try {
                    return Optional.of(UUID.fromString(userUuidClaim));
                } catch (IllegalArgumentException ignored) {
                }
            }

            // Check sub claim
            String sub = jwt.getSubject();
            if (sub != null && !sub.isBlank()) {
                try {
                    return Optional.of(UUID.fromString(sub));
                } catch (IllegalArgumentException ignored) {
                    // sub might be username or email
                    Optional<UserEntity> userOpt = userJpaRepository.findByUsernameOrEmail(sub, sub);
                    if (userOpt.isPresent()) {
                        return Optional.of(userOpt.get().getUuid());
                    }
                }
            }

            // Check preferred_username claim
            String preferredUsername = jwt.getClaimAsString("preferred_username");
            if (preferredUsername != null && !preferredUsername.isBlank()) {
                Optional<UserEntity> userOpt = userJpaRepository.findByUsernameOrEmail(preferredUsername, preferredUsername);
                if (userOpt.isPresent()) {
                    return Optional.of(userOpt.get().getUuid());
                }
            }
        }

        // 3. OAuth2 User (Google, GitHub, etc.)
        if (principal instanceof OAuth2User oauth2User) {
            String email = oauth2User.getAttribute("email");
            if (email != null && !email.isBlank()) {
                Optional<UserEntity> userOpt = userJpaRepository.findByEmail(email);
                if (userOpt.isPresent()) {
                    return Optional.of(userOpt.get().getUuid());
                }
            }
            String login = oauth2User.getAttribute("login");
            if (login != null && !login.isBlank()) {
                Optional<UserEntity> userOpt = userJpaRepository.findByUsername(login);
                if (userOpt.isPresent()) {
                    return Optional.of(userOpt.get().getUuid());
                }
            }
        }

        // 4. Fallback by authentication name
        String name = auth.getName();
        if (name != null && !name.isBlank()) {
            try {
                return Optional.of(UUID.fromString(name));
            } catch (IllegalArgumentException ignored) {
                return userJpaRepository.findByUsernameOrEmail(name, name).map(UserEntity::getUuid);
            }
        }

        return Optional.empty();
    }

    @Override
    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new ResourceNotFoundException("Authenticated user not found in security context");
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails user) {
            return user.getUsername();
        }

        if (principal instanceof Jwt jwt) {
            String preferredUsername = jwt.getClaimAsString("preferred_username");
            if (preferredUsername != null && !preferredUsername.isBlank()) {
                return preferredUsername;
            }
            String sub = jwt.getSubject();
            if (sub != null && !sub.isBlank()) {
                try {
                    UUID uuid = UUID.fromString(sub);
                    return userJpaRepository.findByUuid(uuid).map(UserEntity::getUsername).orElse(sub);
                } catch (IllegalArgumentException ignored) {
                    return sub;
                }
            }
        }

        if (principal instanceof OAuth2User oauth2User) {
            String login = oauth2User.getAttribute("login");
            if (login != null && !login.isBlank()) return login;
            String email = oauth2User.getAttribute("email");
            if (email != null && !email.isBlank()) return email;
        }

        String name = auth.getName();
        if (name != null && !name.isBlank()) {
            return name;
        }

        throw new ResourceNotFoundException("Authenticated user not found in security context");
    }

    @Override
    public String getCurrentActor() {
        try {
            return getCurrentUsername();
        } catch (Exception ex) {
            return "SYSTEM";
        }
    }
}
