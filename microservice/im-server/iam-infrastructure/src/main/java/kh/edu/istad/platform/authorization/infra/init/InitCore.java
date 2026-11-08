package kh.edu.istad.platform.authorization.infra.init;

import jakarta.annotation.PostConstruct;
import kh.edu.istad.platform.authorization.persistence.entity.PermissionEntity;
import kh.edu.istad.platform.authorization.persistence.entity.RoleEntity;
import kh.edu.istad.platform.authorization.persistence.entity.RolePermissionEntity;
import kh.edu.istad.platform.authorization.persistence.entity.UserEntity;
import kh.edu.istad.platform.authorization.persistence.entity.UserPermissionEntity;
import kh.edu.istad.platform.authorization.persistence.entity.UserRoleEntity;
import kh.edu.istad.platform.authorization.persistence.repository.PermissionJpaRepository;
import kh.edu.istad.platform.authorization.persistence.repository.RoleJpaRepository;
import kh.edu.istad.platform.authorization.persistence.repository.RolePermissionJpaRepository;
import kh.edu.istad.platform.authorization.persistence.repository.UserJpaRepository;
import kh.edu.istad.platform.authorization.persistence.repository.UserPermissionJpaRepository;
import kh.edu.istad.platform.authorization.persistence.repository.UserRoleJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class InitCore {

    private final PermissionJpaRepository permissionJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final RolePermissionJpaRepository rolePermissionJpaRepository;
    private final UserRoleJpaRepository userRoleJpaRepository;
    private final UserPermissionJpaRepository userPermissionJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void init() {
        List<String[]> permissionsToSeed = List.of(
                // Users
                new String[]{"user:create", "user", "create", "Create users"},
                new String[]{"user:read", "user", "read", "Read users"},
                new String[]{"user:update", "user", "update", "Update users"},
                new String[]{"user:delete", "user", "delete", "Delete users"},
                new String[]{"user:assign", "user", "assign", "Assign roles/permissions to users"},

                // Roles
                new String[]{"role:create", "role", "create", "Create roles"},
                new String[]{"role:read", "role", "read", "Read roles"},
                new String[]{"role:update", "role", "update", "Update roles"},
                new String[]{"role:delete", "role", "delete", "Delete roles"},
                new String[]{"role:assign", "role", "assign", "Assign permissions to roles"},

                // Permissions
                new String[]{"permission:create", "permission", "create", "Create permissions"},
                new String[]{"permission:read", "permission", "read", "Read permissions"},
                new String[]{"permission:update", "permission", "update", "Update permissions"},
                new String[]{"permission:delete", "permission", "delete", "Delete permissions"}
        );

        // 1. Ensure all permissions exist
        for (String[] permData : permissionsToSeed) {
            String name = permData[0];
            if (!permissionJpaRepository.existsByName(name)) {
                PermissionEntity perm = PermissionEntity.builder()
                        .uuid(UUID.randomUUID())
                        .name(name)
                        .resource(permData[1])
                        .action(permData[2])
                        .description(permData[3])
                        .isActive(true)
                        .isSystem(true)
                        .build();
                permissionJpaRepository.save(perm);
            }
        }

        // 2. Ensure ADMIN role exists
        RoleEntity adminRole = roleJpaRepository.findByName("ADMIN")
                .orElseGet(() -> {
                    RoleEntity role = RoleEntity.builder()
                            .uuid(UUID.randomUUID())
                            .name("ADMIN")
                            .description("Administrator with full privileges")
                            .isSystem(true)
                            .isActive(true)
                            .build();
                    return roleJpaRepository.save(role);
                });

        // 3. Link all permissions to ADMIN role directly in role_permissions table
        List<PermissionEntity> allPermissions = permissionJpaRepository.findAll();
        List<RolePermissionEntity> existingRolePerms = rolePermissionJpaRepository.findByRoleId(adminRole.getId());
        Set<Long> existingRolePermIds = existingRolePerms.stream()
                .filter(rp -> rp.getRevokedAt() == null && rp.getPermission() != null)
                .map(rp -> rp.getPermission().getId())
                .collect(Collectors.toSet());

        for (PermissionEntity p : allPermissions) {
            if (!existingRolePermIds.contains(p.getId())) {
                RolePermissionEntity rp = RolePermissionEntity.builder()
                        .role(adminRole)
                        .permission(p)
                        .assignedAt(Instant.now())
                        .assignedBy("SYSTEM")
                        .build();
                rolePermissionJpaRepository.save(rp);
            }
        }

        // 4. Ensure user ratanak exists
        UserEntity ratanak = userJpaRepository.findByUsername("ratanak")
                .orElseGet(() -> {
                    UserEntity user = UserEntity.builder()
                            .uuid(UUID.randomUUID())
                            .familyName("Thai")
                            .givenName("Ratanak")
                            .email("ratanak@gmail.com")
                            .gender("Male")
                            .emailVerified(true)
                            .username("ratanak")
                            .passwordHash(passwordEncoder.encode("Ratanak@168"))
                            .enabled(true)
                            .accountNonExpired(true)
                            .accountNonLocked(true)
                            .credentialsNonExpired(true)
                            .build();
                    return userJpaRepository.save(user);
                });

        // 5. Ensure ratanak has ADMIN role in user_roles table
        List<UserRoleEntity> existingUserRoles = userRoleJpaRepository.findByUserId(ratanak.getId());
        boolean hasAdminRole = existingUserRoles.stream()
                .anyMatch(ur -> ur.getRole() != null && ur.getRole().getId().equals(adminRole.getId()) && ur.getRevokedAt() == null);

        if (!hasAdminRole) {
            UserRoleEntity ur = UserRoleEntity.builder()
                    .user(ratanak)
                    .role(adminRole)
                    .assignedAt(Instant.now())
                    .assignedBy("SYSTEM")
                    .build();
            userRoleJpaRepository.save(ur);
        }

        // 6. ALSO ensure ratanak has DIRECT permissions for all system permissions
        List<UserPermissionEntity> existingUserPerms = userPermissionJpaRepository.findByUserId(ratanak.getId());
        Set<Long> existingUserPermIds = existingUserPerms.stream()
                .filter(up -> up.getRevokedAt() == null && up.getPermission() != null)
                .map(up -> up.getPermission().getId())
                .collect(Collectors.toSet());

        for (PermissionEntity p : allPermissions) {
            if (!existingUserPermIds.contains(p.getId())) {
                UserPermissionEntity up = UserPermissionEntity.builder()
                        .user(ratanak)
                        .permission(p)
                        .effect("GRANT")
                        .assignedAt(Instant.now())
                        .assignedBy("SYSTEM")
                        .build();
                userPermissionJpaRepository.save(up);
            }
        }

        log.info("Initialized security core: seeded {} permissions into ADMIN role and user ratanak", allPermissions.size());
    }
}
