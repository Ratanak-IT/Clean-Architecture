package kh.edu.istad.common.restapi.exception;

import java.time.ZonedDateTime;

public record ErrorResponse(
        String code,
        String message,
        ZonedDateTime timestamp
) {
}
