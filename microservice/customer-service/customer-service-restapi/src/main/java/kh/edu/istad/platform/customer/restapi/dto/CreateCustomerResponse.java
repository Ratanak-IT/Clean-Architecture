package kh.edu.istad.platform.customer.restapi.dto;

import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;

public record CreateCustomerResponse(
        String customerId,
        String familyName,
        String givenName,
        String username,
        String email,
        String phoneNumber,
        String customerStatus,
        String segmentId

) {
}
