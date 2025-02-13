package org.writer.exception;

/**
 * Thrown when there is an issue with the CSV data (e.g., invalid format or missing fields).
 */
public class CsvDataException extends RuntimeException {
    public CsvDataException(String message) {
        super(message);
    }
}
