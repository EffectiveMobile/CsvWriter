package org.writer;

import org.writer.annotations.CsvField;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс для преобразования объектов в CSV формат.
 * Использует Reflection для анализа аннотаций @CsvField.
 */
public class CsvSerializer {

    private static final String CSV_COLUMN_SEPARATOR = ";";
    private static final String CSV_LINE_SEPARATOR = "\n";
    private static final String CSV_LIST_VALUE_SEPARATOR = ",";
    private static final String CSV_QUOTES_ESCAPE_SYMBOL = "\"";

    /**
     * Сериализует список объектов в CSV строку с заголовком
     *
     * @param objects список объектов для сериализации
     * @return строка в формате CSV
     * @throws IllegalArgumentException если список пуст
     */
    public String serialize(List<?> objects) {
        validateInput(objects);

        Class<?> clazz = objects.get(0).getClass();
        List<Field> fields = getFields(clazz);
        StringBuilder result = new StringBuilder();

        result.append(createHeaderString(fields));

        objects.forEach(
                obj -> result.append(CSV_LINE_SEPARATOR).append(createRowString(obj, fields))
        );

        return result.toString();
    }

    /**
     * Создание строки данных для одного объекта
     *
     * @param obj    объект для получения полей и сериализации
     * @param fields список полей объекта для сериализации
     */
    private String createRowString(Object obj, List<Field> fields) {
        return fields.stream()
                .map(field -> getFieldValue(obj, field))
                .map(this::getValueAsString)
                .map(this::escapeCsv)
                .collect(Collectors.joining(CSV_COLUMN_SEPARATOR));
    }

    /**
     * Получает все поля класса. Пропускает отмеченные флагом ignore аннотации CsvField
     *
     * @param clazz класс для получения полей
     */
    private List<Field> getFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !(field.isAnnotationPresent(CsvField.class) && field.getAnnotation(CsvField.class).ignore()))
                .toList();
    }

    /**
     * Получает значение объекта как String. Для списков использует значения через CSV_LIST_VALUE_SEPARATOR.
     *
     * @param value объект для обработки
     * @return строковое представление для записи
     */
    private String getValueAsString(Object value) {
        if (value == null) {
            return "";
        } else if (value instanceof Collection<?> collection) {
            return collection.stream()
                    .map(this::getValueAsString)
                    .collect(Collectors.joining(CSV_LIST_VALUE_SEPARATOR));
        } else {
            return value.toString();
        }
    }

    /**
     * Создание строки хедера для csv файла.
     * Для поля с аннотацией берет имя из нее. Для полей без использует имя поля.
     *
     * @param fields список полей
     * @return строка с хедером
     */
    private String createHeaderString(List<Field> fields) {
        return fields.stream()
                .map(field -> {
                            CsvField annotation = field.getAnnotation(CsvField.class);
                            return (annotation != null) ? annotation.value() : field.getName();
                        }
                )
                .collect(Collectors.joining(CSV_COLUMN_SEPARATOR));
    }

    /**
     * Получает значение поля объекта
     */
    private Object getFieldValue(Object obj, Field field) {
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot access field: " + field.getName(), e);
        }
    }

    /**
     * Экранирует значение для CSV. Если есть разделители берет значение в кавычки.
     * Для кавычек удваивает.
     */
    private String escapeCsv(String value) {
        boolean needsQuotes = value.contains(CSV_COLUMN_SEPARATOR) ||
                              value.contains(CSV_QUOTES_ESCAPE_SYMBOL) ||
                              value.contains(CSV_LINE_SEPARATOR);
        if (needsQuotes) {
            // Удваиваем кавычки внутри значения
            String escaped = value.replace(CSV_QUOTES_ESCAPE_SYMBOL, CSV_QUOTES_ESCAPE_SYMBOL.repeat(2));
            return CSV_QUOTES_ESCAPE_SYMBOL + escaped + CSV_QUOTES_ESCAPE_SYMBOL;
        }
        return value;
    }

    /**
     * Валидация входных данных. Проверяет чтобы все объекты из списка были одного класса.
     */
    private void validateInput(List<?> objects) {
        if (objects == null) {
            throw new IllegalArgumentException("Objects list cannot be null");
        }
        if (objects.isEmpty()) {
            throw new IllegalArgumentException("Objects list cannot be empty");
        }

        Class<?> firstClass = objects.get(0).getClass();
        objects.forEach(obj -> {
            if (!obj.getClass().equals(firstClass)) {
                throw new IllegalArgumentException("All objects must be of the same type");
            }
        });
    }
}
