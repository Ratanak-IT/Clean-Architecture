package kh.edu.istad.platform.authorization.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permissions")
@Builder
public class PermissionEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID uuid;

    @Column(length = 100, nullable = false, unique = true)
    private String name;

    @Column(length = 50, nullable = false)
    private String resource;

    @Column(length = 50, nullable = false)
    private String action;

    @Column(length = 255)
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
    @OneToMany(mappedBy = "permission")
    private Set<RolePermissionEntity> rolePermissions = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "permission")
    private Set<UserPermissionEntity> userPermissions = new HashSet<>();
}
