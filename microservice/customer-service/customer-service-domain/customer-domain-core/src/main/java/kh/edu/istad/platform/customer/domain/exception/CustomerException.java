package kh.edu.istad.platform.customer.domain.exception;

import kh.edu.istad.common.domain.exception.DomainException;

public class CustomerException extends DomainException {

    public CustomerException(String message) {
        super(message);
    }

    public CustomerException(String message, Throwable cause) {
        super(message, cause);
    }
}
