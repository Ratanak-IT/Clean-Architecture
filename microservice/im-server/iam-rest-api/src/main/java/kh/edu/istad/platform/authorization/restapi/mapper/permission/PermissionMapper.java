package kh.edu.istad.platform.authorization.restapi.mapper.permission;

import kh.edu.istad.platform.authorization.domain.dto.permission.command.CreatePermissionCommand;
import kh.edu.istad.platform.authorization.domain.dto.permission.command.UpdatePermissionCommand;
import kh.edu.istad.platform.authorization.domain.dto.permission.result.PermissionResult;
import kh.edu.istad.platform.authorization.restapi.dto.request.CreatePermissionRequest;
import kh.edu.istad.platform.authorization.restapi.dto.request.UpdatePermissionRequest;
import kh.edu.istad.platform.authorization.restapi.dto.response.PermissionResponse;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    CreatePermissionCommand toCommand(CreatePermissionRequest request);

    PermissionResponse toResponse(PermissionResult result);

    default UpdatePermissionCommand toUpdateCommand(UUID permissionUuid, UpdatePermissionRequest request) {
        return UpdatePermissionCommand.builder()
                .permissionUuid(permissionUuid)
                .description(request != null ? request.description() : null)
                .build();
    }
}