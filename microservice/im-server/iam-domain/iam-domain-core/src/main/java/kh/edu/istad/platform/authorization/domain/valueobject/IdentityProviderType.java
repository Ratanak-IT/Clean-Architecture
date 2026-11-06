package kh.edu.istad.platform.authorization.domain.valueobject;

import kh.edu.istad.platform.authorization.domain.exception.AuthorizationDomainException;

public enum IdentityProviderType {
    LOCAL,
    GOOGLE,
    GITHUB,
    FACEBOOK,
    TELEGRAM;

    public boolean isOAuth() {
        return this != LOCAL;
    }

    public static IdentityProviderType fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new AuthorizationDomainException("Identity provider must not be blank");
        }
        try {
            return IdentityProviderType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AuthorizationDomainException("Unsupported identity provider: " + value);
        }
    }
}

