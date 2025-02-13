package org.writer.exception;

/**
 * Thrown when an unexpected error occurs during CSV processing.
 */
public class CsvUnexpectedException extends CsvWriterException {
    public CsvUnexpectedException(String message, Throwable cause) {
        super(message, cause);
    }
}
