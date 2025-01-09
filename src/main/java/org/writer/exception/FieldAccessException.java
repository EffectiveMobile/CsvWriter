package org.writer.exception;

/**
 * Исключение, выбрасываемое при ошибке доступа к полю объекта.
 */
public class FieldAccessException extends RuntimeException {


    public FieldAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
