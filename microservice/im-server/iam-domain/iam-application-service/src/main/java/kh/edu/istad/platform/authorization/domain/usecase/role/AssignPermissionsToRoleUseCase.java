package kh.edu.istad.platform.authorization.domain.usecase.role;

import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.platform.authorization.domain.dto.role.command.AssignPermissionsToRoleCommand;
import kh.edu.istad.platform.authorization.domain.dto.role.result.PermissionAssignmentResult;
import kh.edu.istad.platform.authorization.domain.entity.Permission;
import kh.edu.istad.platform.authorization.domain.entity.Role;
import kh.edu.istad.platform.authorization.domain.port.out.identity.CurrentActorProvider;
import kh.edu.istad.platform.authorization.domain.port.out.permission.PermissionRepository;
import kh.edu.istad.platform.authorization.domain.port.out.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AssignPermissionsToRoleUseCase {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public List<PermissionAssignmentResult> execute(AssignPermissionsToRoleCommand command) {
        Role role = roleRepository.findByUuid(command.roleUuid());
        if (role == null || role.isDeleted()) {
            throw new ResourceNotFoundException("Role", command.roleUuid());
        }

        role.validateCanModify();

        for (UUID permUuid : command.permissionUuids()) {
            Permission perm = permissionRepository.findByUuid(permUuid);
            if (perm == null || perm.isDeleted()) {
                throw new ResourceNotFoundException("Permission", permUuid);
            }
            perm.validateCanAssign();
        }

        String actor = command.assignedBy() != null ? command.assignedBy() : currentActorProvider.getCurrentActor();
        AssignPermissionsToRoleCommand cmdWithActor = AssignPermissionsToRoleCommand.builder()
                .roleUuid(command.roleUuid())
                .permissionUuids(command.permissionUuids())
                .assignedBy(actor)
                .reason(command.reason())
                .build();

        List<PermissionAssignmentResult> results = roleRepository.assignPermissions(cmdWithActor);
        log.info("Assigned {} permissions to role: {} (uuid: {}) by actor: {}",
                command.permissionUuids().size(), role.getName(), role.getUuid(), actor);
        return results;
    }
}
