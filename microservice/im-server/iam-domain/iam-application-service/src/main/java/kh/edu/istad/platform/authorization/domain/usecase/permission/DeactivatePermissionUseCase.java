package kh.edu.istad.platform.authorization.domain.usecase.permission;

import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.platform.authorization.domain.entity.Permission;
import kh.edu.istad.platform.authorization.domain.port.out.identity.CurrentActorProvider;
import kh.edu.istad.platform.authorization.domain.port.out.permission.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeactivatePermissionUseCase {

    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public void execute(UUID permissionUuid) {
        Permission permission = permissionRepository.findByUuid(permissionUuid);
        if (permission == null || permission.isDeleted()) {
            throw new ResourceNotFoundException("Permission", permissionUuid);
        }

        permission.validateCanModify();
        permission.setActive(false);
        permission.setUpdatedAt(Instant.now());
        permission.setUpdatedBy(currentActorProvider.getCurrentActor());
        permissionRepository.update(permission);
        log.info("Deactivated permission: {} (uuid: {})", permission.getName(), permissionUuid);
    }
}
