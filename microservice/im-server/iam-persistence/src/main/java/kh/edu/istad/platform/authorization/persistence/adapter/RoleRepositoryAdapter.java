package kh.edu.istad.platform.authorization.persistence.adapter;

import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.platform.authorization.domain.dto.PageResult;
import kh.edu.istad.platform.authorization.domain.dto.role.command.AssignPermissionsToRoleCommand;
import kh.edu.istad.platform.authorization.domain.dto.role.command.RoleFilterQuery;
import kh.edu.istad.platform.authorization.domain.dto.role.result.PermissionAssignmentResult;
import kh.edu.istad.platform.authorization.domain.entity.Permission;
import kh.edu.istad.platform.authorization.domain.entity.Role;
import kh.edu.istad.platform.authorization.domain.port.out.role.RoleRepository;
import kh.edu.istad.platform.authorization.persistence.entity.PermissionEntity;
import kh.edu.istad.platform.authorization.persistence.entity.RoleEntity;
import kh.edu.istad.platform.authorization.persistence.mapper.RolePersistenceMapper;
import kh.edu.istad.platform.authorization.persistence.repository.PermissionJpaRepository;
import kh.edu.istad.platform.authorization.persistence.repository.RoleJpaRepository;
import kh.edu.istad.platform.authorization.persistence.specification.RoleSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;
    private final PermissionJpaRepository permissionJpaRepository;
    private final RolePersistenceMapper mapper;

    @Override
    public Role save(Role role) {
        RoleEntity entity = mapper.toEntity(role);
        if (entity.getUuid() == null) {
            entity.setUuid(UUID.randomUUID());
        }
        if (role.getPermissions() != null) {
            String assignedBy = role.getCreatedBy() != null ? role.getCreatedBy() : "system";
            for (Permission perm : role.getPermissions()) {
                if (perm.getUuid() != null) {
                    permissionJpaRepository.findByUuid(perm.getUuid())
                            .ifPresent(p -> entity.addPermission(p, assignedBy));
                }
            }
        }
        return mapper.toDomain(roleJpaRepository.save(entity));
    }

    @Override
    public Role findByUuid(UUID uuid) {
        return roleJpaRepository.findByUuid(uuid)
                .map(mapper::toDomain)
                .orElseThrow(() -> new ResourceNotFoundException("Role", uuid));
    }

    @Override
    public Role findByRole(String role) {
        return roleJpaRepository.findByName(role)
                .map(mapper::toDomain)
                .orElseThrow(() -> new ResourceNotFoundException("Role with name: " + role + " not found"));
    }

    @Override
    public boolean existsByRole(String role) {
        return roleJpaRepository.existsByName(role);
    }

    @Override
    public List<Role> findAll() {
        return roleJpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public PageResult<Role> findAll(RoleFilterQuery query) {
        Specification<RoleEntity> spec = RoleSpecification.filter(query);

        Sort sort = Sort.by(
                "DESC".equalsIgnoreCase(query.sortDirection()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                query.sortBy()
        );
        Pageable pageable = PageRequest.of(query.page(), query.size(), sort);
        Page<RoleEntity> entityPage = roleJpaRepository.findAll(spec, pageable);

        List<Role> content = entityPage.getContent().stream()
                .map(mapper::toDomain)
                .toList();

        return PageResult.of(content, entityPage.getNumber(), entityPage.getSize(), entityPage.getTotalElements());
    }

    @Override
    public Role update(Role role) {
        RoleEntity existing = roleJpaRepository.findByUuid(role.getUuid())
                .orElseThrow(() -> new ResourceNotFoundException("Role", role.getUuid()));

        existing.setName(role.getName());
        existing.setDescription(role.getDescription());
        existing.setActive(role.isActive());
        existing.setDeleted(role.isDeleted());
        existing.setDeletedAt(role.getDeletedAt());
        existing.setDeletedBy(role.getDeletedBy());
        existing.setUpdatedAt(role.getUpdatedAt() != null ? role.getUpdatedAt() : Instant.now());
        existing.setUpdatedBy(role.getUpdatedBy());

        return mapper.toDomain(roleJpaRepository.save(existing));
    }

    @Override
    public void deleteByUuid(UUID uuid) {
        roleJpaRepository.findByUuid(uuid).ifPresent(roleJpaRepository::delete);
    }

    @Override
    public List<PermissionAssignmentResult> assignPermissions(AssignPermissionsToRoleCommand command) {
        RoleEntity role = roleJpaRepository.findByUuid(command.roleUuid())
                .orElseThrow(() -> new ResourceNotFoundException("Role", command.roleUuid()));

        List<PermissionAssignmentResult> results = new ArrayList<>();
        for (UUID permUuid : command.permissionUuids()) {
            PermissionEntity perm = permissionJpaRepository.findByUuid(permUuid)
                    .orElseThrow(() -> new ResourceNotFoundException("Permission", permUuid));
            role.addPermission(perm, command.assignedBy());
            results.add(PermissionAssignmentResult.builder()
                    .roleUuid(role.getUuid())
                    .permissionUuid(perm.getUuid())
                    .permissionName(perm.getName())
                    .assignedAt(Instant.now())
                    .assignedBy(command.assignedBy())
                    .reason(command.reason())
                    .build());
        }
        roleJpaRepository.save(role);
        return results;
    }

    @Override
    public void revokePermission(UUID roleUuid, UUID permissionUuid, String revokedBy, String reason) {
        RoleEntity role = roleJpaRepository.findByUuid(roleUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Role", roleUuid));
        PermissionEntity perm = permissionJpaRepository.findByUuid(permissionUuid)
                .orElseThrow(() -> new ResourceNotFoundException("Permission", permissionUuid));
        role.revokePermission(perm, revokedBy, reason);
        roleJpaRepository.save(role);
    }
}
