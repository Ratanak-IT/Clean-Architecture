package kh.edu.istad.platform.customer.domain.dto;

import java.util.UUID;
// response from domain core to external system
public record InitiateCustomerResult (
        UUID customerId,
        String userName,
        String familyName,
        String givenName,
        String email,
        String phoneNumber
){
}
