package org.writer.exception;

public class FileWritingException extends RuntimeException {
    public FileWritingException(String fileName) {
        super("Ошибка записи файла " + fileName);
    }
}
