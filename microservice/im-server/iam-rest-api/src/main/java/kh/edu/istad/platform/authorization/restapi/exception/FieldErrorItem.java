package kh.edu.istad.platform.authorization.restapi.exception;

public record FieldErrorItem(
        String field,
        String message
) {
}

