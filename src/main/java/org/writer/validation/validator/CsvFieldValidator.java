package org.writer.validation.validator;

import org.writer.validation.annotation.ValidCsvField;

import java.lang.reflect.Field;

/**
 * Class for validating and retrieving header names of fields annotated with {@link ValidCsvField}.
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

    /**
     * Retrieves the header name for a given field.
     * Uses the custom header name from {@link ValidCsvField} if provided, otherwise defaults to the field name.
     *
     * @param field the field to retrieve the header for.
     * @return the header name for the CSV file.
     */
    public static String getFieldHeader(Field field) {
        var annotation = field.getAnnotation(ValidCsvField.class);

        return (annotation != null && !annotation.headerName().isBlank())
                ?
                annotation.headerName()
                :
                field.getName();
    }
}