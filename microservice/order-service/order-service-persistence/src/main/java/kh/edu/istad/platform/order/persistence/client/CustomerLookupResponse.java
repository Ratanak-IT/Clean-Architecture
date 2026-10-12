package kh.edu.istad.platform.order.persistence.client;

import java.util.UUID;

public record CustomerLookupResponse(
        UUID customerId
) {
}
