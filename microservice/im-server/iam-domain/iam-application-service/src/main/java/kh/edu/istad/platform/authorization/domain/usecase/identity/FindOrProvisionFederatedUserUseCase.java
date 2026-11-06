package kh.edu.istad.platform.authorization.domain.usecase.identity;

import kh.edu.istad.platform.authorization.domain.dto.identity.FederatedUserCommand;
import kh.edu.istad.platform.authorization.domain.dto.identity.AuthUserResult;
import kh.edu.istad.platform.authorization.domain.entity.Identity;
import kh.edu.istad.platform.authorization.domain.entity.Permission;
import kh.edu.istad.platform.authorization.domain.entity.Role;
import kh.edu.istad.platform.authorization.domain.entity.User;
import kh.edu.istad.platform.authorization.domain.port.out.IdentityRepository;
import kh.edu.istad.platform.authorization.domain.port.out.UserRepository;
import kh.edu.istad.platform.authorization.domain.valueobject.Email;
import kh.edu.istad.platform.authorization.domain.valueobject.IdentityProviderType;
import kh.edu.istad.platform.authorization.domain.valueobject.Username;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class FindOrProvisionFederatedUserUseCase {

    private final UserRepository userRepository;
    private final IdentityRepository identityRepository;

    @Transactional
    public AuthUserResult execute(FederatedUserCommand command) {
        IdentityProviderType providerType = IdentityProviderType.fromString(command.provider());

        // 1. Check if identity already exists
        Optional<Identity> identityOpt = identityRepository.findByProviderAndProviderSubject(
                providerType, command.providerSubject()
        );

        User user;

        if (identityOpt.isPresent()) {
            Identity identity = identityOpt.get();
            identity.setLastLoginAt(Instant.now());
            identity.setPictureUrl(command.pictureUrl());
            identity.setAttributes(command.accessToken());

            user = userRepository.findById(identity.getUserId())
                    .orElseThrow(() -> new IllegalStateException("User not found for identity id: " + identity.getId()));

            user.setLastLoginAt(Instant.now());
            if (user.getPictureUrl() == null) {
                user.setPictureUrl(command.pictureUrl());
            }

            user = userRepository.save(user);
            identityRepository.save(identity);
            log.info("Updated existing federated user: {}", user.getUsername().value());
        } else {
            // 2. Identity does not exist: find user by email or register new user
            Optional<User> userOpt = (command.email() != null && !command.email().isBlank())
                    ? userRepository.findByEmail(command.email())
                    : Optional.empty();

            if (userOpt.isPresent()) {
                user = userOpt.get();
                user.setLastLoginAt(Instant.now());
                user = userRepository.save(user);
                log.info("Linked new identity to existing user: {}", user.getUsername().value());
            } else {
                String uniqueUsername = resolveUniqueUsername(command.username());
                String emailStr = (command.email() != null && !command.email().isBlank())
                        ? command.email()
                        : uniqueUsername + "@" + command.provider().toLowerCase() + ".local";

                user = User.builder()
                        .uuid(UUID.randomUUID())
                        .username(new Username(uniqueUsername))
                        .email(new Email(emailStr))
                        .emailVerified(command.email() != null && !command.email().isBlank())
                        .givenName(command.name())
                        .nickname(uniqueUsername)
                        .pictureUrl(command.pictureUrl())
                        .profileUrl(command.pictureUrl())
                        .enabled(true)
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .lastLoginAt(Instant.now())
                        .createdAt(Instant.now())
                        .build();

                user = userRepository.save(user);
                log.info("Registered new federated user: {}", user.getUsername().value());
            }

            // 3. Create and save new Identity
            Identity newIdentity = Identity.builder()
                    .uuid(UUID.randomUUID())
                    .userId(user.getId())
                    .provider(providerType)
                    .providerSubject(command.providerSubject())
                    .email(command.email() != null ? new Email(command.email()) : null)
                    .emailVerified(command.email() != null && !command.email().isBlank())
                    .displayName(command.name() != null ? command.name() : command.username())
                    .username(command.username())
                    .pictureUrl(command.pictureUrl())
                    .attributes(command.accessToken())
                    .linkedAt(Instant.now())
                    .lastLoginAt(Instant.now())
                    .createdAt(Instant.now())
                    .deleted(false)
                    .build();

            identityRepository.save(newIdentity);
        }

        return toAuthUserResult(user);
    }

    private String resolveUniqueUsername(String baseUsername) {
        if (baseUsername == null || baseUsername.isBlank()) {
            baseUsername = "user";
        }
        String candidate = baseUsername;
        int counter = 1;
        while (userRepository.existsByUsername(candidate)) {
            candidate = baseUsername + counter;
            counter++;
        }
        return candidate;
    }

    private AuthUserResult toAuthUserResult(User user) {
        Set<String> roleNames = (user.getRoles() != null)
                ? user.getRoles().stream()
                .filter(r -> r.isActive() && !r.isDeleted())
                .map(Role::getName)
                .collect(Collectors.toSet())
                : Collections.emptySet();

        Set<String> permissionNames = (user.getPermissions() != null)
                ? user.getPermissions().stream()
                .filter(p -> p.isActive() && !p.isDeleted())
                .map(Permission::getName)
                .collect(Collectors.toSet())
                : Collections.emptySet();

        return AuthUserResult.builder()
                .userUuid(user.getUuid())
                .username(user.getUsername() != null ? user.getUsername().value() : null)
                .email(user.getEmail() != null ? user.getEmail().value() : null)
                .passwordHash(user.getPasswordHash())
                .enabled(user.isEnabled())
                .accountNonExpired(user.isAccountNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .roles(roleNames)
                .permissions(permissionNames)
                .build();
    }
}
