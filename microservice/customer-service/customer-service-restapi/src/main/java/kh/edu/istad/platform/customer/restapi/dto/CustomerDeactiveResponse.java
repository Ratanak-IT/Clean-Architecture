package kh.edu.istad.platform.customer.restapi.dto;

import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;

import java.util.UUID;

public record CustomerDeactiveResponse(
        CustomerStatus status
) {
}
