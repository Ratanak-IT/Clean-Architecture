package kh.edu.istad.platform.customer.domain.dto;

import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;

public record GetCustomerResult(
        String customerId,
        String familyName,
        String givenName,
        String username,
        String email,
        String phoneNumber,
        CustomerStatus customerStatus,
        CustomerSegmentResult segment

) {
}
