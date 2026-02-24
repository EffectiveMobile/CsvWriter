package org.writer.service;

import org.writer.annotation.ColumnCsv;
import org.writer.exception.EmptyListException;
import org.writer.exception.FileWritingException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация интерфейса {@link Writable}, выполняющая сериализацию
 * списка объектов в CSV-файл с использованием Reflection и аннотации {@link ColumnCsv}.
 * <p>
 * Класс предназначен для сохранения коллекций объектов в виде таблицы CSV.
 * Если значение поля является {@link List}, элементы списка объединяются через «;».
 * </p>
 *
 * @author ZhelnovachevRoman
 */
public class CsvWriter implements Writable {

    @Override
    public void writeToFile(List<?> data,
                            String fileName) {
        if (data == null || data.isEmpty()) {
            throw new EmptyListException("Список пуст");
        }
        Class<?> ourClass = data.get(0)
                .getClass();
        Field[] fields = ourClass.getDeclaredFields();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String header = headerConstructor(fields);
            writer.write(header);
            writer.newLine();
            for (Object object : data) {
                String line = lineConstructor(object,
                        fields);
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e){
            throw new FileWritingException(fileName);
        }
    }

    /**
     * Формирует строку заголовков CSV на основе аннотаций {@link ColumnCsv}.
     *
     * @param fields массив полей класса, извлечённых через Reflection.
     * @return строка заголовков, объединённая через запятую.
     */
    private String headerConstructor(Field[] fields){
        return Arrays.stream(fields)
                .map(field -> {
                    ColumnCsv annotation = field.getAnnotation(ColumnCsv.class);
                    return  annotation.name();
                })
                .collect(Collectors.joining(","));
     }

    /**
     * Формирует строку данных для одного объекта.
     * Если поле содержит список, его элементы соединяются через «;».
     *
     * @param object объект, из которого извлекаются значения.
     * @param fields массив полей класса, доступных для чтения.
     * @return строка данных, объединённая через запятую.
     */
     private String lineConstructor(Object object, Field[] fields) {
        return Arrays.stream(fields)
                .map(field -> {
                    field.setAccessible(true);
                    Object value;
                    try {
                        value = field.get(object);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    if (value instanceof List<?> list) {
                        return list.stream()
                                .map(Object::toString)
                                .collect(Collectors.joining(";"));
                    }
                    return String.valueOf(value);
                })
                .collect(Collectors.joining(","));
    }
}
