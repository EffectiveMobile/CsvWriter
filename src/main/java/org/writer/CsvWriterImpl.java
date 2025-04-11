package org.writer;

import org.writer.annotation.CsvColumn;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Реализация Writable для записи списка объектов в CSV-файл.
 */
public class CsvWriterImpl implements Writable {
    private static final String SEPARATOR = ",";
    private static final Logger LOGGER = Logger.getLogger(CsvWriterImpl.class.getName());


    /**
     * Записывает список объектов в CSV-файл. При ошибках информация записывается в лог.
     * @param data список объектов для записи
     * @param fileName имя файла для записи
     */
    @Override
    public void writeToFile(List<?> data, String fileName) {
        if (data == null || data.isEmpty()) {
            LOGGER.warning("Data list is null or empty, skipping write to " + fileName);
            return;
        }

        List<Field> csvFields = extractCsvFields(data.get(0).getClass());
        String header = buildHeader(csvFields);
        List<String> rows = buildRows(data, csvFields);

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(header + "\n");
            for (String row : rows) {
                writer.write(row + "\n");
            }
        } catch (Exception e) {
            LOGGER.severe("Failed to write to file " + fileName + ": " + e.getMessage());
        }
    }

    /**
     * Извлекает поля класса, помеченные аннотацией CsvColumn, и сортирует их по порядку.
     * @param clazz класс объекта, из которого извлекаются поля
     * @return список отсортированных полей с аннотацией CsvColumn
     */
    private List<Field> extractCsvFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(CsvColumn.class))
                .sorted(Comparator.comparingInt(f -> f.getAnnotation(CsvColumn.class).order()))
                .toList();
    }

    /**
     * Формирует строку заголовков CSV на основе аннотаций CsvColumn.
     * @param csvFields список полей с аннотацией CsvColumn
     * @return строка заголовков, разделённых запятыми
     */
    private String buildHeader(List<Field> csvFields) {
        return csvFields.stream()
                .map(field -> {
                    CsvColumn annotation = field.getAnnotation(CsvColumn.class);
                    return annotation.name().isEmpty() ? field.getName() : annotation.name();
                })
                .collect(Collectors.joining(SEPARATOR));
    }

    /**
     * Преобразует список объектов в список строк CSV, используя поля с аннотацией CsvColumn.
     * @param data список объектов для преобразования
     * @param csvFields список полей с аннотацией CsvColumn
     * @return список строк CSV, готовых для записи
     */
    private List<String> buildRows(List<?> data, List<Field> csvFields) {
        return data.stream()
                .map(obj -> csvFields.stream()
                        .map(field -> {
                            try {
                                field.setAccessible(true);
                                Object value = field.get(obj);
                                if (value instanceof List) {
                                    return ((List<?>) value).stream()
                                            .map(Object::toString)
                                            .collect(Collectors.joining(";"));
                                }
                                return value != null ? value.toString() : "";
                            } catch (IllegalAccessException e) {
                                LOGGER.warning("Failed to access field " + field.getName() + ": " + e.getMessage());
                                return "";
                            }
                        })
                        .collect(Collectors.joining(SEPARATOR)))
                .toList();
    }
}