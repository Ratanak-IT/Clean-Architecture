package kh.edu.istad.platform.customer.restapi.dto;

public record CustomerUpdateByIdRequest(
        String familyName,
        String givenName,
        String phoneNumber
) {
}
