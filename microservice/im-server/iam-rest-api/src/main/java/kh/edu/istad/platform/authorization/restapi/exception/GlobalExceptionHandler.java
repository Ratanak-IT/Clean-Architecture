package kh.edu.istad.platform.authorization.restapi.exception;

import kh.edu.istad.common.domain.exception.DomainException;
import kh.edu.istad.common.domain.exception.ResourceAlreadyExistsException;
import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.platform.authorization.domain.exception.AuthorizationDomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "kh.edu.istad.platform.authorization.restapi")
public class GlobalExceptionHandler {

    private static final String BASE_PROBLEM_URL = "https://iam.istad.co/problems/";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex) {
        log.warn("Validation error: {}", ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setType(URI.create(BASE_PROBLEM_URL + "validation-error"));
        problem.setTitle("Validation failed");

        List<FieldErrorItem> errors = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            String defaultMessage = fieldError.getDefaultMessage() != null
                    ? fieldError.getDefaultMessage()
                    : "Invalid value";
            errors.add(new FieldErrorItem(fieldError.getField(), defaultMessage));
        }

        problem.setProperty("errors", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setType(URI.create(BASE_PROBLEM_URL + "resource-not-found"));
        problem.setTitle("Resource not found");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleAlreadyExists(ResourceAlreadyExistsException ex) {
        log.warn("Resource already exists: {}", ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setType(URI.create(BASE_PROBLEM_URL + "resource-already-exists"));
        problem.setTitle("Resource already exists");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Illegal argument: {}", ex.getMessage());

        boolean isAlreadyExists = ex.getMessage() != null && ex.getMessage().toLowerCase().contains("already exists");
        HttpStatus status = isAlreadyExists ? HttpStatus.CONFLICT : HttpStatus.BAD_REQUEST;
        String type = isAlreadyExists ? "resource-already-exists" : "bad-request";
        String title = isAlreadyExists ? "Resource already exists" : "Invalid request parameter";

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        problem.setType(URI.create(BASE_PROBLEM_URL + type));
        problem.setTitle(title);

        return ResponseEntity.status(status).body(problem);
    }

    @ExceptionHandler({AuthorizationDomainException.class, DomainException.class})
    public ResponseEntity<ProblemDetail> handleDomainException(RuntimeException ex) {
        log.warn("Domain exception: {}", ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setType(URI.create(BASE_PROBLEM_URL + "domain-error"));
        problem.setTitle("Domain rule violation");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleUnreadable(HttpMessageNotReadableException ex) {
        log.warn("Malformed HTTP request body: {}", ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Request body is missing or has invalid JSON format."
        );
        problem.setType(URI.create(BASE_PROBLEM_URL + "malformed-request"));
        problem.setTitle("Malformed request body");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String detail = String.format("Parameter '%s' with value '%s' is invalid.", ex.getName(), ex.getValue());
        log.warn("Type mismatch: {}", detail);

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
        problem.setType(URI.create(BASE_PROBLEM_URL + "type-mismatch"));
        problem.setTitle("Invalid parameter type");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneralException(Exception ex) {
        log.error("Unhandled internal server exception: {}", ex.getMessage(), ex);

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected internal server error occurred."
        );
        problem.setType(URI.create(BASE_PROBLEM_URL + "internal-error"));
        problem.setTitle("Internal server error");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }
}

