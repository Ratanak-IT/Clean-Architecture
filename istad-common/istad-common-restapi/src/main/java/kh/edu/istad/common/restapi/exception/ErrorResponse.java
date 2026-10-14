package kh.edu.istad.common.restapi.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String code,
        String message,
        ZonedDateTime timestamp,
        Map<String, String> errors
) {
    public ErrorResponse(String code, String message, ZonedDateTime timestamp) {
        this(code, message, timestamp, null);
    }
}