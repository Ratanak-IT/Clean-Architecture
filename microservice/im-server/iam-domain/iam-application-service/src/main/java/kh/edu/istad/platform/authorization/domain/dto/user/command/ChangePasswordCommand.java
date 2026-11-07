package kh.edu.istad.platform.authorization.domain.dto.user.command;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ChangePasswordCommand(
        UUID userUuid,
        String currentPassword,
        String newPassword,
        String confirmPassword
) {
}

