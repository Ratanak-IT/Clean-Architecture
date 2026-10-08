package kh.edu.istad.platform.customer.domain.dto;

import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;

import java.util.UUID;

public record UpdateCustomerCommand(
        UUID customerId,
        String familyName,
        String givenName
) {
}