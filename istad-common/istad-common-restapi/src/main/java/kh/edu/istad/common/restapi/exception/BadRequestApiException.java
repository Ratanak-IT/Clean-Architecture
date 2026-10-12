package kh.edu.istad.common.restapi.exception;

import org.springframework.http.HttpStatus;

public class BadRequestApiException extends ApiException{
    public BadRequestApiException(String message){
        super(message, HttpStatus.BAD_REQUEST);
    }
}
