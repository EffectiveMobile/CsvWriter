package org.writer.service;

import org.writer.annotation.CsvField;
import org.writer.annotation.CsvWritable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public class CsvWriter implements Writable {

    @Override
    public void writeToFile(List<?> data, String fileName) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Data list is empty or null");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Получаем первый объект из списка, чтобы определить заголовки
            Object firstObject = data.get(0);
            Class<?> clazz = firstObject.getClass();

            // Проверяем, что класс помечен аннотацией @CsvSerializable
            if (!clazz.isAnnotationPresent(CsvWritable.class)) {
                throw new IllegalArgumentException("Class " + clazz.getSimpleName() + " is not annotated with @CsvWritable");
            }

            // Записываем заголовок CSV
            writeHeader(writer, clazz);

            // Записываем данные
            for (Object obj : data) {
                writeRow(writer, obj, clazz);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to write to file: " + fileName, e);
        }
    }

    private void writeHeader(BufferedWriter writer, Class<?> clazz) throws IOException {
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder header = new StringBuilder();

        for (Field field : fields) {
            if (field.isAnnotationPresent(CsvField.class)) {
                CsvField csvField = field.getAnnotation(CsvField.class);
                // Если header не указан, используем имя поля
                String columnName = csvField.header().isEmpty() ? field.getName() : csvField.header();
                header.append(columnName).append(",");
            }
        }

        // Убираем последнюю запятую
        if (header.length() > 0) {
            header.deleteCharAt(header.length() - 1);
        }

        writer.write(header.toString());
        writer.newLine();
    }

    private void writeRow(BufferedWriter writer, Object obj, Class<?> clazz) throws IOException {
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder row = new StringBuilder();

        for (Field field : fields) {
            if (field.isAnnotationPresent(CsvField.class)) {
                field.setAccessible(true); // Разрешаем доступ к приватным полям
                try {
                    Object value = field.get(obj);
                    if (value instanceof Collection) {
                        // Если значение — это список, объединяем элементы в строку
                        List<?> list = (List<?>) value;
                        String joinedValues = String.join(";", list.stream()
                                .map(Object::toString)
                                .toArray(String[]::new));
                        row.append(joinedValues).append(",");
                    }
                    else {
                        // Для обычных полей
                        row.append(value != null ? value.toString() : "").append(",");
                    }
                }
                catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to access field: " + field.getName(), e);
                }
            }
        }

        // Убираем последнюю запятую
        if (row.length() > 0) {
            row.deleteCharAt(row.length() - 1);
        }

        writer.write(row.toString());
        writer.newLine();
    }
}
