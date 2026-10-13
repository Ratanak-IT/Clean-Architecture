package kh.edu.istad.platform.business.restapi.exception;

import java.time.ZonedDateTime;

public record ErrorResponse(String code, String message, ZonedDateTime timestamp) {
}
