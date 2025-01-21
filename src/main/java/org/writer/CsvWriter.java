package org.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class CsvWriter implements Writable {

    private static final String SEPARATOR = ",";
    private static final String DASH = "-";

    @Override
    public void writeToFile(List<?> data, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            boolean isHeaderLineExist = false;
            String headersLine;
            for (Object object : data) {
                Class<?> clazz = object.getClass();
                if (clazz.isAnnotationPresent(CSV.class)) {
                    if (!isHeaderLineExist) {
                        headersLine = convertHeadersToCsv(object);
                        isHeaderLineExist = true;
                        writer.write(headersLine + "\n");
                    }
                    String csvLine = convertObjectToCsv(object);
                    writer.write(csvLine + "\n");
                } else {
                    throw new IOException("Passed class is not annotated by @CSV: " + clazz);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to write CSV file: " + e.getMessage());
        }
    }

    private String convertObjectToCsv(Object object) {
        Class<?> clazz = object.getClass();
        StringBuilder line = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(object);
            } catch (IllegalAccessException e) {
                System.err.println("Failed to access to field value: " + e.getMessage());
            }

            if (value != null && isList(field)) {
                List<?> list = (List<?>) value;

                String result = list.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(" ", "[", "]"));

                if (!line.isEmpty()) {
                    line.append(SEPARATOR);
                }
                line.append(result);
                continue;
            }

            if (!line.isEmpty()) {
                line.append(SEPARATOR);
            }
            if (value != null) {
                line.append(value);
            } else {
                line.append(DASH);
            }
        }

        return line.toString();
    }

    private String convertHeadersToCsv(Object object) {
        Class<?> clazz = object.getClass();
        StringBuilder line = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            line.append(fields[i].getName());
            if (i < fields.length - 1) {
                line.append(SEPARATOR);
            }
        }

        return line.toString();
    }
    private boolean isList(Field field) {
        if (List.class.isAssignableFrom(field.getType())) {
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
                ParameterizedType pType = (ParameterizedType) genericType;

                return pType.getRawType() == List.class;
            }
        }
        return false;
    }
}
