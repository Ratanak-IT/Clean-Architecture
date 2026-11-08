package kh.edu.istad.platform.authorization.domain.usecase.user;

import kh.edu.istad.platform.authorization.domain.entity.User;
import kh.edu.istad.platform.authorization.domain.port.out.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecordLoginAttemptUseCase {

    private final UserRepository userRepository;

    @Transactional
    public void execute(String usernameOrEmail, boolean success) {
        try {
            User user = userRepository.findByUsernameOrEmail(usernameOrEmail);
            if (user != null) {
                if (success) {
                    user.setFailedLoginAttempts(0);
                    user.setLastLoginAt(Instant.now());
                } else {
                    int attempts = user.getFailedLoginAttempts() + 1;
                    user.setFailedLoginAttempts(attempts);
                    if (attempts >= 5) {
                        user.setAccountNonLocked(false);
                    }
                }
                userRepository.save(user);
            }
        } catch (Exception e) {
            log.debug("Record login attempt skipped: user '{}' not found", usernameOrEmail);
        }
    }
}
