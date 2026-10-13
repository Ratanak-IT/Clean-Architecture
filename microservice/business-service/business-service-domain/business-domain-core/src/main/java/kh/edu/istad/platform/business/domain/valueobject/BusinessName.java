package kh.edu.istad.platform.business.domain.valueobject;

import kh.edu.istad.platform.business.domain.validation.Guard;

public record BusinessName(String legalName, String displayName) {

    public static final int MAX_LENGTH = 100;

    public BusinessName {
        legalName = Guard.text(legalName, "Legal name", MAX_LENGTH);
        displayName = Guard.text(displayName, "Display name", MAX_LENGTH);
    }
}
