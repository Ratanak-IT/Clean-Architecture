package kh.edu.istad.platform.authorization.domain.usecase.permission;

import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.platform.authorization.domain.dto.permission.result.PermissionResult;
import kh.edu.istad.platform.authorization.domain.entity.Permission;
import kh.edu.istad.platform.authorization.domain.mapper.PermissionDomainMapper;
import kh.edu.istad.platform.authorization.domain.port.out.permission.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetPermissionUseCase {

    private final PermissionRepository permissionRepository;
    private final PermissionDomainMapper permissionDomainMapper;

    @Transactional(readOnly = true)
    public PermissionResult execute(UUID permissionUuid) {
        Permission permission = permissionRepository.findByUuid(permissionUuid);
        if (permission == null || permission.isDeleted()) {
            throw new ResourceNotFoundException("Permission", permissionUuid);
        }
        return permissionDomainMapper.toResult(permission);
    }
}
