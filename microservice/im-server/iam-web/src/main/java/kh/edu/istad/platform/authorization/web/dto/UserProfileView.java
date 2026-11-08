package kh.edu.istad.platform.authorization.web.dto;

public record UserProfileView(
        String username,
        String email,
        String givenName,
        String familyName,
        String nickname,
        String phoneNumber,
        String locale
) {
}

