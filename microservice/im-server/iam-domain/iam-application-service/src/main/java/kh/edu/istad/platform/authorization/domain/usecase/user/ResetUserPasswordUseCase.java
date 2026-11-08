package kh.edu.istad.platform.authorization.domain.usecase.user;

import kh.edu.istad.platform.authorization.domain.dto.user.command.ResetUserPasswordCommand;
import kh.edu.istad.platform.authorization.domain.entity.User;
import kh.edu.istad.platform.authorization.domain.port.out.identity.PasswordHasher;
import kh.edu.istad.platform.authorization.domain.port.out.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResetUserPasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    @Transactional
    public void execute(ResetUserPasswordCommand command) {
        User user = userRepository.findByUuid(command.userUuid());

        String hashed = passwordHasher.hash(command.newPassword());
        user.setPasswordHash(hashed);
        user.setPasswordChangedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
        log.info("Reset password for user: {}", user.getUsername().value());
    }
}
