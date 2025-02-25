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
 * Реализация Writable для записи списка объектов в CSV-файл с использованием рефлексии и аннотаций.
 */
public class CsvWriterImpl implements Writable {
    private static final String SEPARATOR = ",";
    private static final Logger LOGGER = Logger.getLogger(CsvWriterImpl.class.getName());

    /**
     * Записывает список объектов в CSV-файл.
     * @param data Список объектов для записи.
     * @param fileName Имя файла.
     */
    @Override
    public void writeToFile(List<?> data, String fileName) {
        if (data == null || data.isEmpty()) {
            LOGGER.warning("Data list is null or empty, skipping write to " + fileName);
            return;
        }

        Class<?> clazz = data.get(0).getClass();
        List<Field> csvFields = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(CsvColumn.class))
                .sorted(Comparator.comparingInt(f -> f.getAnnotation(CsvColumn.class).order()))
                .toList();

        try (FileWriter writer = new FileWriter(fileName)) {

            String header = csvFields.stream()
                    .map(field -> {
                        CsvColumn annotation = field.getAnnotation(CsvColumn.class);
                        return annotation.name().isEmpty() ? field.getName() : annotation.name();
                    })
                    .collect(Collectors.joining(SEPARATOR));
            writer.write(header + "\n");

            for (Object obj : data) {
                String row = csvFields.stream()
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
                        .collect(Collectors.joining(SEPARATOR));
                writer.write(row + "\n");
            }
        } catch (Exception e) {
            LOGGER.severe("Failed to write to file " + fileName + ": " + e.getMessage());
        }
    }
}