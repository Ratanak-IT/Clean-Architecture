package kh.edu.istad.common.persistence.query;

import java.util.Optional;

public enum FilterOperator {
    EQ, NE, GT, GTE, LT, LTE, IN, LIKE;

    public static Optional<FilterOperator> from(String value) {
        try {
            return Optional.of(valueOf(value.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}