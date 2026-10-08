package kh.edu.istad.common.restapi.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class RestControllerExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception){
        ErrorResponse errorResponse = new ErrorResponse("INTERNAL_SERVER_ERROR",exception.getMessage(), ZonedDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
