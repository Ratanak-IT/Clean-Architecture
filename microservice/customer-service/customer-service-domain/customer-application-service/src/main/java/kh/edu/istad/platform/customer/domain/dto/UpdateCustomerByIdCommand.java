package kh.edu.istad.platform.customer.domain.dto;

public record UpdateCustomerByIdCommand(
        String familyName,
        String givenName,
        String phoneNumber
) {
}
