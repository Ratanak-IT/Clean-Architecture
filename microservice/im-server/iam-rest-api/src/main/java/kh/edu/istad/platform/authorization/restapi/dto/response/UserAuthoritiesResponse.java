package kh.edu.istad.platform.authorization.restapi.dto.response;

import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
public record UserAuthoritiesResponse(
        List<RoleAuthorityResponse> roles,
        List<DirectPermissionResponse> directPermissions,
        Set<String> effectivePermissions
) {
    @Builder
    public record RoleAuthorityResponse(
            UUID uuid,
            String name,
            Instant expiresAt
    ) {
    }

    @Builder
    public record DirectPermissionResponse(
            String name,
            String effect
    ) {
    }
}
