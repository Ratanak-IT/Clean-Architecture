package kh.edu.istad.platform.authorization.persistence.mapper;

import kh.edu.istad.platform.authorization.domain.entity.Permission;
import kh.edu.istad.platform.authorization.domain.entity.Role;
import kh.edu.istad.platform.authorization.domain.entity.User;
import kh.edu.istad.platform.authorization.domain.valueobject.Email;
import kh.edu.istad.platform.authorization.domain.valueobject.PhoneNumber;
import kh.edu.istad.platform.authorization.domain.valueobject.Username;
import kh.edu.istad.platform.authorization.persistence.entity.PermissionEntity;
import kh.edu.istad.platform.authorization.persistence.entity.RoleEntity;
import kh.edu.istad.platform.authorization.persistence.entity.UserEntity;
import kh.edu.istad.platform.authorization.persistence.entity.UserPermissionEntity;
import kh.edu.istad.platform.authorization.persistence.entity.UserRoleEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {

    default String fromUsername(Username username) {
        return username != null ? username.value() : null;
    }

    default Username toUsername(String username) {
        return username != null ? new Username(username) : null;
    }

    default String fromEmail(Email email) {
        return email != null ? email.value() : null;
    }

    default Email toEmail(String email) {
        return email != null ? new Email(email) : null;
    }

    default String fromPhone(PhoneNumber phone) {
        return phone != null ? phone.value() : null;
    }

    default PhoneNumber toPhone(String phone) {
        return phone != null ? new PhoneNumber(phone) : null;
    }

    @Mapping(source = "addressStreetAddress", target = "address.streetAddress")
    @Mapping(source = "addressLocality", target = "address.locality")
    @Mapping(source = "addressRegion", target = "address.region")
    @Mapping(source = "addressPostalCode", target = "address.postalCode")
    @Mapping(source = "addressCountry", target = "address.country")
    @Mapping(source = "userRoles", target = "roles")
    @Mapping(source = "userPermissions", target = "permissions")
    @Mapping(target = "password", ignore = true)
    User toDomain(UserEntity entity);

    @Mapping(source = "address.streetAddress", target = "addressStreetAddress")
    @Mapping(source = "address.locality", target = "addressLocality")
    @Mapping(source = "address.region", target = "addressRegion")
    @Mapping(source = "address.postalCode", target = "addressPostalCode")
    @Mapping(source = "address.country", target = "addressCountry")
    @Mapping(source = "deleted", target = "isDeleted")
    @Mapping(target = "userRoles", ignore = true)
    @Mapping(target = "userPermissions", ignore = true)
    @Mapping(target = "identities", ignore = true)
    UserEntity toEntity(User domain);

    @Mapping(source = "address.streetAddress", target = "addressStreetAddress")
    @Mapping(source = "address.locality", target = "addressLocality")
    @Mapping(source = "address.region", target = "addressRegion")
    @Mapping(source = "address.postalCode", target = "addressPostalCode")
    @Mapping(source = "address.country", target = "addressCountry")
    @Mapping(target = "userRoles", ignore = true)
    @Mapping(target = "userPermissions", ignore = true)
    @Mapping(target = "identities", ignore = true)
    void updateEntityFromDomain(User domain, @MappingTarget UserEntity entity);

    default Role mapUserRole(UserRoleEntity ur) {
        if (ur == null || ur.getRole() == null) {
            return null;
        }
        if (ur.getRevokedAt() != null) {
            return null;
        }
        if (ur.getExpiresAt() != null && ur.getExpiresAt().isBefore(java.time.Instant.now())) {
            return null;
        }
        RoleEntity re = ur.getRole();
        return Role.builder()
                .id(re.getId())
                .uuid(re.getUuid())
                .name(re.getName())
                .description(re.getDescription())
                .system(re.isSystem())
                .active(re.isActive())
                .deleted(re.isDeleted())
                .version(re.getVersion())
                .build();
    }

    default Permission mapUserPermission(UserPermissionEntity up) {
        if (up == null || up.getPermission() == null) {
            return null;
        }
        if (up.getRevokedAt() != null) {
            return null;
        }
        if (up.getExpiresAt() != null && up.getExpiresAt().isBefore(java.time.Instant.now())) {
            return null;
        }
        PermissionEntity pe = up.getPermission();
        return Permission.builder()
                .id(pe.getId())
                .uuid(pe.getUuid())
                .name(pe.getName())
                .resource(pe.getResource())
                .action(pe.getAction())
                .description(pe.getDescription())
                .system(pe.isSystem())
                .active(pe.isActive())
                .deleted(pe.isDeleted())
                .version(pe.getVersion())
                .build();
    }
}
