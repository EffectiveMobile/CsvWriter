package org.writer.exception;

/**
 * Thrown when an error occurs while writing to a CSV file.
 */
public class CsvFileWriteException extends CsvWriterException {
    public CsvFileWriteException(String message, Throwable cause) {
        super(message, cause);
    }
}
