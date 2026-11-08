package kh.edu.istad.platform.authorization.domain.mapper;

import kh.edu.istad.platform.authorization.domain.dto.permission.command.CreatePermissionCommand;
import kh.edu.istad.platform.authorization.domain.dto.permission.result.PermissionResult;
import kh.edu.istad.platform.authorization.domain.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PermissionDomainMapper {

    PermissionResult toResult(Permission permission);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "name", expression = "java(command.name())")
    @Mapping(target = "resource", source = "command.resource")
    @Mapping(target = "action", source = "command.action")
    @Mapping(target = "description", source = "command.description")
    @Mapping(target = "system", constant = "false")
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "isSystem", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "createdBy", source = "actor")
    @Mapping(target = "updatedAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "updatedBy", source = "actor")
    @Mapping(target = "version", constant = "0L")
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "permission", ignore = true)
    Permission fromCreateCommand(CreatePermissionCommand command, String actor);
}
