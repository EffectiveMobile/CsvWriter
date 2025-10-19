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
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Реализация интерфейса {@link Writable}, позволяющая сериализовать список объектов в CSV-файл.
 * <p>
 * Класс использует рефлексию для извлечения имён полей и их значений.
 * Поля, помеченные аннотацией {@link Transient}, игнорируются при записи.
 * </p>
 */
public class CsvDocument implements Writable {

    /** Расширение файлов по умолчанию. */
    private static final String EXTENSION = ".csv";

    /**
     * Предикат для фильтрации полей, исключающий те, что помечены аннотацией {@link Transient}.
     */
    private final Predicate<Field> skipTransient = field -> !field.isAnnotationPresent(Transient.class);

    /**
     * Записывает список объектов в CSV-файл.
     *
     * @param data     список объектов для сериализации
     * @param fileName имя файла без расширения (расширение {@code .csv} добавляется автоматически)
     * @throws RuntimeException если список пустой, равен {@code null} или произошла ошибка записи
     */
    @Override
    public void writeToFile(List<?> data, String fileName) {
        if (data == null || data.isEmpty())
            throw new RuntimeException("Data list is null or empty!");

        String form = form(data);
        Path path = Path.of(fileName + EXTENSION);
        try {
            Files.writeString(path, form);
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong while writing CSV file!", e);
        }
    }

    /**
     * Извлекает список имён полей объекта (колонки CSV).
     * Поля с аннотацией {@link Transient} исключаются.
     *
     * @param instance объект, чьи поля анализируются
     * @param <T>      тип объекта
     * @return список имён полей
     */
    public <T> List<String> columns(T instance) {
        Class<?> metadata = instance.getClass();
        return Arrays.stream(metadata.getDeclaredFields())
                .filter(skipTransient)
                .map(Field::getName)
                .collect(Collectors.toList());
    }

    /**
     * Извлекает список значений полей объекта.
     * Поля с аннотацией {@link Transient} исключаются.
     *
     * @param instance объект, чьи значения полей извлекаются
     * @param <T>      тип объекта
     * @return список строковых представлений значений
     * @throws RuntimeException если доступ к полю невозможен
     */
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
                throw new RuntimeException("Access denied!", e);
            }
        }
        return values;
    }

    /**
     * Формирует строковое представление CSV-документа.
     * Первая строка содержит заголовки (имена полей),
     * последующие строки — значения объектов.
     *
     * @param data список объектов для сериализации
     * @return строка в формате CSV
     */
    private String form(List<?> data) {
        StringBuilder sb = new StringBuilder();

        // Заголовки
        columns(data.get(0)).forEach(i -> sb.append(i).append(", "));
        sb.setLength(sb.length() - 2);
        sb.append('\n');

        // Значения
        for (int i = 0; i < data.size(); i++) {
            values(data.get(i)).stream()
                    .filter(Objects::nonNull)
                    .forEach(v -> sb.append(v).append(", "));
            sb.setLength(sb.length() - 2);
            sb.append('\n');
        }
        return sb.toString();
    }
}
