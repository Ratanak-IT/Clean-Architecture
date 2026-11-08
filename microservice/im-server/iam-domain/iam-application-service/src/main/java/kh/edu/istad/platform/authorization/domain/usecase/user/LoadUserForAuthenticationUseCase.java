package kh.edu.istad.platform.authorization.domain.usecase.user;

import kh.edu.istad.platform.authorization.domain.dto.identity.result.AuthUserResult;
import kh.edu.istad.platform.authorization.domain.entity.User;
import kh.edu.istad.platform.authorization.domain.mapper.UserDomainMapper;
import kh.edu.istad.platform.authorization.domain.port.out.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LoadUserForAuthenticationUseCase {

    private final UserRepository userRepository;
    private final UserDomainMapper userDomainMapper;

    @Transactional(readOnly = true)
    public AuthUserResult execute(String usernameOrEmail) {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail);
        return userDomainMapper.toAuthUserResult(user);
    }
}
