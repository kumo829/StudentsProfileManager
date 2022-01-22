package com.javatutoriales.profiles.commons.errors.http;


import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Map;

@Data
@Builder
class ErrorResponse {
    private final ZonedDateTime timestamp = ZonedDateTime.now();
    private int status;
    private String message;
    private Map<String, String> errors;
    private String path;
}
