package kh.edu.istad.platform.business.restapi.exception;

import kh.edu.istad.common.domain.exception.DomainException;
import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ReactiveExceptionAdvice {

    private static final Logger log = LoggerFactory.getLogger(ReactiveExceptionAdvice.class);
    private static final ZoneId UTC = ZoneId.of("UTC");

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorResponses> handleValidation(WebExchangeBindException ex) {
        String errors = ex.getFieldErrors().stream()
                .map(field -> field.getField() + ": " + field.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("Validation failed: {}", errors);
        return build(HttpStatus.BAD_REQUEST.name(), errors, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<ErrorResponses> handleInput(ServerWebInputException ex) {
        log.warn("Unreadable request: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST.name(), "Malformed or unreadable request",
                HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponses> handleNotFound(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND.name(), ex.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponses> handleDomain(DomainException ex) {
        log.warn("Business rule violated: {}", ex.getMessage());
        return build("BUSINESS_RULE_VIOLATION", ex.getMessage(), 422);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponses> handleGeneral(Exception ex) {
        if (ex instanceof ErrorResponse framework) {
            int status = framework.getStatusCode().value();
            return build(HttpStatus.valueOf(status).name(), HttpStatus.valueOf(status).getReasonPhrase(), status);
        }
        log.error("Internal server error: ", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR.name(), "Internal server error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private ResponseEntity<ErrorResponses> build(String code, String message, int status) {
        return ResponseEntity.status(status).body(new ErrorResponses(code, message, ZonedDateTime.now(UTC)));
    }
}
