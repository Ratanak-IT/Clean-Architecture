package kh.edu.istad.platform.order.dto;

import java.util.UUID;

public record CustomerLookupResponse(
        UUID customerId
) {
}
