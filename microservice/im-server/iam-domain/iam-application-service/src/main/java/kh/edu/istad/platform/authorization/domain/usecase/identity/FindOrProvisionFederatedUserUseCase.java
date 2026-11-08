package kh.edu.istad.platform.authorization.domain.usecase.identity;

import kh.edu.istad.platform.authorization.domain.dto.identity.command.FederatedUserCommand;
import kh.edu.istad.platform.authorization.domain.dto.identity.result.AuthUserResult;
import kh.edu.istad.platform.authorization.domain.entity.Identity;
import kh.edu.istad.platform.authorization.domain.entity.User;
import kh.edu.istad.platform.authorization.domain.mapper.IdentityDomainMapper;
import kh.edu.istad.platform.authorization.domain.mapper.UserDomainMapper;
import kh.edu.istad.platform.authorization.domain.port.out.identity.IdentityRepository;
import kh.edu.istad.platform.authorization.domain.port.out.user.UserRepository;
import kh.edu.istad.platform.authorization.domain.valueobject.IdentityProviderType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class FindOrProvisionFederatedUserUseCase {

    private final UserRepository userRepository;
    private final IdentityRepository identityRepository;
    private final UserDomainMapper userDomainMapper;
    private final IdentityDomainMapper identityDomainMapper;

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

            if (identity.getUserUuid() != null) {
                user = userRepository.findByUuid(identity.getUserUuid());
            } else {
                user = userRepository.findByUsernameOrEmail(command.email() != null ? command.email() : command.username());
            }

            user.setLastLoginAt(Instant.now());
            if (user.getPictureUrl() == null) {
                user.setPictureUrl(command.pictureUrl());
            }

            user = userRepository.save(user);
            identityRepository.save(identity);
            log.info("Updated existing federated user: {}", user.getUsername().value());
        } else {
            boolean userExistsWithEmail = (command.email() != null && !command.email().isBlank())
                    && userRepository.existsByEmail(command.email());

            if (userExistsWithEmail) {
                user = userRepository.findByUsernameOrEmail(command.email());
                user.setLastLoginAt(Instant.now());
                user = userRepository.save(user);
                log.info("Linked new identity to existing user: {}", user.getUsername().value());
            } else {
                String uniqueUsername = resolveUniqueUsername(command.username());
                String emailStr = (command.email() != null && !command.email().isBlank())
                        ? command.email()
                        : uniqueUsername + "@" + command.provider().toLowerCase() + ".local";

                user = userDomainMapper.fromFederatedCommand(command, uniqueUsername, emailStr);
                user = userRepository.save(user);
                log.info("Registered new federated user: {}", user.getUsername().value());
            }

            // 3. Create and save new Identity
            Identity newIdentity = identityDomainMapper.fromFederatedCommand(user, providerType, command);
            identityRepository.save(newIdentity);
        }

        return userDomainMapper.toAuthUserResult(user);
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
}
