package kh.edu.istad.platform.authorization.restapi.controller.role;

import jakarta.validation.Valid;
import kh.edu.istad.platform.authorization.domain.dto.PageResult;
import kh.edu.istad.platform.authorization.domain.dto.role.command.AssignPermissionsToRoleCommand;
import kh.edu.istad.platform.authorization.domain.dto.role.command.CreateRoleCommand;
import kh.edu.istad.platform.authorization.domain.dto.role.command.RoleFilterQuery;
import kh.edu.istad.platform.authorization.domain.dto.role.command.UpdateRoleCommand;
import kh.edu.istad.platform.authorization.domain.dto.role.result.PermissionAssignmentResult;
import kh.edu.istad.platform.authorization.domain.dto.role.result.RoleResult;
import kh.edu.istad.platform.authorization.domain.dto.role.result.RoleSummary;
import kh.edu.istad.platform.authorization.domain.usecase.role.*;
import kh.edu.istad.platform.authorization.restapi.dto.request.AssignPermissionsToRoleRequest;
import kh.edu.istad.platform.authorization.restapi.dto.request.CreateRoleRequest;
import kh.edu.istad.platform.authorization.restapi.dto.request.UpdateRoleRequest;
import kh.edu.istad.platform.authorization.restapi.dto.response.PermissionAssignmentResponse;
import kh.edu.istad.platform.authorization.restapi.dto.response.RoleResponse;
import kh.edu.istad.platform.authorization.restapi.dto.response.RoleSummaryResponse;
import kh.edu.istad.platform.authorization.restapi.mapper.role.RoleRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoleController {

    private final CreateRoleUseCase createRoleUseCase;
    private final ListRolesUseCase listRolesUseCase;
    private final GetRoleUseCase getRoleUseCase;
    private final UpdateRoleUseCase updateRoleUseCase;
    private final DeleteRoleUseCase deleteRoleUseCase;
    private final ActivateRoleUseCase activateRoleUseCase;
    private final DeactivateRoleUseCase deactivateRoleUseCase;
    private final AssignPermissionsToRoleUseCase assignPermissionsToRoleUseCase;
    private final RevokePermissionFromRoleUseCase revokePermissionFromRoleUseCase;
    private final RoleRestMapper roleRestMapper;

    @PostMapping("/roles")
    @ResponseStatus(HttpStatus.CREATED)
    public RoleResponse createRole(@Valid @RequestBody CreateRoleRequest request) {
        CreateRoleCommand command = roleRestMapper.toCommand(request);
        RoleResult result = createRoleUseCase.execute(command);
        return roleRestMapper.toResponse(result);
    }

    @GetMapping("/roles")
    public PageResult<RoleSummaryResponse> listRoles(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "active", required = false) Boolean active,
            @RequestParam(name = "system", required = false) Boolean system,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "DESC") String sortDirection
    ) {
        RoleFilterQuery query = RoleFilterQuery.builder()
                .search(search)
                .active(active)
                .system(system)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();
        PageResult<RoleSummary> pageResult = listRolesUseCase.execute(query);
        List<RoleSummaryResponse> content = pageResult.content().stream()
                .map(roleRestMapper::toSummaryResponse)
                .toList();
        return PageResult.of(content, pageResult.pageNumber(), pageResult.pageSize(), pageResult.totalElements());
    }

    @GetMapping("/roles/{uuid}")
    public RoleResponse getRole(@PathVariable("uuid") UUID uuid) {
        RoleResult result = getRoleUseCase.execute(uuid);
        return roleRestMapper.toResponse(result);
    }

    @PatchMapping("/roles/{uuid}")
    public RoleResponse updateRole(
            @PathVariable("uuid") UUID uuid,
            @Valid @RequestBody UpdateRoleRequest request
    ) {
        UpdateRoleCommand command = roleRestMapper.toUpdateCommand(uuid, request);
        RoleResult result = updateRoleUseCase.execute(command);
        return roleRestMapper.toResponse(result);
    }

    @DeleteMapping("/roles/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(@PathVariable("uuid") UUID uuid) {
        deleteRoleUseCase.execute(uuid);
    }

    @PostMapping("/roles/{uuid}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activateRole(@PathVariable("uuid") UUID uuid) {
        activateRoleUseCase.execute(uuid);
    }

    @PostMapping("/roles/{uuid}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateRole(@PathVariable("uuid") UUID uuid) {
        deactivateRoleUseCase.execute(uuid);
    }

    @PostMapping("/roles/{uuid}/permissions")
    public List<PermissionAssignmentResponse> assignPermissionsToRole(
            @PathVariable("uuid") UUID uuid,
            @Valid @RequestBody AssignPermissionsToRoleRequest request
    ) {
        AssignPermissionsToRoleCommand command = roleRestMapper.toAssignCommand(uuid, request);
        List<PermissionAssignmentResult> results = assignPermissionsToRoleUseCase.execute(command);
        return results.stream()
                .map(roleRestMapper::toAssignmentResponse)
                .toList();
    }

    @DeleteMapping("/roles/{uuid}/permissions/{permissionUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokePermissionFromRole(
            @PathVariable("uuid") UUID uuid,
            @PathVariable("permissionUuid") UUID permissionUuid,
            @RequestParam(name = "reason", required = false, defaultValue = "Revoked by admin") String reason
    ) {
        revokePermissionFromRoleUseCase.execute(uuid, permissionUuid, reason);
    }
}
