package org.writer.exception;

/**
 * Исключение, выбрасываемое при попытке сериализовать объект в CSV,
 * если его класс не аннотирован {@link org.writer.annotation.CsvRecord}.
 */
public class CsvRecordAnnotationMissingException extends IllegalArgumentException {

    public CsvRecordAnnotationMissingException(String message) {
        super(message);
    }

    public CsvRecordAnnotationMissingException(Class<?> targetClass) {
        super(String.format("Класс '%s' не аннотирован @CsvRecord и не может быть обработан как CSV запись.",
                targetClass.getName()));
    }
}
