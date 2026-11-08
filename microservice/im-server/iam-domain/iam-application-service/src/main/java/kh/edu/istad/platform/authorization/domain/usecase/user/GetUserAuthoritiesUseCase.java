package kh.edu.istad.platform.authorization.domain.usecase.user;

import kh.edu.istad.platform.authorization.domain.dto.user.result.UserAuthoritiesResult;
import kh.edu.istad.platform.authorization.domain.entity.User;
import kh.edu.istad.platform.authorization.domain.mapper.UserDomainMapper;
import kh.edu.istad.platform.authorization.domain.port.out.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetUserAuthoritiesUseCase {

    private final UserRepository userRepository;
    private final UserDomainMapper userDomainMapper;

    @Transactional(readOnly = true)
    public UserAuthoritiesResult execute(UUID uuid) {
        User user = userRepository.findByUuid(uuid);
        return userDomainMapper.toAuthoritiesResult(user);
    }
}
