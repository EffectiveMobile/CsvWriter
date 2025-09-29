package org.writer.writer;

import org.writer.annotation.CsvColumn;
import org.writer.annotation.CsvEntity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Реализация интерфейса {@link Writable}, которая сохраняет
 * список объектов в файл в формате CSV, используя аннотации и рефлексию.
 */
public class CsvWriterImpl implements Writable {
    private static final String CSV_DELIMITER = ",";
    private static final String COLLECTION_DELIMITER = ";";
    private static final String KEY_VALUE_DELIMITER = ":";

    /**
     * Записывает данные в CSV-файл. Первой строкой всегда будет заголовок,
     * сформированный из аннотаций {@link CsvColumn}.
     *
     * @param data список объектов для записи. Класс объектов должен быть помечен
     *             аннотацией {@link CsvEntity}.
     * @param fileName имя файла, в который будут сохранены данные.
     *
     * @throws IllegalArgumentException если список data: <p>
     *                                  а) {@code null}, <p>
     *                                  б) {@code data.isEmpty()}, <p>
     *                                  в) содержит объекты, не помеченные аннотацией {@link CsvEntity}; <p>
     *                                  если строка fileName: <p>
     *                                  а) {@code null}, <p>
     *                                  б) {@code fileName.isBlank()}.
     * @throws RuntimeException в случае ошибки ввода-вывода при записи файла.
     */
    @Override
    public void writeToFile(final List<?> data, final String fileName) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("data не должен быть isEmpty или null");
        }

        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("fileName не должен быть isBlank или null");
        }

        Class<?> csvEntityClass = data.get(0).getClass();
        if (!csvEntityClass.isAnnotationPresent(CsvEntity.class)) {
            throw new IllegalArgumentException(csvEntityClass.getSimpleName() + " не имеет аннотации @CsvEntity");
        }

        List<Field> sortedFields = getSortedCsvFields(csvEntityClass);

        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(fileName))) {
            String header = generateHeader(sortedFields);
            writer.write(header);
            writer.newLine();

            for (Object item : data) {
                String row = convertObjectToCsvRow(item, sortedFields);
                writer.write(row);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка во время записи в файл: " + fileName, e);
        }
    }

    /**
     * Находит все поля, помеченные аннотацией {@link CsvColumn},
     * проверяет уникальность их параметров {@link CsvColumn#name()} и {@link CsvColumn#order()},
     * и сортирует их в соответствии с параметром {@code order}.
     *
     * @param csvEntityClass класс для сканирования.
     *
     * @return отсортированный список полей.
     *
     * @throws IllegalStateException если найдены дублирующиеся значения {@code name} или {@code order}
     *                               в аннотациях {@link CsvColumn} в пределах класса.
     */
    private List<Field> getSortedCsvFields(final Class<?> csvEntityClass) {
        List<Field> fields = Arrays.stream(csvEntityClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(CsvColumn.class))
                .toList();

        List<String> names = fields.stream()
                .map(field -> field.getAnnotation(CsvColumn.class).name())
                .toList();
        if (new HashSet<>(names).size() < names.size()) {
            throw new IllegalStateException("В классе " + csvEntityClass.getSimpleName() + " найдены дублирующиеся значения \"name\" в аннотациях @CsvColumn");
        }

        List<Integer> orders = fields.stream()
                .map(field -> field.getAnnotation(CsvColumn.class).order())
                .toList();
        if (new HashSet<>(orders).size() < orders.size()) {
            throw new IllegalStateException("В классе " + csvEntityClass.getSimpleName() + " найдены дублирующиеся значения \"order\" в аннотациях @CsvColumn");
        }

        return fields.stream()
                .sorted(Comparator.comparingInt(field -> field.getAnnotation(CsvColumn.class).order()))
                .toList();
    }

    /**
     * Генерирует строку заголовка CSV на основе сортированного списка полей.
     *
     * @param sortedFields отсортированный список полей.
     *
     * @return строка заголовка, например, "firstName,lastName,birthYear".
     */
    private String generateHeader(final List<Field> sortedFields) {
        return sortedFields.stream()
                .map(field -> field.getAnnotation(CsvColumn.class).name())
                .map(this::escapeCsvValue)
                .collect(Collectors.joining(CSV_DELIMITER));
    }

    /**
     * Преобразует один объект в строку формата CSV.
     *
     * @param item объект для преобразования.
     * @param sortedFields отсортированный список полей, значения которых нужно извлечь.
     *
     * @return строка в формате CSV, например, "John,Doe,1990".
     *
     * @throws RuntimeException если не удалось получить доступ к полю.
     */
    private String convertObjectToCsvRow(final Object item, final List<Field> sortedFields) {
        return sortedFields.stream()
                .map(field -> {
                    try {
                        field.setAccessible(true);
                        Object value = field.get(item);
                        String stringValue = convertFieldValueToString(value);
                        return escapeCsvValue(stringValue);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Не удалось получить доступ к полю: " + field.getName(), e);
                    }
                })
                .collect(Collectors.joining(CSV_DELIMITER));
    }

    /**
     * Конвертирует значение поля в его строковое представление для CSV.
     * Обрабатывает {@code Map}, разделяя ключ-значение {@code KEY_VALUE_DELIMITER},
     * и объединяя все пары {@code COLLECTION_DELIMITER}.
     * Обрабатывает {@code Collection}, объединяя их элементы {@code COLLECTION_DELIMITER}.
     *
     * @param value значение поля.
     *
     * @return строковое представление значения.
     */
    private String convertFieldValueToString(final Object value) {
        if (value == null) {
            return "";
        }

        if (value instanceof Map<?, ?> map) {
            return map.entrySet().stream()
                    .map(entry -> entry.getKey().toString() + KEY_VALUE_DELIMITER + entry.getValue().toString())
                    .collect(Collectors.joining(COLLECTION_DELIMITER));
        }

        if (value instanceof Collection<?> collection) {
            return collection.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(COLLECTION_DELIMITER));
        }

        return value.toString();
    }

    /**
     * Экранирует значение поля для CSV формата в соответствии с RFC 4180.
     *
     * @param value исходное строковое значение.
     *
     * @return экранированное значение, готовое к записи в CSV.
     */
    private String escapeCsvValue(final String value) {
        if (value.contains(CSV_DELIMITER) || value.contains("\"") || value.contains("\n")) {
            String escapedValue = value.replace("\"", "\"\"");
            return "\"" + escapedValue + "\"";
        }

        return value;
    }
}
