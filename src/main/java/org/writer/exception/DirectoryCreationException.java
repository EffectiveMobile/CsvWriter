package org.writer.exception;

/**
 * Исключение, выбрасываемое при ошибке создания директории.
 */
public class DirectoryCreationException extends RuntimeException {

    public DirectoryCreationException(String message) {
        super(message);
    }
}