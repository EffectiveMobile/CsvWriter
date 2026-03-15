package org.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс, предназанченный для записи коллекции объектов в CSV-файл
 */
public class WritableImpl implements Writable {

    /**
     * Метод, для записи коллекуии в файл формата CSV
     *
     * @param data     - коллекция объектов для сохранения
     * @param fileName - имя файла, в который будут сохранены данные
     */
    @Override
    public void writeToFile(List<?> data, String fileName) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("data is null or empty");
        }

        Class<?> clazz = data.get(0).getClass();
        List<Field> fields = fieldsToCsv(clazz);

        try (
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(fileName))
        ) {
            fileWriter.write(
                fields.stream()
                    .map(f -> f.getAnnotation(Csv.class).name())
                    .collect(Collectors.joining(",")));

            fileWriter.newLine();

            for (Object o : data) {
                List<String> values = new ArrayList<>();
                for (Field f : o.getClass().getDeclaredFields()) {
                    if (f.isAnnotationPresent(Csv.class)) {
                        f.setAccessible(true);
                        Object val = f.get(o);
                        if (val != null) {
                            values.add(val.toString());
                        }
                    }
                }
                fileWriter.write(String.join(",", values));
                fileWriter.newLine();
            }

        } catch (IOException | IllegalAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private List<Field> fieldsToCsv(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Csv.class))
            .toList();
    }
}
