package org.writer.exception;

/**
 * Исключение, выбрасываемое при попытке обработки пустых данных.
 */
public class EmptyDataException extends RuntimeException {

    public EmptyDataException(String message) {
        super(message);
    }


}
