package kh.edu.istad.platform.authorization.domain.usecase.role;

import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.platform.authorization.domain.dto.role.result.RoleResult;
import kh.edu.istad.platform.authorization.domain.entity.Role;
import kh.edu.istad.platform.authorization.domain.mapper.RoleDomainMapper;
import kh.edu.istad.platform.authorization.domain.port.out.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetRoleUseCase {

    private final RoleRepository roleRepository;
    private final RoleDomainMapper roleDomainMapper;

    @Transactional(readOnly = true)
    public RoleResult execute(UUID roleUuid) {
        Role role = roleRepository.findByUuid(roleUuid);
        if (role == null || role.isDeleted()) {
            throw new ResourceNotFoundException("Role", roleUuid);
        }
        return roleDomainMapper.toResult(role);
    }
}
