package org.writer.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.writer.exception.CsvReflectionException;
import org.writer.exception.handler.CsvErrorHandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@UtilityClass
public class CsvReflectionUtil {
    private static final CsvErrorHandler errorHandler = new CsvErrorHandler();

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
            throw errorHandler.handleError("Error accessing field: " + field.getName(), ex,
                    CsvReflectionException.class);
        }
    }
}
