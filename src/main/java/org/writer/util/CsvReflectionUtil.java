package org.writer.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.writer.annotation.CsvFieldOrder;
import org.writer.exception.CsvReflectionException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Utility class for reflection-based operations on CSV models.
 * Provides methods to retrieve and process fields of a class, including sorting by {@link CsvFieldOrder}.
 */
@Slf4j
@UtilityClass
public class CsvReflectionUtil {

    /**
     * Retrieves all fields of a class, including those from its superclasses.
     * Fields are sorted based on the {@link CsvFieldOrder} annotation.
     *
     * @param clazz the class to retrieve fields from.
     * @return a sorted list of fields.
     * @throws CsvReflectionException if a security violation or unexpected error occurs.
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();

        while (clazz != null) {
            try {
                Field[] declaredFields = clazz.getDeclaredFields();
                Collections.addAll(fields, declaredFields);
            } catch (SecurityException ex) {
                log.error("Security violation while accessing fields of class: {}", clazz.getName(), ex);
                throw new CsvReflectionException(
                        "Security violation while accessing fields of class: " + clazz.getName(), ex);
            } catch (Exception ex) {
                log.error("Unexpected error while retrieving fields for class: {}", clazz.getName(), ex);
                throw new CsvReflectionException(
                        "Unexpected error while retrieving fields for class: " + clazz.getName(), ex);
            }
            clazz = clazz.getSuperclass();
        }

        fields.sort(Comparator.comparingInt(field -> {
            CsvFieldOrder order = field.getAnnotation(CsvFieldOrder.class);
            return order != null ?
                    order.value() :
                    Integer.MAX_VALUE;
        }));

        log.info("Total fields retrieved: {}", fields.size());
        return fields;
    }

    /**
     * Retrieves the value of a field from an object using reflection.
     *
     * @param object the object to retrieve the field value from.
     * @param field  the field to access.
     * @return the value of the field.
     * @throws CsvReflectionException if the field cannot be accessed or an unexpected error occurs.
     */
    public static Object getFieldValue(Object object, Field field) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException ex) {
            log.error("Failed to access field: {}", field.getName(), ex);
            throw new CsvReflectionException("Failed to access field: " + field.getName(), ex);
        } catch (IllegalArgumentException ex) {
            log.error("Invalid argument provided for field: {}", field.getName(), ex);
            throw new CsvReflectionException("Invalid argument provided for field: " + field.getName(), ex);
        } catch (SecurityException ex) {
            log.error("Security violation while accessing field: {}", field.getName(), ex);
            throw new CsvReflectionException("Security violation while accessing field: " + field.getName(), ex);
        } catch (Exception ex) {
            log.error("Unexpected error while accessing field: {}", field.getName(), ex);
            throw new CsvReflectionException("Unexpected error while accessing field: " + field.getName(), ex);
        }
    }
}
