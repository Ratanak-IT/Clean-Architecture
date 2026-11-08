package kh.edu.istad.platform.order.exception;

import kh.edu.istad.platform.order.client.exception.DownStreamServiceException;
import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobleExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(DownStreamServiceException.class)
    public ResponseEntity<?> handle(DownStreamServiceException ex){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "service", ex.getService(),
                        "message", "Down stream service error",
                        "detail", ex.getMessage()
                ));
    }
}
