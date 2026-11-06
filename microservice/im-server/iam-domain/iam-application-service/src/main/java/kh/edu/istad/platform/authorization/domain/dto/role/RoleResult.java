package kh.edu.istad.platform.authorization.domain.dto.role;

import lombok.Builder;

import java.util.UUID;

@Builder
public record RoleResult(
        UUID uuid,
        String name
) {
}

