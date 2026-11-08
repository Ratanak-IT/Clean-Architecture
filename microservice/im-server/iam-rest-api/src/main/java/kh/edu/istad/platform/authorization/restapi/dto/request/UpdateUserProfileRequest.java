package kh.edu.istad.platform.authorization.restapi.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateUserProfileRequest(
        @Size(max = 100) String givenName,
        @Size(max = 100) String familyName,
        @Size(max = 100) String nickname,
        @Size(max = 20) String phoneNumber,
        @Size(max = 10) String locale
) {
}
