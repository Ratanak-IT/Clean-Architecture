package kh.edu.istad.platform.authorization.domain.valueobject;

import kh.edu.istad.platform.authorization.domain.exception.AuthorizationDomainException;

import java.util.regex.Pattern;

public record PhoneNumber(String value) {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[1-9]\\d{6,14}$");

    public PhoneNumber {
        if (value == null || value.isBlank()) {
            throw new AuthorizationDomainException("Phone number must not be blank");
        }

        value = value.trim().replaceAll("[\\s\\-\\(\\)]", "");

        if (!PHONE_PATTERN.matcher(value).matches()) {
            throw new AuthorizationDomainException("Invalid phone number format (must follow E.164 standard): " + value);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}

