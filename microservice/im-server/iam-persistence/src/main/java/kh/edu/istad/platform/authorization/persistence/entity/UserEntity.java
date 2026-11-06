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
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRoleEntity> userRoles = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserPermissionEntity> userPermissions = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<IdentityEntity> identities = new HashSet<>();

    public void addRole(RoleEntity role) {
        UserRoleEntity userRole = UserRoleEntity.builder()
                .user(this)
                .role(role)
                .assignedAt(Instant.now())
                .build();
        userRoles.add(userRole);
    }

    public void removeRole(RoleEntity role) {
        userRoles.removeIf(ur -> ur.getRole() != null && ur.getRole().equals(role));
    }

    public void addPermission(PermissionEntity permission, String effect) {
        UserPermissionEntity userPermission = UserPermissionEntity.builder()
                .user(this)
                .permission(permission)
                .effect(effect != null ? effect : "GRANT")
                .assignedAt(Instant.now())
                .build();
        userPermissions.add(userPermission);
    }

    public void removePermission(PermissionEntity permission) {
        userPermissions.removeIf(up -> up.getPermission() != null && up.getPermission().equals(permission));
    }
}
