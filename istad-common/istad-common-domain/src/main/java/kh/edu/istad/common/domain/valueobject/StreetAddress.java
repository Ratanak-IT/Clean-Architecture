package kh.edu.istad.common.domain.valueobject;

import kh.edu.istad.common.domain.exception.DomainException;

import java.util.UUID;

public record StreetAddress(UUID id, String street, String postalCode, String city) {
    private static final int MAX_STREET = 50;
    private static final int MAX_POSTAL_CODE = 10;
    private static final int MAX_CITY = 50;

    public StreetAddress {
        if (id == null) {
            throw new DomainException("Address id must not be null");
        }
        street = requireText(street, "Street", MAX_STREET);
        postalCode = requireText(postalCode, "Postal code", MAX_POSTAL_CODE);
        city = requireText(city, "City", MAX_CITY);
    }

    public static StreetAddress of(String street, String postalCode, String city) {
        return new StreetAddress(UUID.randomUUID(), street, postalCode, city);
    }

    private static String requireText(String value, String field, int maxLength) {
        if (value == null || value.isBlank()) {
            throw new DomainException(field + " must not be blank");
        }
        String trimmed = value.trim();
        if (trimmed.length() > maxLength) {
            throw new DomainException(field + " must not exceed " + maxLength + " characters");
        }
        return trimmed;
    }
}
