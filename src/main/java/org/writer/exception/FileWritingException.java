package org.writer.exception;

/**
 * Исключение, выбрасываемое при ошибке записи данных в файл.
 */
public class FileWritingException extends RuntimeException {


    public FileWritingException(String message, Throwable cause) {
        super(message, cause);
    }
}
