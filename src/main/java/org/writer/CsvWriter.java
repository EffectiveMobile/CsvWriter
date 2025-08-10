package org.writer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CSV file writer implementation.
 * Uses reflection to read object fields and export them to CSV format.
 */
public class CsvWriter implements Writable {

    @Override
    public void writeToFile(List<?> data, String fileName) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Data list is empty or null.");
        }

        try (Writer writer = new FileWriter(fileName)) {
            // Get object class and fields
            Class<?> dataClass = data.get(0).getClass();
            List<Field> fields = Arrays.stream(dataClass.getDeclaredFields()).toList();

            // Write CSV header
            List<String> headers = new ArrayList<>();
            for (Field field : fields) {
                CsvField annotation = field.getAnnotation(CsvField.class);
                headers.add(annotation != null && !annotation.name().isEmpty()
                        ? annotation.name()
                        : field.getName());
            }
            writer.write(String.join(",", headers) + "\n");

            // Write data rows
            for (Object obj : data) {
                List<String> values = new ArrayList<>();
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = field.get(obj);

                    if (value instanceof List<?> listVal) {
                        // Join list elements with semicolon
                        values.add(String.join(";", listVal.stream().map(Object::toString).toList()));
                    } else {
                        values.add(value != null ? value.toString() : "");
                    }
                }
                writer.write(String.join(",", values) + "\n");
            }

        } catch (IOException | IllegalAccessException e) {
            throw new RuntimeException("Error writing to CSV", e);
        }
    }
}
