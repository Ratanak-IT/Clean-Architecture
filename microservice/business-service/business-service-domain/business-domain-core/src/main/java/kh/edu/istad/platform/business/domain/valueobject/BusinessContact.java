package kh.edu.istad.platform.business.domain.valueobject;

import kh.edu.istad.platform.business.domain.exception.BusinessDomainException;
import kh.edu.istad.platform.business.domain.validation.Guard;

import java.util.Locale;
import java.util.regex.Pattern;

public record BusinessContact(String email, String phoneNumber) {

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern PHONE = Pattern.compile("^\\+?[0-9]{8,15}$");

    public BusinessContact {
        email = Guard.text(email, "Email", 100).toLowerCase(Locale.ROOT);
        if (!EMAIL.matcher(email).matches()) {
            throw new BusinessDomainException("Email '" + email + "' is not valid");
        }
        phoneNumber = Guard.text(phoneNumber, "Phone number", 20).replaceAll("[\\s-]", "");
        if (!PHONE.matcher(phoneNumber).matches()) {
            throw new BusinessDomainException("Phone number '" + phoneNumber + "' is not valid");
        }
    }
}
