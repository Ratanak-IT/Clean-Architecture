package kh.edu.istad.platform.authorization.restapi.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
public record CreateUserRequest(
        @NotBlank @Size(min = 3, max = 50) String username,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 100) String password,
        String givenName,
        String familyName,
        String gender,
        LocalDate birthdate,
        String phoneNumber,
        String zoneinfo,
        String locale,
        @Valid AddressRequest address,
        List<UUID> roleUuids
) {
}
