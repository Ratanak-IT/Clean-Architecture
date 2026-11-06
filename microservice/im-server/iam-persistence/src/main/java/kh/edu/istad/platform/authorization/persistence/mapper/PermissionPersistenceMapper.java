package kh.edu.istad.platform.authorization.persistence.mapper;

import kh.edu.istad.platform.authorization.domain.entity.Permission;
import kh.edu.istad.platform.authorization.persistence.entity.PermissionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PermissionPersistenceMapper {

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "permission", ignore = true)
    Permission toDomain(PermissionEntity entity);

    @Mapping(source = "system", target = "isSystem")
    @Mapping(source = "active", target = "isActive")
    @Mapping(source = "deleted", target = "isDeleted")
    @Mapping(target = "rolePermissions", ignore = true)
    @Mapping(target = "userPermissions", ignore = true)
    PermissionEntity toEntity(Permission domain);
}
