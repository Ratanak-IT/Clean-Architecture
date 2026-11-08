package kh.edu.istad.platform.authorization.restapi.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RoleSummaryResponse(
        UUID uuid,
        String name,
        String description,
        Boolean system,
        Boolean active,
        Integer permissionCount,
        Instant createdAt
) {
}

