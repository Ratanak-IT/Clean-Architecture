package kh.edu.istad.platform.authorization.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
@Builder
public class RoleEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID uuid;

    @Column(length = 50, nullable = false, unique = true)
    private String name;

    private String description;

    @Column(name = "is_system", nullable = false)
    private boolean isSystem;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Version
    private Long version;

    @Builder.Default
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private Set<RolePermissionEntity> rolePermissions = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "role")
    private Set<UserRoleEntity> userRoles = new HashSet<>();

    public void addPermission(PermissionEntity permission, String assignedBy) {
        RolePermissionEntity existing = rolePermissions.stream()
                .filter(rp -> rp.getPermission() != null
                        && ((permission.getId() != null && permission.getId().equals(rp.getPermission().getId()))
                            || (permission.getUuid() != null && permission.getUuid().equals(rp.getPermission().getUuid()))))
                .findFirst()
                .orElse(null);
        if (existing != null) {
            existing.setRevokedAt(null);
            existing.setRevokedBy(null);
            existing.setReason(null);
            existing.setAssignedAt(Instant.now());
            existing.setAssignedBy(assignedBy);
        } else {
            RolePermissionEntity rolePermission = RolePermissionEntity.builder()
                    .role(this)
                    .permission(permission)
                    .assignedAt(Instant.now())
                    .assignedBy(assignedBy)
                    .build();
            rolePermissions.add(rolePermission);
        }
    }

    public void revokePermission(PermissionEntity permission, String revokedBy, String reason) {
        rolePermissions.stream()
                .filter(rp -> rp.getPermission() != null
                        && ((permission.getId() != null && permission.getId().equals(rp.getPermission().getId()))
                            || (permission.getUuid() != null && permission.getUuid().equals(rp.getPermission().getUuid())))
                        && rp.getRevokedAt() == null)
                .forEach(rp -> {
                    rp.setRevokedAt(Instant.now());
                    rp.setRevokedBy(revokedBy);
                    rp.setReason(reason);
                });
    }
}
