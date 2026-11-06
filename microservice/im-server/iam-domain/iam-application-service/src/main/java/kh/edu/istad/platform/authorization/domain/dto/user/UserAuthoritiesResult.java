package kh.edu.istad.platform.authorization.domain.dto.user;

import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record UserAuthoritiesResult(
        UUID userUuid,
        String username,
        Set<String> roles,
        Set<String> permissions
) {
}

