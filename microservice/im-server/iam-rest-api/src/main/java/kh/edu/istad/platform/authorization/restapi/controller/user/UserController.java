package kh.edu.istad.platform.authorization.restapi.controller.user;

import jakarta.validation.Valid;
import kh.edu.istad.platform.authorization.domain.dto.PageResult;
import kh.edu.istad.platform.authorization.domain.dto.identity.result.IdentityResult;
import kh.edu.istad.platform.authorization.domain.dto.user.command.*;
import kh.edu.istad.platform.authorization.domain.dto.user.result.*;
import kh.edu.istad.platform.authorization.domain.usecase.identity.GetMyIdentitiesUseCase;
import kh.edu.istad.platform.authorization.domain.usecase.identity.ListUserIdentitiesUseCase;
import kh.edu.istad.platform.authorization.domain.usecase.identity.UnlinkIdentityUseCase;
import kh.edu.istad.platform.authorization.domain.usecase.identity.UnlinkMyIdentityUseCase;
import kh.edu.istad.platform.authorization.domain.usecase.user.*;
import kh.edu.istad.platform.authorization.restapi.dto.request.*;
import kh.edu.istad.platform.authorization.restapi.dto.response.*;
import kh.edu.istad.platform.authorization.restapi.mapper.user.UserRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    // Mapper
    private final UserRestMapper userRestMapper;

    // Use cases for user
    private final RegisterUserUseCase registerUserUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final GetMyProfileUseCase getMyProfileUseCase;
    private final ListUsersUseCase listUsersUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final EnableUserUseCase enableUserUseCase;
    private final DisableUserUseCase disableUserUseCase;
    private final LockUserUseCase lockUserUseCase;
    private final UnlockUserUseCase unlockUserUseCase;
    private final ResetUserPasswordUseCase resetUserPasswordUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final GetUserAuthoritiesUseCase getUserAuthoritiesUseCase;
    private final AssignRolesToUserUseCase assignRolesToUserUseCase;
    private final RevokeRoleFromUserUseCase revokeRoleFromUserUseCase;
    private final GrantPermissionsToUserUseCase grantPermissionsToUserUseCase;
    private final RevokePermissionFromUserUseCase revokePermissionFromUserUseCase;

    // Use cases for Identities
    private final ListUserIdentitiesUseCase listUserIdentitiesUseCase;
    private final GetMyIdentitiesUseCase getMyIdentitiesUseCase;
    private final UnlinkIdentityUseCase unlinkIdentityUseCase;
    private final UnlinkMyIdentityUseCase unlinkMyIdentityUseCase;

    @PostMapping("/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterUserRequest request) {
        RegisterUserCommand command = userRestMapper.toCommand(request);
        UserResult result = registerUserUseCase.execute(command);
        return userRestMapper.toResponse(result);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        CreateUserCommand command = userRestMapper.toCommand(request);
        UserResult result = createUserUseCase.execute(command);
        return userRestMapper.toResponse(result);
    }

    @GetMapping("/users")
    public PageResult<UserSummaryResponse> listUsers(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "enabled", required = false) Boolean enabled,
            @RequestParam(name = "emailVerified", required = false) Boolean emailVerified,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "DESC") String sortDirection
    ) {
        UserFilterQuery query = UserFilterQuery.builder()
                .search(search)
                .enabled(enabled)
                .emailVerified(emailVerified)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();
        PageResult<UserSummary> pageResult = listUsersUseCase.execute(query);
        return userRestMapper.toResponse(pageResult);
    }

    @GetMapping("/users/{uuid}")
    public UserResponse getUser(@PathVariable("uuid") UUID uuid) {
        UserResult result = getUserUseCase.execute(uuid);
        return userRestMapper.toResponse(result);
    }

    @PatchMapping("/users/{uuid}")
    public UserResponse updateUserProfile(
            @PathVariable("uuid") UUID uuid,
            @RequestBody UpdateUserProfileRequest request
    ) {
        UpdateUserProfileCommand command = userRestMapper.toCommand(uuid, request);
        UserResult result = updateUserProfileUseCase.execute(command);
        return userRestMapper.toResponse(result);
    }

    @DeleteMapping("/users/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("uuid") UUID uuid) {
        deleteUserUseCase.execute(uuid);
    }

    @PostMapping("/users/{uuid}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enableUser(@PathVariable("uuid") UUID uuid) {
        enableUserUseCase.execute(uuid);
    }

    @PostMapping("/users/{uuid}/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disableUser(@PathVariable("uuid") UUID uuid) {
        disableUserUseCase.execute(uuid);
    }

    @PostMapping("/users/{uuid}/lock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void lockUser(@PathVariable("uuid") UUID uuid) {
        lockUserUseCase.execute(uuid);
    }

    @PostMapping("/users/{uuid}/unlock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlockUser(@PathVariable("uuid") UUID uuid) {
        unlockUserUseCase.execute(uuid);
    }

    @PutMapping("/users/{uuid}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetUserPassword(
            @PathVariable("uuid") UUID uuid,
            @Valid @RequestBody ResetUserPasswordRequest request
    ) {
        ResetUserPasswordCommand command = userRestMapper.toCommand(uuid, request);
        resetUserPasswordUseCase.execute(command);
    }

    @GetMapping("/users/{uuid}/authorities")
    public UserAuthoritiesResponse getUserAuthorities(@PathVariable("uuid") UUID uuid) {
        UserAuthoritiesResult result = getUserAuthoritiesUseCase.execute(uuid);
        return userRestMapper.toResponse(result);
    }

    @PostMapping("/users/{uuid}/roles")
    public List<RoleAssignmentResponse> assignRolesToUser(
            @PathVariable("uuid") UUID uuid,
            @Valid @RequestBody AssignRolesToUserRequest request
    ) {
        AssignRolesToUserCommand command = userRestMapper.toCommand(uuid, request, null);
        List<RoleAssignmentResult> results = assignRolesToUserUseCase.execute(command);
        return userRestMapper.toRoleAssignmentResponses(results);
    }

    @DeleteMapping("/users/{uuid}/roles/{roleUuid}") // not test yet
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeRoleFromUser(
            @PathVariable("uuid") UUID uuid,
            @PathVariable("roleUuid") UUID roleUuid,
            @RequestParam(name = "reason", required = false, defaultValue = "Revoked by admin") String reason
    ) {
        revokeRoleFromUserUseCase.execute(uuid, roleUuid, reason);
    }

    @PostMapping("/users/{uuid}/permissions")
    public List<PermissionGrantResponse> grantPermissionsToUser(
            @PathVariable("uuid") UUID uuid,
            @Valid @RequestBody GrantPermissionsToUserRequest request
    ) {
        GrantPermissionsToUserCommand command = userRestMapper.toCommand(uuid, request, null);
        List<PermissionGrantResult> results = grantPermissionsToUserUseCase.execute(command);
        return userRestMapper.toPermissionGrantResponses(results);
    }

    @DeleteMapping("/users/{uuid}/permissions/{permissionUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokePermissionFromUser(
            @PathVariable("uuid") UUID uuid,
            @PathVariable("permissionUuid") UUID permissionUuid,
            @RequestParam(name = "reason", required = false, defaultValue = "Revoked by admin") String reason
    ) {
        revokePermissionFromUserUseCase.execute(uuid, permissionUuid, reason);
    }

    @GetMapping("/users/{uuid}/identities")
    public List<IdentityResponse> listUserIdentities(@PathVariable("uuid") UUID uuid) {
        List<IdentityResult> results = listUserIdentitiesUseCase.execute(uuid);
        return userRestMapper.toIdentityResponses(results);
    }

    @DeleteMapping("/users/{uuid}/identities/{identityUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlinkUserIdentity(
            @PathVariable("uuid") UUID uuid,
            @PathVariable("identityUuid") UUID identityUuid
    ) {
        unlinkIdentityUseCase.execute(uuid, identityUuid);
    }

    @GetMapping("/me")
    public UserResponse getMyProfile() {
        UserResult result = getMyProfileUseCase.execute();
        return userRestMapper.toResponse(result);
    }

    @PatchMapping("/me")
    public UserResponse updateMyProfile(@RequestBody UpdateUserProfileRequest request) {
        UpdateUserProfileCommand command = userRestMapper.toCommand(null, request);
        UserResult result = updateUserProfileUseCase.execute(command);
        return userRestMapper.toResponse(result);
    }

    @PutMapping("/me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeMyPassword(@Valid @RequestBody ChangePasswordRequest request) {
        ChangePasswordCommand command = userRestMapper.toCommand(null, request);
        changePasswordUseCase.execute(command);
    }

    @GetMapping("/me/identities")
    public List<IdentityResponse> listMyIdentities() {
        List<IdentityResult> results = getMyIdentitiesUseCase.execute();
        return userRestMapper.toIdentityResponses(results);
    }

    @DeleteMapping("/me/identities/{identityUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlinkMyIdentity(@PathVariable("identityUuid") UUID identityUuid) {
        unlinkMyIdentityUseCase.execute(identityUuid);
    }
}
