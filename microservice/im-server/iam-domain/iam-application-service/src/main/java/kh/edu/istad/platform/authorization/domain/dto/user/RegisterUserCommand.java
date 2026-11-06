package kh.edu.istad.platform.authorization.domain.dto.user;

import lombok.Builder;

@Builder
public record RegisterUserCommand(
        String username,
        String email,
        String password,
        String confirmPassword,
        String givenName,
        String familyName,
        String locale
) {
}
