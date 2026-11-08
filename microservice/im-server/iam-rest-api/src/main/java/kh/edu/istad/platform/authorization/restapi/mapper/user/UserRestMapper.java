package kh.edu.istad.platform.authorization.restapi.mapper.user;

import kh.edu.istad.platform.authorization.domain.dto.identity.result.IdentityResult;
import kh.edu.istad.platform.authorization.domain.dto.user.command.*;
import kh.edu.istad.platform.authorization.domain.dto.user.result.*;
import kh.edu.istad.platform.authorization.domain.valueobject.Address;
import kh.edu.istad.platform.authorization.restapi.dto.request.*;
import kh.edu.istad.platform.authorization.restapi.dto.response.*;
import kh.edu.istad.platform.authorization.domain.dto.PageResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserRestMapper {

    // Request to Command mappings
    RegisterUserCommand toCommand(RegisterUserRequest request);

    CreateUserCommand toCommand(CreateUserRequest request);

    Address toDomain(AddressRequest request);

    @Mapping(target = "userUuid", source = "userUuid")
    UpdateUserProfileCommand toCommand(UUID userUuid, UpdateUserProfileRequest request);

    @Mapping(target = "userUuid", source = "userUuid")
    ChangePasswordCommand toCommand(UUID userUuid, ChangePasswordRequest request);

    @Mapping(target = "userUuid", source = "userUuid")
    ResetUserPasswordCommand toCommand(UUID userUuid, ResetUserPasswordRequest request);

    @Mapping(target = "userUuid", source = "userUuid")
    @Mapping(target = "assignedBy", source = "assignedBy")
    AssignRolesToUserCommand toCommand(UUID userUuid, AssignRolesToUserRequest request, String assignedBy);

    @Mapping(target = "userUuid", source = "userUuid")
    @Mapping(target = "grantedBy", source = "grantedBy")
    GrantPermissionsToUserCommand toCommand(UUID userUuid, GrantPermissionsToUserRequest request, String grantedBy);

    // Result to Response mappings
    UserResponse toResponse(UserResult result);

    UserSummaryResponse toResponse(UserSummary summary);

    List<UserSummaryResponse> toUserSummaryResponses(List<UserSummary> summaries);

    default PageResult<UserSummaryResponse> toResponse(PageResult<UserSummary> pageResult) {
        if (pageResult == null) {
            return null;
        }
        return PageResult.<UserSummaryResponse>builder()
                .content(toUserSummaryResponses(pageResult.content()))
                .pageNumber(pageResult.pageNumber())
                .pageSize(pageResult.pageSize())
                .totalElements(pageResult.totalElements())
                .totalPages(pageResult.totalPages())
                .first(pageResult.first())
                .last(pageResult.last())
                .build();
    }

    RoleAssignmentResponse toResponse(RoleAssignmentResult result);

    List<RoleAssignmentResponse> toRoleAssignmentResponses(List<RoleAssignmentResult> results);

    PermissionGrantResponse toResponse(PermissionGrantResult result);

    List<PermissionGrantResponse> toPermissionGrantResponses(List<PermissionGrantResult> results);

    UserAuthoritiesResponse toResponse(UserAuthoritiesResult result);

    IdentityResponse toResponse(IdentityResult result);

    List<IdentityResponse> toIdentityResponses(List<IdentityResult> results);
}
