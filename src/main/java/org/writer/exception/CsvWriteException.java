package org.writer.exception;

/**
 * Исключение, которое выбрасывается при ошибках записи CSV-файла.
 */
public class CsvWriteException extends RuntimeException {

    public CsvWriteException(String message) {
        super(message);
    }
}
