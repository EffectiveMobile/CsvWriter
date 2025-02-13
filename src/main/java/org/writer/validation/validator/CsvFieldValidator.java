package org.writer.validation.validator;

import org.writer.validation.annotation.ValidCsvField;

import java.lang.reflect.Field;

/**
 * Class for validating fields annotated with {@link ValidCsvField}.
 * Provides methods to check if a field is valid for CSV processing.
 */
public class CsvFieldValidator {

    /**
     * Checks if a field is annotated with {@link ValidCsvField}.
     *
     * @param field the field to validate.
     * @return {@code true} if the field is annotated with {@link ValidCsvField}, otherwise {@code false}.
     */
    public static boolean isValidField(Field field) {
        return field.isAnnotationPresent(ValidCsvField.class);
    }
}
