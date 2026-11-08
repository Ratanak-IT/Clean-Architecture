package kh.edu.istad.platform.authorization.domain.usecase.user;

import kh.edu.istad.platform.authorization.domain.dto.user.command.ChangePasswordCommand;
import kh.edu.istad.platform.authorization.domain.entity.User;
import kh.edu.istad.platform.authorization.domain.port.out.identity.CurrentActorProvider;
import kh.edu.istad.platform.authorization.domain.port.out.identity.PasswordHasher;
import kh.edu.istad.platform.authorization.domain.port.out.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChangePasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public void execute(ChangePasswordCommand command) {
        UUID userUuid = (command.userUuid() != null)
                ? command.userUuid()
                : currentActorProvider.getCurrentUserUuid();

        User user = userRepository.findByUuid(userUuid);

        if (!passwordHasher.matches(command.currentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (!command.newPassword().equals(command.confirmPassword())) {
            throw new IllegalArgumentException("New passwords do not match");
        }

        String hashed = passwordHasher.hash(command.newPassword());
        user.setPasswordHash(hashed);
        user.setPasswordChangedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
        log.info("Changed password for user: {}", user.getUsername().value());
    }
}
