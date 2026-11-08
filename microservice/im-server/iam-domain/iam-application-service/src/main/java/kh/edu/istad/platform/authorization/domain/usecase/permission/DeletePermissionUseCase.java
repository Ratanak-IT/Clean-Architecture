package kh.edu.istad.platform.authorization.domain.usecase.permission;

import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.platform.authorization.domain.entity.Permission;
import kh.edu.istad.platform.authorization.domain.port.out.identity.CurrentActorProvider;
import kh.edu.istad.platform.authorization.domain.port.out.permission.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeletePermissionUseCase {

    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public void execute(UUID permissionUuid) {
        Permission permission = permissionRepository.findByUuid(permissionUuid);
        if (permission == null || permission.isDeleted()) {
            throw new ResourceNotFoundException("Permission", permissionUuid);
        }

        permission.validateCanModify();

        String actor = currentActorProvider.getCurrentActor();
        permission.setDeleted(true);
        permission.setDeletedAt(Instant.now());
        permission.setDeletedBy(actor);
        permission.setUpdatedAt(Instant.now());
        permission.setUpdatedBy(actor);

        permissionRepository.update(permission);
        log.info("Soft-deleted permission: {} (uuid: {}) by actor: {}", permission.getName(), permissionUuid, actor);
    }
}
