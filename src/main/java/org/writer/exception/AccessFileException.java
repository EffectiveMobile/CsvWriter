package org.writer.exception;

public class AccessFileException extends RuntimeException {
    public AccessFileException(String message) {
        super(message);
    }
    public AccessFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
