package kh.edu.istad.platform.customer.restapi.dto;

import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;

public record CustomerGetResponse(
        String customerId,
        String username,
        String familyName,
        String givenName,
        String email,
        String phoneNumber,
        CustomerStatus customerStatus,
        CustomerSegmentResponse segment
) {
}
