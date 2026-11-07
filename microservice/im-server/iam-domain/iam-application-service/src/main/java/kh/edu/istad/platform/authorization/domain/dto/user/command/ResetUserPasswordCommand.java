package kh.edu.istad.platform.authorization.domain.dto.user.command;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ResetUserPasswordCommand(
        UUID userUuid,
        String newPassword
) {
}

