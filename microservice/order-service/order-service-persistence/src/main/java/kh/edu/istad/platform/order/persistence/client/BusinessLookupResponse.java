package kh.edu.istad.platform.order.persistence.client;

import kh.edu.istad.common.domain.valueobject.Money;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record BusinessLookupResponse(
        UUID businessId,
        String legalName,
        String displayName,
        String email,
        String phoneNumber,
        boolean active,
        List<ProductLookupResponse> products
) {
    public record ProductLookupResponse(
            UUID productId,
            String name,
            BigDecimal price,
            int quantity,
            boolean available
    ) {}
}