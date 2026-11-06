package kh.edu.istad.platform.authorization.domain.valueobject;

import kh.edu.istad.platform.authorization.domain.exception.AuthorizationDomainException;

public record Address(
        String streetAddress,
        String locality,
        String region,
        String postalCode,
        String country
) {

    private static final int MAX_STREET = 255;
    private static final int MAX_LOCALITY = 100;
    private static final int MAX_REGION = 100;
    private static final int MAX_POSTAL_CODE = 20;
    private static final int MAX_COUNTRY = 100;

    public Address {
        streetAddress = clean(streetAddress, "Street address", MAX_STREET);
        locality = clean(locality, "Locality", MAX_LOCALITY);
        region = clean(region, "Region", MAX_REGION);
        postalCode = clean(postalCode, "Postal code", MAX_POSTAL_CODE);
        country = clean(country, "Country", MAX_COUNTRY);
    }

    private static String clean(String value, String fieldName, int maxLength) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.length() > maxLength) {
            throw new AuthorizationDomainException(fieldName + " length must not exceed " + maxLength + " characters");
        }
        return trimmed;
    }

    public boolean isEmpty() {
        return streetAddress == null && locality == null && region == null
                && postalCode == null && country == null;
    }
}

