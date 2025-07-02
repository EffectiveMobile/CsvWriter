package org.writer;

import org.writer.exception.IORuntimeException;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Singleton-класс для записи коллекции объектов в CSV-файл.
 * <p>
 * Использует Reflection и аннотацию {@link CsvField} для определения, какие поля
 * должны быть включены в CSV-файл.
 * </p>
 */
public class CsvWriter implements Writable {

    private static final String ERROR_ACCESS_FIELD_MSG = "Ошибка доступа к полю";
    private static final String IO_ERROR_MSG = "Ошибка записи в файл: ";
    private static final String ERROR_ENTITY_LIST_MSG = "Коллекция объектов пуста или null";
    private static final String COMMA = ",";
    private static final String EMPTY_STRING = "";
    private static final String LINE_BREAK = "\n";
    private static final String QUOTES = "\"";
    private static final String DOUBLE_QUOTES = "\"\"";

    private static CsvWriter INSTANCE;

    /**
     * Приватный конструктор для реализации Singleton.
     */
    private CsvWriter() {
    }

    /**
     * Возвращает единственный экземпляр CsvWriter.
     *
     * @return экземпляр CsvWriter
     */
    public static CsvWriter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CsvWriter();
        }
        return INSTANCE;
    }

    /**
     * Записывает список объектов в CSV-файл.
     *
     * @param data     список объектов
     * @param fileName имя файла
     * @throws IORuntimeException если произошла ошибка при записи
     */
    @Override
    public void writeToFile(List<?> data, String fileName) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException(ERROR_ENTITY_LIST_MSG);
        }

        Class<?> entityClass = data.iterator().next().getClass();
        List<Field> csvFields = extractCsvFields(entityClass);

        try (FileWriter writer = new FileWriter(fileName)) {
            writeHeaders(writer, csvFields);
            for (Object entity : data) {
                writeRow(writer, entity, csvFields);
            }
        } catch (IOException e) {
            throw new IORuntimeException(IO_ERROR_MSG + fileName);
        }
    }

    /**
     * Извлекает список полей класса, аннотированных {@link CsvField}.
     *
     * @param clazz класс объекта
     * @return список отражённых полей с аннотацией {@link CsvField}
     */
    private List<Field> extractCsvFields(Class<?> clazz) {
        return Stream.of(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(CsvField.class))
                .toList();
    }

    /**
     * Записывает заголовки CSV-файла.
     * <p>
     * Названия столбцов берутся из параметра {@code name()} аннотации {@link CsvField} или из названия поля если {@code name()} это пустая строка.
     * </p>
     *
     * @param writer {@link FileWriter}, используемый для записи
     * @param fields список полей, аннотированных {@link CsvField}
     * @throws IOException если произошла ошибка при записи
     */
    private void writeHeaders(FileWriter writer, List<Field> fields) throws IOException {
        String header = fields.stream()
                .map(f -> {
                            var headerName = f.getAnnotation(CsvField.class).name();
                            return !Objects.equals(headerName, EMPTY_STRING) ? headerName : f.getName();
                        }
                )
                .collect(Collectors.joining(COMMA));
        writer.write(header);
        writer.write(LINE_BREAK);
    }

    /**
     * Записывает одну строку объекта в формате CSV.
     *
     * @param writer {@link FileWriter}, используемый для записи
     * @param obj    объект, который нужно сериализовать
     * @param fields список полей, аннотированных {@link CsvField}
     * @throws IOException      если произошла ошибка при записи
     * @throws IORuntimeException если возникает {@link IllegalAccessException}
     */
    private void writeRow(FileWriter writer, Object obj, List<Field> fields) throws IOException {
        String row = fields.stream()
                .map(f -> {
                    f.setAccessible(true);
                    try {
                        Object val = f.get(obj);
                        return val != null ? escapeCsv(val.toString()) : EMPTY_STRING;
                    } catch (IllegalAccessException e) {
                        throw new IORuntimeException(ERROR_ACCESS_FIELD_MSG);
                    }
                })
                .collect(Collectors.joining(COMMA));
        writer.write(row);
        writer.write(LINE_BREAK);
    }

    /**
     * Экранирует значение, чтобы оно соответствовало CSV-формату.
     * <p>
     * Если строка содержит запятую, она берётся в кавычки.
     * </p>
     *
     * @param value значение для экранирования
     * @return безопасная для CSV строка
     */
    private String escapeCsv(String value) {
        if (value.contains(COMMA) || value.contains(QUOTES)) {
            value = value.replace(QUOTES, DOUBLE_QUOTES);
            return QUOTES + value + QUOTES;
        }
        return value;
    }
}
