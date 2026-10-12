package kh.edu.istad.common.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record TrackingId(UUID value) {
    public TrackingId {
        Objects.requireNonNull(value, "TrackingId must not be null");
    }

    public static TrackingId newId() {
        return new TrackingId(UUID.randomUUID());
    }
}
