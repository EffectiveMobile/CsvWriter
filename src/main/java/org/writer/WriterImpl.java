package org.writer;

import org.writer.annotation.Column;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WriterImpl implements Writable {
    @Override
    public void writeToFile(List<?> data, String fileName) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException
                    ("Data mustn't be empty");
        }

        Class<?> _class = data.get(0).getClass();

        List<Field> annotatedFields = getAnnotatedFields(_class);

        if (annotatedFields.isEmpty()) {
            throw new IllegalArgumentException
                    ("There is no annotated fields in class " + _class.getSimpleName());
        }

        annotatedFields.sort(Comparator.comparingInt(f -> f.getAnnotation(Column.class).position()));

        List<String> columnNames = annotatedFields
                .stream()
                .map(f -> {
                    String name = f.getAnnotation(Column.class).columnName();
                    return name == null || name.isBlank()
                            ? f.getName()
                            : name;
                })
                .toList();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

            writer.write(buildCsvLine(columnNames));
            writer.newLine();

            for (Object obj : data) {
                List<String> values = new ArrayList<>(annotatedFields.size());
                for (Field field : annotatedFields) {
                    field.setAccessible(true);
                    Object rawValue = field.get(obj);
                    String str = (rawValue == null) ? "" : rawValue.toString();
                    values.add(escapeCsv(str));
                }
                writer.write(buildCsvLine(values));
                writer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to write CSV in file " + fileName, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to get an access with reflection", e);
        }
    }

    private List<Field> getAnnotatedFields(Class<?> _class) {
        Field[] allFields = _class.getDeclaredFields();
        List<Field> annotatedFields = new ArrayList<>();
        for (Field field : allFields) {
            if (field.isAnnotationPresent(Column.class)) {
                annotatedFields.add(field);
            }
        }

        return annotatedFields;
    }

    private String buildCsvLine(List<String> values) {
        return String.join(",", values);
    }

    private String escapeCsv(String value) {
        boolean needQuotes = value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r");
        if (needQuotes) {
            String doubled = value.replace("\"", "\"\"");
            return "\"" + doubled + "\"";
        } else {
            return value;
        }
    }
}
