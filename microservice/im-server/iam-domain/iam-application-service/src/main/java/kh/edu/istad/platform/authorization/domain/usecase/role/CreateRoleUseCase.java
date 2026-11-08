package kh.edu.istad.platform.authorization.domain.usecase.role;

import kh.edu.istad.common.domain.exception.ResourceAlreadyExistsException;
import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.platform.authorization.domain.dto.role.command.CreateRoleCommand;
import kh.edu.istad.platform.authorization.domain.dto.role.result.RoleResult;
import kh.edu.istad.platform.authorization.domain.entity.Permission;
import kh.edu.istad.platform.authorization.domain.entity.Role;
import kh.edu.istad.platform.authorization.domain.mapper.RoleDomainMapper;
import kh.edu.istad.platform.authorization.domain.port.out.identity.CurrentActorProvider;
import kh.edu.istad.platform.authorization.domain.port.out.permission.PermissionRepository;
import kh.edu.istad.platform.authorization.domain.port.out.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateRoleUseCase {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleDomainMapper roleDomainMapper;
    private final CurrentActorProvider currentActorProvider;

    @Transactional
    public RoleResult execute(CreateRoleCommand command) {
        if (roleRepository.existsByRole(command.name())) {
            throw new ResourceAlreadyExistsException("Role", "name", command.name());
        }

        Set<Permission> permissions = new HashSet<>();
        if (command.permissionUuids() != null && !command.permissionUuids().isEmpty()) {
            for (UUID permUuid : command.permissionUuids()) {
                Permission perm = permissionRepository.findByUuid(permUuid);
                if (perm == null || perm.isDeleted()) {
                    throw new ResourceNotFoundException("Permission", permUuid);
                }
                perm.validateCanAssign();
                permissions.add(perm);
            }
        }

        String actor = currentActorProvider.getCurrentActor();
        Role role = Role.builder()
                .uuid(UUID.randomUUID())
                .name(command.name())
                .description(command.description())
                .system(false)
                .active(true)
                .deleted(false)
                .permissions(permissions)
                .createdAt(Instant.now())
                .createdBy(actor)
                .build();

        Role saved = roleRepository.save(role);
        log.info("Created role: {} (uuid: {}) by actor: {}", saved.getName(), saved.getUuid(), actor);
        return roleDomainMapper.toResult(saved);
    }
}
