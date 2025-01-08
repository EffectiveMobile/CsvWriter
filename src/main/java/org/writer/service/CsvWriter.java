package org.writer.service;

import org.writer.annotation.CsvField;
import org.writer.annotation.CsvWritable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

/**
 * Реализация интерфейса {@link Writable} для записи данных в CSV-файл.
 * Использует Reflection и аннотации для определения структуры CSV.
 *
 * @author Мельников Никита
 */
public class CsvWriter implements Writable {

    private static final String filePrefix = ".csv";

    /**
     * Записывает список объектов в CSV-файл.
     *
     * @param data     список объектов для записи.
     * @param fileName имя файла, в который будут записаны данные.
     * @throws IllegalArgumentException если список данных пуст или объекты не аннотированы {@link CsvWritable}.
     * @throws RuntimeException         если произошла ошибка при записи в файл.
     */
    @Override
    public void writeToFile(List<?> data, String fileName) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Data list is empty or null");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName + filePrefix))) {
            Object firstObject = data.get(0);
            Class<?> clazz = firstObject.getClass();

            if (!clazz.isAnnotationPresent(CsvWritable.class)) {
                throw new IllegalArgumentException("Class " + clazz.getSimpleName() + " is not annotated with @CsvWritable");
            }

            writeHeader(writer, clazz);
            for (Object obj : data) {
                writeRow(writer, obj, clazz);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to write to file: " + fileName + filePrefix, e);
        }
    }

    /**
     * Записывает заголовок CSV на основе аннотированных полей класса.
     *
     * @param writer BufferedWriter для записи в файл.
     * @param clazz  класс объекта, поля которого используются для создания заголовка.
     * @throws IOException если произошла ошибка при записи в файл.
     */
    private void writeHeader(BufferedWriter writer, Class<?> clazz) throws IOException {
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder header = new StringBuilder();

        for (Field field : fields) {
            if (field.isAnnotationPresent(CsvField.class)) {
                CsvField csvField = field.getAnnotation(CsvField.class);
                String columnName = csvField.header().isEmpty() ? field.getName() : csvField.header();
                header.append(columnName).append(",");
            }
        }

        if (header.length() > 0) {
            header.deleteCharAt(header.length() - 1);
        }
        writer.write(header.toString());
        writer.newLine();
    }

    /**
     * Записывает строку данных в CSV на основе аннотированных полей объекта.
     *
     * @param writer BufferedWriter для записи в файл.
     * @param obj    объект, данные которого нужно записать.
     * @param clazz  класс объекта.
     * @throws IOException если произошла ошибка при записи в файл.
     */
    private void writeRow(BufferedWriter writer, Object obj, Class<?> clazz) throws IOException {
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder row = new StringBuilder();

        for (Field field : fields) {
            if (field.isAnnotationPresent(CsvField.class)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(obj);
                    if (value instanceof Collection<?> collection) {
                        String joinedValues = String.join(";", collection.stream()
                                .map(Object::toString)
                                .toArray(String[]::new));
                        row.append(joinedValues).append(",");
                    }
                    else {
                        row.append(value != null ? value.toString() : "").append(",");
                    }
                }
                catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to access field: " + field.getName(), e);
                }
            }
        }

        if (row.length() > 0) {
            row.deleteCharAt(row.length() - 1);
        }
        writer.write(row.toString());
        writer.newLine();
    }
}
