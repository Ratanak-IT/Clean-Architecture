package kh.edu.istad.platform.authorization.domain.usecase.role;

import kh.edu.istad.platform.authorization.domain.dto.PageResult;
import kh.edu.istad.platform.authorization.domain.dto.role.command.RoleFilterQuery;
import kh.edu.istad.platform.authorization.domain.dto.role.result.RoleSummary;
import kh.edu.istad.platform.authorization.domain.entity.Role;
import kh.edu.istad.platform.authorization.domain.mapper.RoleDomainMapper;
import kh.edu.istad.platform.authorization.domain.port.out.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListRolesUseCase {

    private final RoleRepository roleRepository;
    private final RoleDomainMapper roleDomainMapper;

    @Transactional(readOnly = true)
    public PageResult<RoleSummary> execute(RoleFilterQuery query) {
        PageResult<Role> pageResult = roleRepository.findAll(query);
        List<RoleSummary> content = pageResult.content().stream()
                .map(roleDomainMapper::toSummary)
                .toList();
        return PageResult.of(content, pageResult.pageNumber(), pageResult.pageSize(), pageResult.totalElements());
    }
}
