package com.example.rarchive.exception;

public class UnauthorizedAccessException extends RuntimeException {

    public static final String errorMessage = "Unauthorized access to other people's data!\n";

    public UnauthorizedAccessException() {
        super(errorMessage);
    }

    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
