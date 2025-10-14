package org.writer;

import org.writer.annotation.CsvColumn;
import org.writer.annotation.CsvSerializable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Реализация интерфейса {@link Writable}, который сериализует список объектов в CSV файл.
 * <p>
 * Данная реализация использует рефлексию, а также кастомные аннотации ({@link CsvSerializable},
 * {@link CsvColumn}) для валидации данных, подлежащих записи.
 */
public class CsvWriter implements Writable {

    /**
     * Разделитель, используемый для отделения значений в строке CSV.
     */
    private final static String SEPARATOR = ",";

    /**
     * Разделитель, используемый для объединения элементов списка в одну ячейку CSV.
     */
    private static final String LIST_ITEM_DELIM = ";";

    /**
     * Записывает список объектов в CSV файл.
     * <p>
     * Сначала записывается строка заголовка, основанная на именах колонок,
     * а затем для каждого объекта из списка записывается строка данных.
     *
     * @param data     список объектов для записи. Класс этих объектов должен быть
     *                 помечен аннотацией {@link CsvSerializable}.
     * @param fileName имя файла, в который будут сохранены данные.
     * @throws IllegalArgumentException если список данных null или пуст, или если
     *                                  класс объектов не помечен аннотацией {@link CsvSerializable}.
     * @throws RuntimeException         если возникает ошибка ввода-вывода при записи в файл.
     */
    @Override
    public void writeToFile(List<?> data, String fileName) {

        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Список данных не может быть пустым.");
        }

        Class<?> clazz = data.get(0).getClass();
        if (!clazz.isAnnotationPresent(CsvSerializable.class)) {
            throw new IllegalArgumentException("Класс " + clazz.getSimpleName() + " не помечен аннотацией @CsvSerializable.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            List<Field> fields = getCsvFields(clazz);

            // Header
            String header = fields.stream()
                    .map(this::getColumnName)
                    .collect(Collectors.joining(SEPARATOR));

            writer.write(header);
            writer.newLine();

            // Body
            String body = data.stream()
                    .map(object -> fields.stream()
                            .map(field -> getFieldValue(field, object))
                            .collect(Collectors.joining(SEPARATOR)))
                    .collect(Collectors.joining(System.lineSeparator()));

            writer.write(body);
        }
        catch (IOException e) {
            throw new RuntimeException("Ошибка записи в файл " + fileName, e);
        }
    }

    /**
     * Сканирует класс на наличие полей, аннотированных {@link CsvColumn}, с помощью рефлексии.
     *
     * @param clazz класс для сканирования.
     * @return список объектов {@link Field}, которые помечены для сериализации в CSV.
     */
    private List<Field> getCsvFields(Class<?> clazz){
        return Stream.of(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(CsvColumn.class))
                .collect(Collectors.toList());
    }

    /**
     * Определяет имя колонки для заданного поля.
     * <p>
     * В приоритете используется атрибут {@code name} из аннотации {@link CsvColumn}.
     * Если он пуст, используется фактическое имя поля.
     *
     * @param field поле, для которого нужно получить имя колонки.
     * @return имя колонки CSV.
     */
    private String getColumnName(Field field) {
        String name = field.getAnnotation(CsvColumn.class).name();
        return name.isEmpty() ? field.getName() : name;
    }

    /**
     * Извлекает строковое представление значения поля из заданного объекта.
     * <p>
     * Предоставляет специальную обработку для полей типа {@link List},
     * объединяя их элементы в одну строку с помощью разделителя {@code LIST_ITEM_DELIM}.
     *
     * @param field  поле, значение которого нужно извлечь.
     * @param object экземпляр объекта, из которого извлекается значение.
     * @return строковое представление значения поля.
     * @throws RuntimeException если доступ к полю невозможен.
     */
    private String getFieldValue(Field field, Object object) {
        try {
            field.setAccessible(true);
            Object value = field.get(object);
            if (value instanceof List) {
                return ((List<?>) value).stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(LIST_ITEM_DELIM));
            }
            return String.valueOf(value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Не удалось получить доступ к полю " + field.getName(), e);
        }
    }
}