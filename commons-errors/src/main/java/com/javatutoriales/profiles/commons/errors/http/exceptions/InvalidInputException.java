package com.javatutoriales.profiles.commons.errors.http.exceptions;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException() {}

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInputException(Throwable cause) {
        super(cause);
    }
}