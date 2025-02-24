package org.writer.exception.handler;

import lombok.extern.slf4j.Slf4j;

/**
 * Handles CSV processing errors by logging and throwing a specified exception.
 */
@Slf4j
public class CsvErrorHandler {

    /**
     * Logs an error and throws the specified exception type.
     *
     * @param message       Error message.
     * @param ex            Original exception.
     * @param exceptionType Exception class to be thrown.
     * @param <T>           Exception type.
     * @throws T If instantiation fails, a {@link RuntimeException} is thrown.
     */
    public <T extends Exception> T handleError(String message, Exception ex, Class<T> exceptionType) throws T {
        log.error(message, ex);

        try {
            throw exceptionType
                    .getConstructor(String.class, Throwable.class)
                    .newInstance(message, ex);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to create exception of type: " + exceptionType.getName(), e);
        }
    }
}