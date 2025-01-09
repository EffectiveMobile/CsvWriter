package org.writer.service;

import org.writer.annotation.CsvField;
import org.writer.exception.FieldAccessException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * Сервис для форматирования данных в CSV-формат.
 * <p>
 * Обрабатываетт аннотированные поля объектов и преобразует их в строки
 * заголовков и данных для дальнейшей записи в файл.
 */
public class CsvFormatter implements Formatter {


    /**
     * Форматируетт заголовки CSV файла на основе аннотированных полей объектов.
     *
     * @param data список объектов, для которых требуется формирование заголовков.
     * @return строка заголовков, разделённых запятыми.
     * @throws IllegalArgumentException если список данных пустой.
     */
    public String formatHeaders(List<?> data) {
        Class<?> clazz = data.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();

        List<Field> annotatedFields = getAnnotatedFields(fields);

        return annotatedFields.stream()
                .flatMap(field -> {
                    CsvField annotation = field.getAnnotation(CsvField.class);
                    String baseName = annotation.name().isEmpty() ? field.getName() : annotation.name();
                    if (field.getType().equals(List.class)) {
                        List<?> list = getListValue(field, data.get(0));

                        return IntStream.range(0, list.size())
                                .mapToObj(i -> baseName + (i + 1));
                    } else {
                        return Stream.of(baseName);
                    }
                })
                .collect(Collectors.joining(","));
    }



    /**
     * Форматирует данные объектов в строки для CSV файла.
     *
     * @param data список объектов для преобразования в CSV-строки.
     * @return список строк, каждая из которых представляет одну строку CSV.
     * @throws FieldAccessException если невозможно получить доступ к полю объекта.
     */
    public List<String> formatData(List<?> data) {

        if (data.isEmpty()) {
            return List.of();
        }

        Class<?> clazz = data.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();

        List<Field> annotatedFields = getAnnotatedFields(fields);

        return data.stream()
                .map(obj -> annotatedFields.stream()
                        .flatMap(field -> {
                            field.setAccessible(true);
                            try {
                                Object value = field.get(obj);
                                if (value instanceof List<?> list) {
                                    return list.stream()
                                            .map(String::valueOf);
                                } else {
                                    return Stream.of(String.valueOf(value));
                                }
                            } catch (IllegalAccessException e) {
                                throw new FieldAccessException("Error accessing field: " + field.getName(), e);
                            }
                        })
                        .collect(Collectors.joining(",")))
                .collect(Collectors.toList());
    }


    /**
     * Получает значение списка из указанного поля объекта.
     *
     * @param field поле объекта, содержащее список.
     * @param obj   объект, из которого извлекается значение поля.
     * @return список значений
     * @throws FieldAccessException если невозможно получить доступ к полю объекта.
     */
    private List<?> getListValue(Field field, Object obj) {
        try {
            field.setAccessible(true);
            return (List<?>) field.get(obj);
        } catch (IllegalAccessException e) {
            throw new FieldAccessException("Error accessing list field: " + field.getName(), e);
        }
    }

    /**
     * Получает список полей, аннотированных аннотацией {@link CsvField}.
     *
     * @param fields массив полей класса.
     * @return список аннотированных полей.
     */
    private List<Field> getAnnotatedFields(Field[] fields) {
        return Stream.of(fields)
                .filter(field -> field.isAnnotationPresent(CsvField.class))
                .collect(Collectors.toList());
    }
}
