package kh.edu.istad.platform.authorization.restapi.mapper.role;

import kh.edu.istad.platform.authorization.domain.dto.role.command.AssignPermissionsToRoleCommand;
import kh.edu.istad.platform.authorization.domain.dto.role.command.CreateRoleCommand;
import kh.edu.istad.platform.authorization.domain.dto.role.command.UpdateRoleCommand;
import kh.edu.istad.platform.authorization.domain.dto.role.result.PermissionAssignmentResult;
import kh.edu.istad.platform.authorization.domain.dto.role.result.RoleResult;
import kh.edu.istad.platform.authorization.domain.dto.role.result.RoleSummary;
import kh.edu.istad.platform.authorization.restapi.dto.request.AssignPermissionsToRoleRequest;
import kh.edu.istad.platform.authorization.restapi.dto.request.CreateRoleRequest;
import kh.edu.istad.platform.authorization.restapi.dto.request.UpdateRoleRequest;
import kh.edu.istad.platform.authorization.restapi.dto.response.PermissionAssignmentResponse;
import kh.edu.istad.platform.authorization.restapi.dto.response.RoleResponse;
import kh.edu.istad.platform.authorization.restapi.dto.response.RoleSummaryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface RoleRestMapper {

    RoleResponse toResponse(RoleResult result);

    RoleSummaryResponse toSummaryResponse(RoleSummary summary);

    CreateRoleCommand toCommand(CreateRoleRequest request);

    default UpdateRoleCommand toUpdateCommand(UUID roleUuid, UpdateRoleRequest request) {
        if (request == null) {
            return null;
        }
        return UpdateRoleCommand.builder()
                .roleUuid(roleUuid)
                .name(request.name())
                .description(request.description())
                .build();
    }

    default AssignPermissionsToRoleCommand toAssignCommand(UUID roleUuid, AssignPermissionsToRoleRequest request) {
        if (request == null) {
            return null;
        }
        return AssignPermissionsToRoleCommand.builder()
                .roleUuid(roleUuid)
                .permissionUuids(request.permissionUuids())
                .reason(request.reason())
                .build();
    }

    PermissionAssignmentResponse toAssignmentResponse(PermissionAssignmentResult result);
}
