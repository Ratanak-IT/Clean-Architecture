package kh.edu.istad.platform.authorization.domain.usecase.user;

import kh.edu.istad.platform.authorization.domain.dto.user.result.UserResult;
import kh.edu.istad.platform.authorization.domain.entity.User;
import kh.edu.istad.platform.authorization.domain.mapper.UserDomainMapper;
import kh.edu.istad.platform.authorization.domain.port.out.identity.CurrentActorProvider;
import kh.edu.istad.platform.authorization.domain.port.out.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetMyProfileUseCase {

    private final UserRepository userRepository;
    private final UserDomainMapper userDomainMapper;
    private final CurrentActorProvider currentActorProvider;

    @Transactional(readOnly = true)
    public UserResult execute() {
        UUID currentUuid = currentActorProvider.getCurrentUserUuid();
        User user = userRepository.findByUuid(currentUuid);
        return userDomainMapper.toResult(user);
    }
}

