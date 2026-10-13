package kh.edu.istad.platform.business.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record OrderApprovalId(UUID value) {
    public OrderApprovalId {
        Objects.requireNonNull(value, "OrderApprovalId must not be null");
    }

    public static OrderApprovalId newId() {
        return new OrderApprovalId(UUID.randomUUID());
    }
}
