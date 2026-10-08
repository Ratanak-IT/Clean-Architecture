package kh.edu.istad.platform.customer.restapi.dto;

import jakarta.validation.constraints.NotBlank;

//conversion ខុសគ្នាពី command (In-port)
public record CustomerInitiateRequest(
        @NotBlank
        String userName,
        @NotBlank
        String familyName,
        @NotBlank
        String givenName,
        @NotBlank
        String email,
        @NotBlank
        String phoneNumber
) {
}