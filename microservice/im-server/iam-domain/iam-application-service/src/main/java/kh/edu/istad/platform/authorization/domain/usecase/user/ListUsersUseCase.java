package kh.edu.istad.platform.authorization.domain.usecase.user;

import kh.edu.istad.platform.authorization.domain.dto.PageResult;
import kh.edu.istad.platform.authorization.domain.dto.user.command.UserFilterQuery;
import kh.edu.istad.platform.authorization.domain.dto.user.result.UserSummary;
import kh.edu.istad.platform.authorization.domain.entity.User;
import kh.edu.istad.platform.authorization.domain.mapper.UserDomainMapper;
import kh.edu.istad.platform.authorization.domain.port.out.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListUsersUseCase {

    private final UserRepository userRepository;
    private final UserDomainMapper userDomainMapper;

    @Transactional(readOnly = true)
    public PageResult<UserSummary> execute(UserFilterQuery query) {
        PageResult<User> pageResult = userRepository.findAll(query);
        List<UserSummary> content = pageResult.content().stream()
                .map(userDomainMapper::toSummary)
                .toList();
        return PageResult.of(content, pageResult.pageNumber(), pageResult.pageSize(), pageResult.totalElements());
    }
}
