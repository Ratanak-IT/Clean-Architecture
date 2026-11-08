package kh.edu.istad.platform.authorization.domain.dto.user.result;

import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
public record UserAuthoritiesResult(
        List<RoleAuthority> roles,
        List<DirectPermission> directPermissions,
        Set<String> effectivePermissions
) {
    @Builder
    public record RoleAuthority(
            UUID uuid,
            String name,
            Instant expiresAt
    ) {
    }

    @Builder
    public record DirectPermission(
            String name,
            String effect
    ) {
    }
}
