package org.writer;

import lombok.SneakyThrows;
import org.writer.model.CSVField;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.StringJoiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Реализация интерфейса {@link Writable} для записи данных объектов в CSV-файл.
 * Этот класс предоставляет функциональность для записи объектов в файл CSV, используя аннотацию
 * {@link CSVField} для определения полей, которые должны быть записаны.
 * @see Writable
 * @see CSVField
 */
public class WritableImpl implements Writable {

    /**
     * Логгер для записи событий и ошибок в процессе работы класса.
     */
     private static final Logger logger = LoggerFactory.getLogger(WritableImpl.class);

    /**
     * Записывает список объектов в CSV-файл с указанным именем.
     * Метод анализирует поля объектов, помеченные аннотацией {@link CSVField}, и записывает их
     * в CSV-файл. Если поле является списком или перечислением, оно будет корректно сериализовано
     * в строковое представление.
     * Пример использования:
     * <pre>{@code
     * List<Person> people = List.of(new Person("John", "Doe", 1, Months.JANUARY, 1990));
     * writable.writeToFile(people, "people.csv");
     * }</pre>
     * @param data     Список объектов для записи в файл. Не может быть {@code null} или пустым.
     * @param fileName Имя файла, в который будут записаны данные. Не может быть {@code null}.
     * @throws IllegalArgumentException Если переданный список {@code data} равен {@code null} или пуст.
     * @throws RuntimeException         Если происходит ошибка при записи в файл.
     * @see CSVField
     */
    @Override
    public void writeToFile(List<?> data, String fileName) {
        //Проверка null для data
        if (data == null || data.isEmpty()) {
            logger.error("Data is null or empty");
            throw new IllegalArgumentException("Data list cannot be null or empty");
        }
        //Проверка null для fileName
        if (fileName == null) {
            logger.error("File name is null");
            throw new IllegalArgumentException("File name cannot be null");
        }

        Class<?> clazz = data.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();

        try (FileWriter writer = new FileWriter(fileName)) {
            // Записываем заголовки
            StringJoiner headers = new StringJoiner(",");
            for (Field field : fields) {
                if (field.isAnnotationPresent(CSVField.class)) {
                    CSVField csvField = field.getAnnotation(CSVField.class);
                    headers.add(csvField.name().isEmpty() ? field.getName() : csvField.name());
                }
            }
            writer.append(headers.toString()).append("\n");
            logger.debug("Headers written: {}", headers);

            // Записываем данные
            for (Object item : data) {
                StringJoiner row = new StringJoiner(",");
                for (Field field : fields) {
                    if (field.isAnnotationPresent(CSVField.class)) {
                        field.setAccessible(true);
                        Object value = null;
                        try {
                            value = field.get(item);
                        } catch (IllegalAccessException e) {
                            logger.error(e.getMessage());
                        }
                        //Обработка списков
                        if (value instanceof List) {
                            StringJoiner joiner = new StringJoiner(",");
                            for (Object element : (List<?>) value) {
                                joiner.add(element.toString());
                            }
                            value = joiner.toString();
                        }
                        //Обработка перечислений
                        if (value instanceof Enum) {
                            value = ((Enum<?>) value).name();
                        }
                        row.add(value != null ? value.toString() : "");
                    }
                }
                writer.append(row.toString()).append('\n');
                logger.debug("Row written: {}", row);
            }
            logger.info("Data successfully written to {}", fileName);

        } catch (IOException e) {
            logger.error("Error writing to file {}", fileName, e);
            throw new RuntimeException("Error writing to file: " + fileName, e);
        }
    }
}
