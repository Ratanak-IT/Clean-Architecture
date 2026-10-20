package kh.edu.istad.platform.customer.restapi.dto;

import jakarta.validation.constraints.NotBlank;

public record CustomerInitiateResponse(
        String customerId,
        String username,
        String familyName,
        String givenName,
        String email,
        String phoneNumber
) {
}
