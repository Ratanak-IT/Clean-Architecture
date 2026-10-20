package kh.edu.istad.platform.customer.domain.exception;

import kh.edu.istad.common.domain.exception.DomainException;

public class DuplicateException extends DomainException {
    public DuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateException(String message) {
        super(message);
    }
}