package org.writer.exception;

public class FieldAccessException extends RuntimeException {
    public FieldAccessException(String message, IllegalAccessException e) {
        super(message);
    }
}
