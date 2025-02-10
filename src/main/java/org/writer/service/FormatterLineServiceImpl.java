package org.writer.service;

import org.writer.annotation.CsvField;
import org.writer.exception.FieldAccessException;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Реализация интерфейса {@link FormatterLineService} для подготовки данных перед записью в CSV-файл.
 */
public class FormatterLineServiceImpl implements FormatterLineService {


    /**
     * Форматирования данных для записи заголовка в файл.
     *
     * @param data - список с объектами.
     * @return - строку с названиями колонок.
     */
    @Override
    public StringBuilder formatHeaders(List<?> data) {

        List<Field> list = getFieldsWithAnnotation(getDeclaredFields(data));

        return new StringBuilder().append(
                String.join(",", list.stream()
                .flatMap(field -> {
                    CsvField csvField = field.getAnnotation(CsvField.class);
                    String headerName = csvField.name().isEmpty() ? field.getName() : csvField.name();
                    return Stream.of(headerName);
                })
                        .toArray(String[]::new)));
    }


    /**
     * Форматирование данных для записи в файл.
     *
     * @param data - список с объектами.
     * @return - строку с данными.
     * @throws FieldAccessException - если нет доступа к полю.
     */
    @Override
    public StringBuilder formatLine(List<?> data) {
        if(data == null || data.isEmpty())
            return new StringBuilder();

        List<Field> list = getFieldsWithAnnotation(getDeclaredFields(data));

        return new StringBuilder().append(
                String.join("\n",
                        data.stream()
                                .map(obj -> list.stream()
                                        .flatMap(field -> {
                                            field.setAccessible(true);
                                            try {
                                                Object val = field.get(obj);
                                                if (val instanceof Collection<?> collection) {
                                                    return Stream.of(getCollectionsData(collection));
                                                }else{
                                                    return Stream.of(String.valueOf(val == null ? "" : String.valueOf(val)));
                                                }
                                            } catch (IllegalAccessException e) {
                                                throw new FieldAccessException("Field access error", e);
                                            }
                                        }).collect(Collectors.joining(","))
                                )
                                .toArray(String[]::new)));
    }


    /**
     * Получение данных из полей с коллекциями.
     *
     * @param collection - коллекция с данными.
     * @return - строку с данными из коллекции.
     */
    private String getCollectionsData(Collection<?> collection) {
        return String.join(";", collection.stream()
                .map(Objects::toString)
                .toArray(String[]::new));
    }

    /**
     * Получение всех полей (включая приватные) класса первого объекта из списка
     *
     * @param data список объектов.
     * @return массив полей {@link Field[]} класса первого объекта в списке.
     */
    private Field[] getDeclaredFields(List<?> data) {
        Object first = data.get(0);
        Class<?> clazz = first.getClass();
        return clazz.getDeclaredFields();
    }

    /**
     * Получение всех полей с аннотацией {@link CsvField}.
     *
     * @param fields - массив со всеми полями объекта.
     * @return - массив полей с аннотацией {@link CsvField}.
     */
    private List<Field> getFieldsWithAnnotation(Field[] fields) {

        return Stream.of(fields)
                .filter(field -> field.isAnnotationPresent(CsvField.class))
                .collect(Collectors.toList());
    }
}
