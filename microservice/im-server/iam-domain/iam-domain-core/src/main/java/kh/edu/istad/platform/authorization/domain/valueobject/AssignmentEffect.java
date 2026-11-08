package kh.edu.istad.platform.authorization.domain.valueobject;

import kh.edu.istad.platform.authorization.domain.exception.AuthorizationDomainException;

public enum AssignmentEffect {
    GRANT,
    DENY;

    public boolean isGrant() {
        return this == GRANT;
    }

    public boolean isDeny() {
        return this == DENY;
    }

    public static AssignmentEffect fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new AuthorizationDomainException("Assignment effect must not be blank");
        }
        try {
            return AssignmentEffect.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AuthorizationDomainException("Invalid assignment effect: " + value + ". Must be GRANT or DENY.");
        }
    }
}

