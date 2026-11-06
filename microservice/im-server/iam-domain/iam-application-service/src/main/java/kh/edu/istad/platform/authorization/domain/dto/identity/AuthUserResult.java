package kh.edu.istad.platform.authorization.domain.dto.identity;

import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record AuthUserResult(
        UUID userUuid,
        String username,
        String email,
        String passwordHash,
        boolean enabled,
        boolean accountNonExpired,
        boolean accountNonLocked,
        boolean credentialsNonExpired,
        Set<String> roles,
        Set<String> permissions
) {
}

