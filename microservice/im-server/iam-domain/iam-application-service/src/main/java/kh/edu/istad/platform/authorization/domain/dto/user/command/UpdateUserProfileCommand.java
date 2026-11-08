package kh.edu.istad.platform.authorization.domain.dto.user.command;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UpdateUserProfileCommand(
        UUID userUuid,
        String givenName,
        String nickname,
        String phoneNumber,
        String locale
) {
}
