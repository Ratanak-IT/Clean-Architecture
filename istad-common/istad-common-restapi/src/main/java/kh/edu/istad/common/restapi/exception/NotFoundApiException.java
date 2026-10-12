package kh.edu.istad.common.restapi.exception;

import org.springframework.http.HttpStatus;

public class NotFoundApiException extends ApiException{
    public NotFoundApiException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
