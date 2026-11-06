package kh.edu.istad.platform.authorization.domain.valueobject;

import kh.edu.istad.platform.authorization.domain.exception.AuthorizationDomainException;

import java.util.regex.Pattern;

public record Username(String value) {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 30;
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){1,28}[a-zA-Z0-9]$");

    public Username {
        if (value == null || value.isBlank()) {
            throw new AuthorizationDomainException("Username must not be blank");
        }

        value = value.trim().toLowerCase();

        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new AuthorizationDomainException("Username length must be between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters");
        }

        if (!USERNAME_PATTERN.matcher(value).matches()) {
            throw new AuthorizationDomainException("Username contains invalid characters or pattern: " + value);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}

