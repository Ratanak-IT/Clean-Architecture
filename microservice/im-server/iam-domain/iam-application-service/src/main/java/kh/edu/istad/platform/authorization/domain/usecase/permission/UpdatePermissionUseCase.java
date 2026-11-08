package kh.edu.istad.platform.authorization.domain.usecase.permission;

import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.platform.authorization.domain.dto.permission.command.UpdatePermissionCommand;
import kh.edu.istad.platform.authorization.domain.dto.permission.result.PermissionResult;
import kh.edu.istad.platform.authorization.domain.entity.Permission;
import kh.edu.istad.platform.authorization.domain.mapper.PermissionDomainMapper;
import kh.edu.istad.platform.authorization.domain.port.out.identity.CurrentActorProvider;
import kh.edu.istad.platform.authorization.domain.port.out.permission.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdatePermissionUseCase {

    private final PermissionRepository permissionRepository;
    private final PermissionDomainMapper permissionDomainMapper;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public PermissionResult execute(UpdatePermissionCommand command) {
        Permission permission = permissionRepository.findByUuid(command.permissionUuid());
        if (permission == null || permission.isDeleted()) {
            throw new ResourceNotFoundException("Permission", command.permissionUuid());
        }

        permission.validateCanModify();

        if (command.description() != null) {
            permission.setDescription(command.description());
        }

        String actor = currentActorProvider.getCurrentActor();
        permission.setUpdatedAt(Instant.now());
        permission.setUpdatedBy(actor);

        Permission updated = permissionRepository.update(permission);
        log.info("Updated permission: {} (uuid: {}) by actor: {}", updated.getName(), updated.getUuid(), actor);

        return permissionDomainMapper.toResult(updated);
    }
}
