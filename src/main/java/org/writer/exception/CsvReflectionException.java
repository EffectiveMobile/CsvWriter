package org.writer.exception;

/**
 * Thrown when an error occurs during reflection operations (e.g., accessing fields or methods).
 */
public class CsvReflectionException extends CsvWriterException {
    public CsvReflectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
