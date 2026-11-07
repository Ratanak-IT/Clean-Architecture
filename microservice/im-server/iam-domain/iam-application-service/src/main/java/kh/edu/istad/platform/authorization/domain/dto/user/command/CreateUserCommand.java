package kh.edu.istad.platform.authorization.domain.dto.user.command;

import kh.edu.istad.platform.authorization.domain.valueobject.Address;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
public record CreateUserCommand(
        String username,
        String email,
        String password,
        String givenName,
        String familyName,
        String gender,
        LocalDate birthdate,
        String phoneNumber,
        String zoneinfo,
        String locale,
        Address address,
        List<UUID> roleUuids
) {
}
