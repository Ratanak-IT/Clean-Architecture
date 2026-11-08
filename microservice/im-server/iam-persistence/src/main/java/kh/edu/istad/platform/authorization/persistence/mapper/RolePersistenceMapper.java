package kh.edu.istad.platform.authorization.persistence.mapper;

import kh.edu.istad.platform.authorization.domain.entity.Permission;
import kh.edu.istad.platform.authorization.domain.entity.Role;
import kh.edu.istad.platform.authorization.persistence.entity.PermissionEntity;
import kh.edu.istad.platform.authorization.persistence.entity.RoleEntity;
import kh.edu.istad.platform.authorization.persistence.entity.RolePermissionEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RolePersistenceMapper {

    @Mapping(target = "permissions", source = "rolePermissions")
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "role", ignore = true)
    Role toDomain(RoleEntity entity);

    @Mapping(source = "system", target = "isSystem")
    @Mapping(source = "active", target = "isActive")
    @Mapping(source = "deleted", target = "isDeleted")
    @Mapping(target = "rolePermissions", ignore = true)
    @Mapping(target = "userRoles", ignore = true)
    RoleEntity toEntity(Role domain);

    default Permission mapRolePermission(RolePermissionEntity rp) {
        if (rp == null || rp.getPermission() == null) {
            return null;
        }
        PermissionEntity pe = rp.getPermission();
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

    @AfterMapping
    default void linkPermissions(Role domain, @MappingTarget RoleEntity entity) {
        if (domain != null && domain.getPermissions() != null) {
            for (Permission p : domain.getPermissions()) {
                PermissionEntity pe = PermissionEntity.builder()
                        .id(p.getId())
                        .uuid(p.getUuid())
                        .name(p.getName())
                        .resource(p.getResource())
                        .action(p.getAction())
                        .description(p.getDescription())
                        .isSystem(p.isSystem())
                        .isActive(p.isActive())
                        .isDeleted(p.isDeleted())
                        .version(p.getVersion())
                        .build();
                entity.addPermission(pe, domain.getUpdatedBy() != null ? domain.getUpdatedBy() : "system");
            }
        }
    }
}
