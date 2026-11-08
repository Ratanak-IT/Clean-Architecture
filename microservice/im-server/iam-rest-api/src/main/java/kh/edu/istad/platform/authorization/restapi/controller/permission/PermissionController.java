package kh.edu.istad.platform.authorization.restapi.controller.permission;

import jakarta.validation.Valid;
import kh.edu.istad.platform.authorization.domain.dto.PageResult;
import kh.edu.istad.platform.authorization.domain.dto.permission.command.CreatePermissionCommand;
import kh.edu.istad.platform.authorization.domain.dto.permission.command.PermissionFilterQuery;
import kh.edu.istad.platform.authorization.domain.dto.permission.command.UpdatePermissionCommand;
import kh.edu.istad.platform.authorization.domain.dto.permission.result.PermissionResult;
import kh.edu.istad.platform.authorization.domain.usecase.permission.ActivatePermissionUseCase;
import kh.edu.istad.platform.authorization.domain.usecase.permission.CreatePermissionUseCase;
import kh.edu.istad.platform.authorization.domain.usecase.permission.DeactivatePermissionUseCase;
import kh.edu.istad.platform.authorization.domain.usecase.permission.DeletePermissionUseCase;
import kh.edu.istad.platform.authorization.domain.usecase.permission.GetPermissionUseCase;
import kh.edu.istad.platform.authorization.domain.usecase.permission.ListPermissionsUseCase;
import kh.edu.istad.platform.authorization.domain.usecase.permission.UpdatePermissionUseCase;
import kh.edu.istad.platform.authorization.restapi.dto.request.CreatePermissionRequest;
import kh.edu.istad.platform.authorization.restapi.dto.request.UpdatePermissionRequest;
import kh.edu.istad.platform.authorization.restapi.dto.response.PermissionResponse;
import kh.edu.istad.platform.authorization.restapi.mapper.permission.PermissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PermissionController {

    private final CreatePermissionUseCase createPermissionUseCase;
    private final ListPermissionsUseCase listPermissionsUseCase;
    private final GetPermissionUseCase getPermissionUseCase;
    private final UpdatePermissionUseCase updatePermissionUseCase;
    private final DeletePermissionUseCase deletePermissionUseCase;
    private final ActivatePermissionUseCase activatePermissionUseCase;
    private final DeactivatePermissionUseCase deactivatePermissionUseCase;
    private final PermissionMapper permissionMapper;

    @PostMapping("/permissions")
    @ResponseStatus(HttpStatus.CREATED)
    public PermissionResponse createPermission(@Valid @RequestBody CreatePermissionRequest request) {
        CreatePermissionCommand command = permissionMapper.toCommand(request);
        PermissionResult result = createPermissionUseCase.execute(command);
        return permissionMapper.toResponse(result);
    }

    @GetMapping("/permissions")
    public PageResult<PermissionResponse> listPermissions(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "resource", required = false) String resource,
            @RequestParam(name = "action", required = false) String action,
            @RequestParam(name = "active", required = false) Boolean active,
            @RequestParam(name = "system", required = false) Boolean system,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "DESC") String sortDirection
    ) {
        PermissionFilterQuery query = PermissionFilterQuery.builder()
                .search(search)
                .resource(resource)
                .action(action)
                .active(active)
                .system(system)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();
        PageResult<PermissionResult> pageResult = listPermissionsUseCase.execute(query);
        List<PermissionResponse> content = pageResult.content().stream()
                .map(permissionMapper::toResponse)
                .toList();
        return PageResult.of(content, pageResult.pageNumber(), pageResult.pageSize(), pageResult.totalElements());
    }

    @GetMapping("/permissions/{uuid}")
    public PermissionResponse getPermissionByUuid(@PathVariable("uuid") UUID uuid) {
        PermissionResult result = getPermissionUseCase.execute(uuid);
        return permissionMapper.toResponse(result);
    }

    @PatchMapping("/permissions/{uuid}")
    public PermissionResponse updatePermission(
            @PathVariable("uuid") UUID uuid,
            @Valid @RequestBody UpdatePermissionRequest request
    ) {
        UpdatePermissionCommand command = permissionMapper.toUpdateCommand(uuid, request);
        PermissionResult result = updatePermissionUseCase.execute(command);
        return permissionMapper.toResponse(result);
    }

    @DeleteMapping("/permissions/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePermission(@PathVariable("uuid") UUID uuid) {
        deletePermissionUseCase.execute(uuid);
    }

    @PostMapping("/permissions/{uuid}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activatePermission(@PathVariable("uuid") UUID uuid) {
        activatePermissionUseCase.execute(uuid);
    }

    @PostMapping("/permissions/{uuid}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivatePermission(@PathVariable("uuid") UUID uuid) {
        deactivatePermissionUseCase.execute(uuid);
    }
}
