package kh.edu.istad.platform.authorization.domain.valueobject;

import kh.edu.istad.platform.authorization.domain.exception.AuthorizationDomainException;

import java.util.regex.Pattern;

public record Email(String value) {

    private static final int MAX_LENGTH = 254;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    public Email {
        if (value == null || value.isBlank()) {
            throw new AuthorizationDomainException("Email must not be blank");
        }

        value = value.trim().toLowerCase();

        if (value.length() > MAX_LENGTH) {
            throw new AuthorizationDomainException("Email length must not exceed " + MAX_LENGTH + " characters");
        }

        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new AuthorizationDomainException("Invalid email format: " + value);
        }
    }

    public String localPart() {
        return value.substring(0, value.indexOf('@'));
    }

    public String domain() {
        return value.substring(value.indexOf('@') + 1);
    }

    @Override
    public String toString() {
        return value;
    }
}

