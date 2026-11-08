package kh.edu.istad.platform.authorization.domain.mapper;

import kh.edu.istad.platform.authorization.domain.dto.identity.command.FederatedUserCommand;
import kh.edu.istad.platform.authorization.domain.dto.identity.result.AuthUserResult;
import kh.edu.istad.platform.authorization.domain.dto.user.command.CreateUserCommand;
import kh.edu.istad.platform.authorization.domain.dto.user.command.RegisterUserCommand;
import kh.edu.istad.platform.authorization.domain.dto.user.result.*;
import kh.edu.istad.platform.authorization.domain.entity.Permission;
import kh.edu.istad.platform.authorization.domain.entity.Role;
import kh.edu.istad.platform.authorization.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserDomainMapper {

    @Mapping(target = "uuid", source = "user.uuid")
    @Mapping(target = "username", expression = "java(user.getUsername() != null ? user.getUsername().value() : null)")
    @Mapping(target = "email", expression = "java(user.getEmail() != null ? user.getEmail().value() : null)")
    @Mapping(target = "emailVerified", source = "user.emailVerified")
    @Mapping(target = "name", expression = "java(toFullName(user))")
    @Mapping(target = "locale", source = "user.locale")
    @Mapping(target = "enabled", source = "user.enabled")
    @Mapping(target = "roles", expression = "java(mapRolesToRoleSummaries(user.getRoles()))")
    @Mapping(target = "createdAt", source = "user.createdAt")
    @Mapping(target = "updatedAt", source = "user.updatedAt")
    UserResult toResult(User user);

    @Mapping(target = "uuid", source = "uuid")
    @Mapping(target = "name", source = "name")
    UserResult.RoleSummary toRoleSummary(Role role);

    @Mapping(target = "uuid", source = "user.uuid")
    @Mapping(target = "username", expression = "java(user.getUsername() != null ? user.getUsername().value() : null)")
    @Mapping(target = "email", expression = "java(user.getEmail() != null ? user.getEmail().value() : null)")
    @Mapping(target = "givenName", source = "user.givenName")
    @Mapping(target = "familyName", source = "user.familyName")
    @Mapping(target = "pictureUrl", source = "user.pictureUrl")
    @Mapping(target = "enabled", source = "user.enabled")
    @Mapping(target = "emailVerified", source = "user.emailVerified")
    @Mapping(target = "createdAt", source = "user.createdAt")
    @Mapping(target = "lastLoginAt", source = "user.lastLoginAt")
    UserSummary toSummary(User user);

    default UserAuthoritiesResult toAuthoritiesResult(User user) {
        if (user == null) {
            return null;
        }

        List<UserAuthoritiesResult.RoleAuthority> roleAuthorities = (user.getRoles() != null)
                ? user.getRoles().stream()
                .filter(r -> r.isActive() && !r.isDeleted())
                .map(r -> UserAuthoritiesResult.RoleAuthority.builder()
                        .uuid(r.getUuid())
                        .name(r.getName())
                        .build())
                .toList()
                : Collections.emptyList();

        List<UserAuthoritiesResult.DirectPermission> directPermissions = (user.getPermissions() != null)
                ? user.getPermissions().stream()
                .filter(p -> p.isActive() && !p.isDeleted())
                .map(p -> UserAuthoritiesResult.DirectPermission.builder()
                        .name(p.getName())
                        .effect("GRANT")
                        .build())
                .toList()
                : Collections.emptyList();

        Set<String> effectivePermissions = user.getEffectivePermissions(null);

        return UserAuthoritiesResult.builder()
                .roles(roleAuthorities)
                .directPermissions(directPermissions)
                .effectivePermissions(effectivePermissions)
                .build();
    }

    @Mapping(target = "userUuid", source = "user.uuid")
    @Mapping(target = "username", expression = "java(user.getUsername() != null ? user.getUsername().value() : null)")
    @Mapping(target = "email", expression = "java(user.getEmail() != null ? user.getEmail().value() : null)")
    @Mapping(target = "passwordHash", source = "user.passwordHash")
    @Mapping(target = "enabled", source = "user.enabled")
    @Mapping(target = "accountNonExpired", source = "user.accountNonExpired")
    @Mapping(target = "accountNonLocked", source = "user.accountNonLocked")
    @Mapping(target = "credentialsNonExpired", source = "user.credentialsNonExpired")
    @Mapping(target = "roles", expression = "java(extractRoleNames(user.getRoles()))")
    @Mapping(target = "permissions", expression = "java(extractEffectivePermissions(user))")
    AuthUserResult toAuthUserResult(User user);

    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "username", expression = "java(new kh.edu.istad.platform.authorization.domain.valueobject.Username(command.username()))")
    @Mapping(target = "email", expression = "java(new kh.edu.istad.platform.authorization.domain.valueobject.Email(command.email()))")
    @Mapping(target = "passwordHash", source = "hashedPassword")
    @Mapping(target = "givenName", source = "command.givenName")
    @Mapping(target = "familyName", source = "command.familyName")
    @Mapping(target = "locale", source = "command.locale")
    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "accountNonExpired", constant = "true")
    @Mapping(target = "accountNonLocked", constant = "true")
    @Mapping(target = "credentialsNonExpired", constant = "true")
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    User fromRegisterCommand(RegisterUserCommand command, String hashedPassword, Set<Role> roles);

    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "username", expression = "java(new kh.edu.istad.platform.authorization.domain.valueobject.Username(command.username()))")
    @Mapping(target = "email", expression = "java(new kh.edu.istad.platform.authorization.domain.valueobject.Email(command.email()))")
    @Mapping(target = "passwordHash", source = "hashedPassword")
    @Mapping(target = "givenName", source = "command.givenName")
    @Mapping(target = "familyName", source = "command.familyName")
    @Mapping(target = "gender", source = "command.gender")
    @Mapping(target = "birthdate", source = "command.birthdate")
    @Mapping(target = "phoneNumber", expression = "java(command.phoneNumber() != null ? new kh.edu.istad.platform.authorization.domain.valueobject.PhoneNumber(command.phoneNumber()) : null)")
    @Mapping(target = "zoneinfo", source = "command.zoneinfo")
    @Mapping(target = "locale", source = "command.locale")
    @Mapping(target = "address", source = "command.address")
    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "emailVerified", constant = "true")
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "accountNonExpired", constant = "true")
    @Mapping(target = "accountNonLocked", constant = "true")
    @Mapping(target = "credentialsNonExpired", constant = "true")
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    User fromCreateCommand(CreateUserCommand command, String hashedPassword, Set<Role> roles);

    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "username", expression = "java(new kh.edu.istad.platform.authorization.domain.valueobject.Username(uniqueUsername))")
    @Mapping(target = "email", expression = "java(emailStr != null ? new kh.edu.istad.platform.authorization.domain.valueobject.Email(emailStr) : null)")
    @Mapping(target = "emailVerified", expression = "java(command.email() != null && !command.email().isBlank())")
    @Mapping(target = "givenName", source = "command.name")
    @Mapping(target = "pictureUrl", source = "command.pictureUrl")
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "accountNonExpired", constant = "true")
    @Mapping(target = "accountNonLocked", constant = "true")
    @Mapping(target = "credentialsNonExpired", constant = "true")
    @Mapping(target = "lastLoginAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    User fromFederatedCommand(FederatedUserCommand command, String uniqueUsername, String emailStr);

    @Mapping(target = "userUuid", source = "user.uuid")
    @Mapping(target = "roleUuid", source = "role.uuid")
    @Mapping(target = "roleName", source = "role.name")
    @Mapping(target = "assignedAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "assignedBy", source = "assignedBy")
    @Mapping(target = "reason", source = "reason")
    RoleAssignmentResult toRoleAssignmentResult(User user, Role role, String assignedBy, String reason);

    @Mapping(target = "userUuid", source = "user.uuid")
    @Mapping(target = "permissionUuid", source = "permission.uuid")
    @Mapping(target = "permissionName", source = "permission.name")
    @Mapping(target = "grantedAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "grantedBy", source = "grantedBy")
    @Mapping(target = "reason", source = "reason")
    PermissionGrantResult toPermissionGrantResult(User user, Permission permission, String grantedBy, String reason);

    default String toFullName(User user) {
        if (user == null) {
            return null;
        }
        String fullName = "";
        if (user.getGivenName() != null) {
            fullName += user.getGivenName();
        }
        if (user.getFamilyName() != null) {
            fullName = fullName.isBlank() ? user.getFamilyName() : fullName + " " + user.getFamilyName();
        }
        return fullName;
    }

    default List<UserResult.RoleSummary> mapRolesToRoleSummaries(Set<Role> roles) {
        if (roles == null) {
            return Collections.emptyList();
        }
        return roles.stream()
                .filter(r -> r.isActive() && !r.isDeleted())
                .map(this::toRoleSummary)
                .toList();
    }

    default Set<String> extractRoleNames(Set<Role> roles) {
        if (roles == null) {
            return Collections.emptySet();
        }
        return roles.stream()
                .filter(r -> r.isActive() && !r.isDeleted())
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    default Set<String> extractPermissionNames(Set<Permission> permissions) {
        if (permissions == null) {
            return Collections.emptySet();
        }
        return permissions.stream()
                .filter(p -> p.isActive() && !p.isDeleted())
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }

    default Set<String> extractEffectivePermissions(User user) {
        if (user == null) {
            return Collections.emptySet();
        }
        Set<String> effective = new java.util.HashSet<>();
        if (user.getRoles() != null) {
            effective.addAll(user.getEffectivePermissions(null));
        }
        if (user.getPermissions() != null) {
            user.getPermissions().stream()
                    .filter(p -> p.isActive() && !p.isDeleted())
                    .map(Permission::getName)
                    .forEach(effective::add);
        }
        return effective;
    }
}
