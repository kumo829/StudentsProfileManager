package com.javatutoriales.profiles.commons.errors.http;

import com.javatutoriales.profiles.commons.errors.http.exceptions.BadRequestException;
import com.javatutoriales.profiles.commons.errors.http.exceptions.InvalidInputException;
import com.javatutoriales.profiles.commons.errors.http.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.TreeMap;

@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(ServerHttpRequest request,
                                                                               MethodArgumentNotValidException ex) {
        Map<String, String> errors = new TreeMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.put(error.getObjectName(), error.getDefaultMessage());
        }

        return buildErrorResponse(HttpStatus.BAD_REQUEST, request, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(ServerHttpRequest request, BadRequestException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, request, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(ServerHttpRequest request, NotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, request, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleInvalidInputException(ServerHttpRequest request, InvalidInputException ex) {
        return buildErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, request, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknownException(ServerHttpRequest request, Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, request, ex.getMessage());
    }


    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, ServerHttpRequest request, String message, Map<String, String> errors) {
        return ResponseEntity.status(status).body(ErrorResponse.builder()
                .status(status.value())
                .path(getRequestPath(request))
                .errors(errors)
                .message(message)
                .build());
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, ServerHttpRequest request, String message) {
        return buildErrorResponse(status, request, message, null);
    }

    private String getRequestPath(ServerHttpRequest request) {
        return request.getPath().pathWithinApplication().value();
    }
}
