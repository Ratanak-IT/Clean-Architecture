package kh.edu.istad.platform.order.dto;

import java.util.UUID;

public record CustomerResponse(
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
