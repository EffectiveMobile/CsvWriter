package org.writer.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.writer.exception.CsvFileWriteException;
import org.writer.exception.CsvReflectionException;
import org.writer.exception.CsvUnexpectedException;

/**
 * Handles errors that occur during CSV writing operations.
 * Provides a centralized way to log errors and map them to specific exceptions.
 */
@Slf4j
public class CsvErrorHandler {

    /**
     * Handles errors during CSV writing.
     *
     * @param message the error message.
     * @param ex      the exception that occurred.
     * @param <T>     the type of exception to throw.
     * @throws T the exception to be thrown.
     */
    public <T extends Exception> void handleError(String message, Exception ex, Class<T> exceptionType) throws T {
        log.error(message, ex);

        Exception mappedException = switch (exceptionType.getSimpleName()) {
            case "CsvFileWriteException" -> new CsvFileWriteException(message, ex);
            case "CsvReflectionException" -> new CsvReflectionException(message, ex);
            case "CsvUnexpectedException" -> new CsvUnexpectedException(message, ex);
            default -> new RuntimeException(message, ex);
        };

        throw exceptionType.cast(mappedException);
    }
}
