package org.writer.exceptions;

/**
 * Исключение выбрасываемое в случае непредвиденной ошибки по время маппинга объектов в CSV-формат
 */
public class CsvMappingException extends RuntimeException {

    public CsvMappingException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public CsvMappingException(String message) {
        super(message);
    }
}
