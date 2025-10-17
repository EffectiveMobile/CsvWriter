package org.writer.csv;

import org.writer.Writable;
import org.writer.csv.annotations.Transient;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CsvDocument implements Writable {
    private static final String EXTENSION = ".csv";
    private final Predicate<Field> skipTransient = field -> !field.isAnnotationPresent(Transient.class);

    @Override
    public void writeToFile(List<?> data, String fileName) {
        if (data == null || data.isEmpty())
            throw new RuntimeException("");

        String form = form(data);
        Path path = Path.of(fileName + EXTENSION);
        try {
            Files.writeString(path, form);
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong!!");
        }

    }

    public <T> List<String> columns(T instance) {
        Class<?> metadata = instance.getClass();
        return Arrays.stream(metadata.getDeclaredFields())
                .filter(skipTransient)
                .map(Field::getName)
                .collect(Collectors.toList());
    }

    public <T> List<String> values(T instance) {
        Class<?> metadata = instance.getClass();
        List<String> values = new ArrayList<>();
        Field[] fields = Arrays.stream(metadata.getDeclaredFields())
                .filter(skipTransient)
                .toArray(Field[]::new);
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                values.add(field.get(instance).toString());
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Access denied!");
            }
        }
        return values;
    }

    private String form(List<?> data) {
        StringBuilder sb = new StringBuilder();

        columns(data.get(0)).forEach(i -> sb.append(i).append(", "));
        sb.setLength(sb.length() - 2);
        sb.append('\n');

        for (int i = 0; i < data.size(); i++) {
            values(data.get(0)).forEach(v -> sb.append(v).append(", "));
            sb.setLength(sb.length() - 2);
            sb.append('\n');
        }
        return sb.toString();
    }
}
