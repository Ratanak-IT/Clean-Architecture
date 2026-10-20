package kh.edu.istad.platform.customer.domain.dto;

public record CreateCustomerResult(
        String familyName,
        String givenName,
        String username,
        String email,
        String phoneNumber,
        String segmentId
) {
}
