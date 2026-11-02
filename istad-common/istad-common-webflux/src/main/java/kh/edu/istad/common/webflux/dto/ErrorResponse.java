package kh.edu.istad.common.webflux.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;
import java.util.Map;

public record ErrorResponse(
        String message,
        int code,
        Map<String, Object> details
) {
}
