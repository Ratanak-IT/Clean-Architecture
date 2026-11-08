package kh.edu.istad.platform.authorization.domain.usecase.role;

import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.platform.authorization.domain.entity.Role;
import kh.edu.istad.platform.authorization.domain.port.out.identity.CurrentActorProvider;
import kh.edu.istad.platform.authorization.domain.port.out.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RevokePermissionFromRoleUseCase {

    private final RoleRepository roleRepository;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public void execute(UUID roleUuid, UUID permissionUuid, String reason) {
        Role role = roleRepository.findByUuid(roleUuid);
        if (role == null || role.isDeleted()) {
            throw new ResourceNotFoundException("Role", roleUuid);
        }

        role.validateCanModify();

        String actor = currentActorProvider.getCurrentActor();
        roleRepository.revokePermission(roleUuid, permissionUuid, actor, reason);
        log.info("Revoked permission {} from role: {} (uuid: {}) by actor: {}",
                permissionUuid, role.getName(), roleUuid, actor);
    }
}
