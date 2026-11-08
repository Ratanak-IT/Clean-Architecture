package kh.edu.istad.platform.authorization.domain.usecase.user;

import kh.edu.istad.platform.authorization.domain.port.out.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class LockUserUseCase {

    private final UserRepository userRepository;

    @Transactional
    public void execute(UUID uuid) {
        userRepository.lock(uuid);
        log.info("Locked user: {}", uuid);
    }
}
