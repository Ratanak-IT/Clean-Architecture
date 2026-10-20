package kh.edu.istad.platform.customer.restapi.dto;

public record CreateCustomerRequest(
        String familyName,
        String givenName,
        String username,
        String email,
        String phoneNumber,
        String segmentId
) {
}
