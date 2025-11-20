package org.writer;

import lombok.extern.slf4j.Slf4j;
import org.writer.annotation.CsvColumn;
import org.writer.exception.CsvWriteException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Класс для записи объектов в CSV формат.
 * <p>
 * Класс использует рефлексию: поля, помеченные аннотацией {@link CsvColumn},
 * извлекаются, сортируются по порядку и записываются в файл как строки CSV.
 * Поддерживается экранирование значений, содержащих запятые, кавычки или переносы строки.
 * </p>
 */
@Slf4j
public class CsvWriter implements Writable {

    /**
     * Записывает список объектов в CSV-файл.
     *
     * @param data список объектов, которые необходимо сохранить
     * @param fileName имя создаваемого файла (существующий будет перезаписан)
     * @throws IllegalArgumentException если список пустой или null
     * @throws CsvWriteException при ошибке ввода-вывода
     */
    @Override
    public void writeToFile(List<?> data, String fileName) {
        if (data == null || data.isEmpty()) {
            log.error("Data list is empty");
            throw new IllegalArgumentException("Data list is empty");
        }

        Class<?> clazz = data.get(0).getClass();
        List<Field> csvFields = getCsvFields(clazz);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(buildHeader(csvFields));
            writer.newLine();

            for (Object object : data) {
                writer.write(buildRow(object, csvFields));
                writer.newLine();
            }

        } catch (IOException e) {
            log.error(e.getMessage());
            throw new CsvWriteException("Failed to write CSV file: " + fileName);
        }
    }

    /**
     * Возвращает отсортированный список полей с аннотацией {@link CsvColumn}.
     *
     * @param clazz класс модели
     * @return список отсортированных полей
     */
    private List<Field> getCsvFields(Class<?> clazz) {
        return Stream.of(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(CsvColumn.class))
                .sorted(Comparator.comparingInt(field -> field.getAnnotation(CsvColumn.class).order()))
                .collect(Collectors.toList());
    }

    /**
     * Формирует строку заголовка CSV на основе имени столбца из аннотации {@link CsvColumn}.
     *
     * @param fields список полей
     * @return строка заголовка
     */
    private String buildHeader(List<Field> fields) {
        return fields.stream()
                .map(field -> field.getAnnotation(CsvColumn.class).name())
                .collect(Collectors.joining(","));
    }

    /**
     * Формирует строку CSV, извлекая значения полей объекта.
     *
     * @param object объект модели
     * @param fields список полей для экспорта
     * @return строка в формате CSV
     */
    private String buildRow(Object object, List<Field> fields) {
        return fields.stream()
                .map(field -> {
                    try {
                        field.setAccessible(true);
                        return escapeCsvValue(convertValue(field.get(object)));
                    } catch (IllegalAccessException e) {
                        log.error(e.getMessage());
                        throw new CsvWriteException(e.getMessage());
                    }
                })
                .collect(Collectors.joining(","));
    }

    /**
     * Преобразует значение поля в строку.
     *
     * @param value значение поля
     * @return строковое представление
     */
    private String convertValue(Object value) {
        if (value == null) {
            return "";
        }

        if (value instanceof List<?> list) {
            return list.toString();
        }

        return value.toString();
    }

    /**
     * Экранирует значение для корректной записи в CSV.
     *
     * @param value исходное значение
     * @return экранированное значение
     */
    private String escapeCsvValue(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value.trim();
    }
}
