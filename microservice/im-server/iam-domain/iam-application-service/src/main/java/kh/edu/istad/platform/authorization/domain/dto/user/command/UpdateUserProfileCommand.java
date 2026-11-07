package kh.edu.istad.platform.authorization.domain.dto.user.command;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record UpdateUserProfileCommand(
        UUID userUuid,
        String givenName,
        String familyName,
        String middleName,
        String nickname,
        String gender,
        LocalDate birthdate,
        String phoneNumber,
        String profileUrl,
        String pictureUrl,
        String websiteUrl,
        String zoneinfo,
        String locale,
        String addressStreetAddress,
        String addressLocality,
        String addressRegion,
        String addressPostalCode,
        String addressCountry
) {
}

