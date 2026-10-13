package kh.edu.istad.platform.business.domain.validation;

import kh.edu.istad.platform.business.domain.exception.BusinessDomainException;

public final class Guard {

    private Guard() {
    }

    public static <T> T notNull(T value, String name) {
        if (value == null) {
            throw new BusinessDomainException(name + " is required");
        }
        return value;
    }

    public static String text(String value, String name, int maxLength) {
        if (value == null || value.isBlank()) {
            throw new BusinessDomainException(name + " must not be blank");
        }
        String trimmed = value.trim();
        if (trimmed.length() > maxLength) {
            throw new BusinessDomainException(name + " must not exceed " + maxLength + " characters");
        }
        return trimmed;
    }
}
