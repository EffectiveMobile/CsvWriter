package org.writer.writer;

import org.writer.exception.CsvWriterException;
import org.writer.Writable;
import org.writer.annotation.CSVField;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Имплиментация генератора csv файлов
 * на основе интерфейса Writable
 *
 * @author Sidorin Aleksei
 * @version 1.0
 * @since 16.10.2025
 */
public class CSVWriter implements Writable {

    private static final String CSV_DELIMITER = ",";
    private static final String CSV_QUOTE = "\"";
    private static final String NEW_LINE = "\n";

    @Override
    public void writeToFile(List<?> data, String fileName) {
        if (Objects.isNull(data) || data.isEmpty()) {
            throw new IllegalArgumentException("List of data cant be null or empty");
        }
        if (Objects.isNull(fileName)) {
            throw new IllegalArgumentException("File name can't be null");
        }

        File outputDir = new File("outputs");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        File outputFile = new File(outputDir, fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            Class<?> objectClass = data.get(0).getClass();
            List<Field> fields = getAnnotatedFields(objectClass);

            writer.write(generateHeader(fields));
            writer.write(NEW_LINE);

            for (var object : data) {
                writer.write(generateDataRow(object, fields));
                writer.write(NEW_LINE);
            }
        } catch (IOException e) {
            throw new CsvWriterException("You have uncorrected path way", e);
        }
    }

    /**
     * Экранирует кавычки для CSV
     * @param value значение для экранирования
     * @return экранированная строка
     */
    private String escapeCsvValue(Object value) {
        if (value == null) {
            return "";
        }

        String stringValue = value.toString();
        if (stringValue.contains(CSV_DELIMITER) ||
            stringValue.contains(CSV_QUOTE) ||
            stringValue.trim().isEmpty()
        ) {
            stringValue = stringValue.replace(CSV_QUOTE, CSV_QUOTE + CSV_QUOTE);
            return CSV_QUOTE + stringValue + CSV_QUOTE;
        }
        return stringValue;
    }

    /**
     * Получает аннотированные поля класса в правильном порядке
     * @param clazz класс для анализа
     * @return список полей с аннотацией CSVField, отсортированный по order
     */
    private List<Field> getAnnotatedFields(Class<?> clazz) {
        List<Field> annotatedFields = new ArrayList<>();

        Class<?> currentClass = clazz;
        while (currentClass != null) {
            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(CSVField.class)) {
                    field.setAccessible(true);
                    annotatedFields.add(field);
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        return annotatedFields.stream()
                .sorted(Comparator.comparingInt(field ->
                        field.getAnnotation(CSVField.class).order()))
                .toList();
    }

    /**
     * Форматирует значение поля согласно аннотации
     * @param field поле
     * @param value значение
     * @return отформатированное значение
     */
    private String formatFieldValue(Field field, Object value) {
        if (value == null) {
            return "";
        }

        CSVField annotation = field.getAnnotation(CSVField.class);
        String format = annotation.format();

        if (!format.isEmpty() && value instanceof LocalDateTime localDateTime) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return localDateTime.format(formatter);
        }

        return value.toString();
    }

    /**
     * Генерирует заголовок CSV на основе аннотаций полей
     * @param fields список полей
     * @return строка заголовка
     */
    private String generateHeader(List<Field> fields) {
        return fields.stream()
                .map(field -> {
                    CSVField annotation = field.getAnnotation(CSVField.class);
                    String columnName = annotation.name().isEmpty() ?
                            field.getName() : annotation.name();
                    return escapeCsvValue(columnName);
                })
                .collect(Collectors.joining(CSV_DELIMITER));
    }

    /**
     * Генерирует строку данных для объекта
     * @param object объект
     * @param fields список полей
     * @return строка данных
     */
    private String generateDataRow(Object object, List<Field> fields) {
        return fields.stream()
                .map(field -> {
                    try {
                        Object value = field.get(object);
                        String formattedValue = formatFieldValue(field, value);
                        return escapeCsvValue(formattedValue);
                    } catch (IllegalAccessException e) {
                        throw new CsvWriterException("Cant generate data csv",e);
                    }
                })
                .collect(Collectors.joining(CSV_DELIMITER));
    }

}
