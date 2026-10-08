package kh.edu.istad.platform.customer.domain.dto;


// request to Domain core

public record InitiateCustomerCommand(
        String userName,
        String familyName,
        String givenName,
        String email,
        String phoneNumber

) {
}
