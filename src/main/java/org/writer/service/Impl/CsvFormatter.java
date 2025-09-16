package org.writer.service.Impl;

import lombok.extern.log4j.Log4j2;
import org.writer.annotation.CsvField;
import org.writer.service.Formatter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс {@code CsvFormatter} реализует интерфейс {@link Formatter} для форматирования списка объектов
 * в CSV-формат. Использует аннотацию {@link org.writer.annotation.CsvField} для определения полей,
 * которые должны быть включены в CSV, их порядка и имен столбцов.
 *
 * <p>
 * Класс поддерживает логирование с использованием Log4j2 для обработки ошибок доступа к полям.
 * </p>
 *
 * @since 1.0
 */
@Log4j2
public class CsvFormatter implements Formatter {

    @Override
    public <T> List<String> formatting(List<T> data) {
        if (data == null || data.isEmpty()) {
            return null;
        }

        Class<?> clazz = data.get(0).getClass();

        List<Field> csvFields = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(CsvField.class))
                .sorted(Comparator.comparing(field -> field.getAnnotation(CsvField.class).order()))
                .toList();

        List<String> dataRows = data.parallelStream()
                .map(obj -> csvFields.stream()
                        .map(field -> {
                            try {
                                field.setAccessible(true);
                                Object value = field.get(obj);
                                return escapeCsv(value != null ? value.toString() : "");
                            } catch (IllegalAccessException e) {
                                log.warn(e.getLocalizedMessage());
                                return "";
                            }
                        })
                        .collect(Collectors.joining(", ")))
                .toList();

        List<String> result = new ArrayList<>(data.size() + 1);
        result.add(generateHeader(csvFields));
        result.addAll(dataRows);

        return result;
    }

    /**
     * Генерирует строку заголовка CSV на основе аннотаций {@link CsvField} для указанных полей.
     *
     * @param fields список полей, аннотированных {@link CsvField}
     * @return строка заголовка CSV
     */
    private String generateHeader(List<Field> fields) {
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append(fields.stream()
                .map(field -> {
                    CsvField annotation = field.getAnnotation(CsvField.class);
                    String columnName = annotation.columnName().isEmpty() ? field.getName() : annotation.columnName();
                    return escapeCsv(columnName);
                })
                .collect(Collectors.joining(", ")));
        return csvBuilder.toString();
    }

    /**
     * Экранирует строку для корректного представления в CSV-формате. Добавляет кавычки вокруг строки,
     * если она содержит запятые, кавычки или символы новой строки, и экранирует существующие кавычки.
     *
     * @param value строка для экранирования
     * @return экранированная строка или пустая строка, если значение {@code null}
     */
    private static String escapeCsv(String value) {
        if (value == null) {
            return "";
        }

        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
