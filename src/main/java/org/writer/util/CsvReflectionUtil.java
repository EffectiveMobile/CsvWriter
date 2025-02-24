package org.writer.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.writer.annotation.CsvFieldOrder;
import org.writer.exception.CsvReflectionException;
import org.writer.exception.handler.CsvErrorHandler;
import org.writer.util.sorter.CsvFieldSorter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class for handling reflection-based operations in CSV processing.
 */
@Slf4j
@UtilityClass
public class CsvReflectionUtil {
    private static final CsvErrorHandler errorHandler = new CsvErrorHandler();

    /**
     * Retrieves all fields of a given class, including inherited fields from superclasses.
     * The fields are sorted according to the {@link CsvFieldOrder} annotation.
     *
     * @param clazz the class from which to retrieve fields.
     * @return a sorted list of fields.
     * @throws CsvReflectionException if an error occurs while retrieving fields.
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();

        while (clazz != null) {
            try {
                Field[] declaredFields = clazz.getDeclaredFields();
                Collections.addAll(fields, declaredFields);
            } catch (Exception ex) {
                throw errorHandler.handleError("Error retrieving fields for class: " + clazz.getName(), ex,
                        CsvReflectionException.class);
            }
            clazz = clazz.getSuperclass();
        }

        CsvFieldSorter.sortFields(fields);

        log.info("Total fields retrieved: {}", fields.size());
        return fields;
    }

    /**
     * Retrieves the value of a specified field from an object using reflection.
     *
     * @param object the object from which to extract the field value.
     * @param field  the field to access.
     * @return the value of the field.
     * @throws CsvReflectionException if the field cannot be accessed.
     */
    public static Object getFieldValue(Object object, Field field) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception ex) {
            errorHandler.handleError("Error accessing field: " + field.getName(), ex, CsvReflectionException.class);
            throw new AssertionError("Unreachable code");
        }
    }
}
