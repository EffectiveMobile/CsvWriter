package org.writer.exception;

/**
 * Base exception for CSV writing operations.
 * Extends {@link RuntimeException} and provides a common structure for CSV-related exceptions.
 */
public abstract class CsvWriterException extends RuntimeException {
    public CsvWriterException(String message, Throwable cause) {
        super(message, cause);
    }
}
