package kh.edu.istad.platform.authorization.restapi.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Builder
public record AssignRolesToUserRequest(
        @NotEmpty Set<UUID> roleUuids,
        Instant expiresAt,
        String reason
) {
}
