package org.writer.exception;

/**
 * @author Sidorin Aleksei
 * @version 1.0
 * @since 19.12.2025
 */
public class CsvWriterException extends RuntimeException {
    public CsvWriterException(String message) {
        super(message);
    }

    public CsvWriterException(String message, Throwable cause) {
        super(message, cause);
    }
}
