package org.writer.service.impl;

import org.writer.annotations.CSV;
import org.writer.annotations.CsvListField;
import org.writer.service.Writable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация интерфейса Writable для записи данных в CSV-формат.
 * Класс предоставляет функциональность для преобразования списка объектов в CSV-файл,
 * с поддержкой специальной обработки полей, помеченных аннотацией @CsvListField.
 * Требования:
 *   Класс объектов должен быть помечен аннотацией @CSV
 *   Поля-коллекции должны быть помечены аннотацией @CsvListField
 */
public class CsvWritableImpl implements Writable {

    /**
     * Записывает список объектов в CSV-файл.
     * @param data List<?> список объектов для преобразования
     * @param fileName задает имя файла, куда будет производиться запись
     * @throws IllegalArgumentException если класс объектов не помечен аннотацией @CSV
     * @throws RuntimeException если происходит ошибка ввода-вывода или доступа к полям
     */
    @Override
    public void writeToFile(List<?> data, String fileName) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            Class<?> clazz = data.get(0).getClass();
            if (!clazz.isAnnotationPresent(CSV.class)) {
                throw new IllegalArgumentException("класс невозможно преобразовать в CSV формат");
            }

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
            }

            writeHeaders(writer, fields);
            writeData(data, writer, fields);

        } catch (IOException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Записывает заголовки CSV (имена полей) в файл.
     * @param writer BufferedWriter для записи в файл
     * @param fields массив полей для записи в заголовок
     * @throws IOException если происходит ошибка ввода-вывода
     */
    private void writeHeaders(BufferedWriter writer, Field[] fields) throws IOException {

        List<String> headers = new ArrayList<>();
        for (Field field : fields) {
            headers.add(field.getName());
        }
        writer.write(String.join(",", headers));
        writer.newLine();

    }

    /**
     * Записывает данные объектов в CSV-формате.
     * @param data список объектов для записи
     * @param writer BufferedWriter для записи в файл
     * @param fields массив полей объекта
     * @throws IOException если происходит ошибка ввода-вывода
     * @throws IllegalAccessException если нет доступа к полям объекта
     */
    private void writeData(List<?> data, BufferedWriter writer, Field[] fields) throws IOException, IllegalAccessException {

        for (Object obj : data) {
            List<String> values = new ArrayList<>();
            for (Field field : fields) {
                if (field.isAnnotationPresent(CsvListField.class)) {
                    Collection<?> list = (Collection<?>) field.get(obj);
                    String listStr = list.stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(", "));
                    values.add("\"" + listStr + "\"");
                } else {
                    values.add(String.valueOf(field.get(obj)));
                }
            }
            writer.write(String.join(",", values));
            writer.newLine();
        }
    }

}





