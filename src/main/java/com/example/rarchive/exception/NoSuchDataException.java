package com.example.rarchive.exception;

public class NoSuchDataException extends RuntimeException {

    public static final String errorMessage = "No such data!\n";

    public NoSuchDataException() {
        super(errorMessage);
    }

    public NoSuchDataException(String message) {
        super(message);
    }
}
