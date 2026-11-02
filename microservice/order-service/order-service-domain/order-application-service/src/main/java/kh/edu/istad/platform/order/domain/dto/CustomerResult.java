package kh.edu.istad.platform.order.domain.dto;

import java.util.UUID;

public record CustomerResult(
        String customerId,
        String username,
        String familyName,
        String givenName,
        String email,
        String phoneNumber,
        String customerStatus,
        String segment
) {
}
