package kh.edu.istad.platform.authorization.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Builder
public class UserEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID uuid;

    @Column(length = 50, nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "given_name", length = 100)
    private String givenName;

    @Column(name = "family_name", length = 100)
    private String familyName;

    @Column(name = "middle_name", length = 100)
    private String middleName;

    @Column(length = 100)
    private String nickname;

    @Column(length = 20)
    private String gender;

    private LocalDate birthdate;

    @Column(name = "profile_url", length = 500)
    private String profileUrl;

    @Column(name = "picture_url", length = 500)
    private String pictureUrl;

    @Column(name = "website_url", length = 500)
    private String websiteUrl;

    @Column(length = 50)
    private String zoneinfo;

    @Column(length = 10)
    private String locale;

    @Column(name = "phone_number", length = 20, unique = true)
    private String phoneNumber;

    @Column(name = "phone_number_verified", nullable = false)
    private boolean phoneNumberVerified;

    @Column(name = "address_street_address")
    private String addressStreetAddress;

    @Column(name = "address_locality", length = 100)
    private String addressLocality;

    @Column(name = "address_region", length = 100)
    private String addressRegion;

    @Column(name = "address_postal_code", length = 20)
    private String addressPostalCode;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "address_country", columnDefinition = "char(2)")
    private String addressCountry;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "account_non_expired", nullable = false)
    private boolean accountNonExpired;

    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked;

    @Column(name = "credentials_non_expired", nullable = false)
    private boolean credentialsNonExpired;

    @Column(name = "failed_login_attempts", nullable = false)
    private int failedLoginAttempts;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @Column(name = "password_changed_at")
    private Instant passwordChangedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Version
    private Long version;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserRoleEntity> userRoles = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserPermissionEntity> userPermissions = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<IdentityEntity> identities = new HashSet<>();

    public void addRole(RoleEntity role, String assignBy) {
        UserRoleEntity existing = userRoles.stream()
                .filter(ur -> ur.getRole() != null
                        && ((role.getId() != null && role.getId().equals(ur.getRole().getId()))
                            || (role.getUuid() != null && role.getUuid().equals(ur.getRole().getUuid()))))
                .findFirst()
                .orElse(null);
        if (existing != null) {
            existing.setRevokedAt(null);
            existing.setRevokedBy(null);
            existing.setReason(null);
            existing.setAssignedAt(Instant.now());
            existing.setAssignedBy(assignBy);
        } else {
            UserRoleEntity userRole = UserRoleEntity.builder()
                    .user(this)
                    .role(role)
                    .assignedAt(Instant.now())
                    .assignedBy(assignBy)
                    .build();
            userRoles.add(userRole);
        }
    }

    public void revokeRole(RoleEntity role, String revokedBy, String reason) {
        userRoles.stream()
                .filter(ur -> ur.getRole() != null
                        && ((role.getId() != null && role.getId().equals(ur.getRole().getId()))
                            || (role.getUuid() != null && role.getUuid().equals(ur.getRole().getUuid())))
                        && ur.getRevokedAt() == null)
                .forEach(ur -> {
                    ur.setRevokedAt(Instant.now());
                    ur.setRevokedBy(revokedBy);
                    ur.setReason(reason);
                });
    }

    public void addPermission(PermissionEntity permission, String effect, String assignedBy) {
        UserPermissionEntity existing = userPermissions.stream()
                .filter(up -> up.getPermission() != null
                        && ((permission.getId() != null && permission.getId().equals(up.getPermission().getId()))
                            || (permission.getUuid() != null && permission.getUuid().equals(up.getPermission().getUuid()))))
                .findFirst()
                .orElse(null);
        if (existing != null) {
            existing.setEffect(effect != null ? effect : "GRANT");
            existing.setRevokedAt(null);
            existing.setRevokedBy(null);
            existing.setReason(null);
            existing.setAssignedAt(Instant.now());
            existing.setAssignedBy(assignedBy);
        } else {
            UserPermissionEntity userPermission = UserPermissionEntity.builder()
                    .user(this)
                    .permission(permission)
                    .effect(effect != null ? effect : "GRANT")
                    .assignedAt(Instant.now())
                    .assignedBy(assignedBy)
                    .build();
            userPermissions.add(userPermission);
        }
    }

    public void revokePermission(PermissionEntity permission, String revokedBy, String reason) {
        userPermissions.stream()
                .filter(up -> up.getPermission() != null
                        && ((permission.getId() != null && permission.getId().equals(up.getPermission().getId()))
                            || (permission.getUuid() != null && permission.getUuid().equals(up.getPermission().getUuid())))
                        && up.getRevokedAt() == null)
                .forEach(up -> {
                    up.setRevokedAt(Instant.now());
                    up.setRevokedBy(revokedBy);
                    up.setReason(reason);
                });
    }

}
