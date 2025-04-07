package org.writer.csv;


import lombok.extern.slf4j.Slf4j;
import org.writer.Writable;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация интерфейса {@link Writable} для сохранения объектов в формате CSV.
 * Использует reflection и аннотацию {@link CsvField} для определения, какие поля сериализовать.
 */

@Slf4j
public class CsvWriter implements Writable {

    /**
     * Записывает список объектов в CSV файл.
     * Только поля, помеченные {@link CsvField}, будут включены.
     *
     * @param data     список объектов
     * @param fileName имя CSV файла
     * @throws IOException если возникает ошибка записи
     */
    @Override
    public void writeToFile(List<?> data, String fileName) throws IOException {
        if (data == null || data.isEmpty()) return;

        Class<?> clazz = data.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();

        List<Field> annotatedFields = getAnnotatedFields(fields);

        try (FileWriter writer = new FileWriter(fileName)) {

            // Заголовок
            String header = annotatedFields.stream()
                    .map(f -> {
                        CsvField ann = f.getAnnotation(CsvField.class);
                        return !ann.name().isEmpty() ? ann.name() : f.getName();
                    })
                    .collect(Collectors.joining(" | "));
            writer.write(header + "\n");

            // Данные
            for (Object obj : data) {
                String row = annotatedFields.stream().map(f -> {
                    f.setAccessible(true);
                    try {
                        Object value = f.get(obj);
                        return formatValue(value);
                    } catch (IllegalAccessException e) {
                        return "";
                    }
                }).collect(Collectors.joining(" | "));
                writer.write(row + "\n");
            }
            log.info("Файл успешно создан: {}", fileName);
        }
    }

    /**
     * Возвращает список полей, помеченных {@link CsvField}.
     *
     * @param fields все поля класса
     * @return список аннотированных полей
     */
    private List<Field> getAnnotatedFields(Field[] fields) {
        return List.of(fields).stream()
                .filter(f -> f.isAnnotationPresent(CsvField.class))
                .collect(Collectors.toList());
    }

    /**
     * Форматирует значение поля перед записью в CSV.
     * Поддерживает {@code List}, {@code Enum} и простые типы.
     *
     * @param value значение поля
     * @return строковое представление
     */
    private String formatValue(Object value) {
        if (value == null) return "";
        if (value instanceof List<?>) {
            return ((List<?>) value).stream()
                    .map(Object::toString)
                    .collect(Collectors.joining("; "));
        }
        if (value instanceof Enum<?>) {
            return ((Enum<?>) value).name();
        }
        return value.toString();
    }
}