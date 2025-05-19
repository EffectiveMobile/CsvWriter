package org.writer.exception;

import org.writer.annotation.csv.CsvRecord;

/**
 * Исключение, выбрасываемое при попытке сериализовать объект в CSV,
 * если его класс не аннотирован {@link CsvRecord}.
 */
public class FormatRecordAnnotationMissingException extends IllegalArgumentException {

    public FormatRecordAnnotationMissingException(String message) {
        super(message);
    }

    public FormatRecordAnnotationMissingException(Class<?> targetClass) {
        super(String.format("Класс '%s' не аннотирован @CsvRecord и не может быть обработан как CSV запись.",
                targetClass.getName()));
    }
}
