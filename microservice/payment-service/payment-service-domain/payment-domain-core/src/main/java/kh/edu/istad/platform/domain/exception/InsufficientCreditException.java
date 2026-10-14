package kh.edu.istad.platform.domain.exception;

public class InsufficientCreditException extends PaymentDomainException {

    public InsufficientCreditException(String message) {
        super(message);
    }
}
