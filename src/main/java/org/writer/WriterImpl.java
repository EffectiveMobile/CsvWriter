package org.writer;

import org.writer.annotation.Column;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Реализация интерфейса для записи данных в Csv.
 * Использует аннотацию {@link Column} для определения структуры Csv.
 * Обрабатывает спецсимволы и сортирует столбцы по позиции.
 */
public class WriterImpl implements Writable {
    /**
     *Записывает список объектов в Csv файл, используя аннотации {@link Column}
     * для определения структуры.
     * Формирует заголовки столбцов на основе аннотаций и значений полей объектов.
     * @param data список объектов для записи (не может быть null или пустым).
     * @param fileName имя файла для записи, включая расширение .csv.
     * @throws IllegalArgumentException если data пуст или не содержит аннотированных полей.
     * @throws RuntimeException если произошла ошибка ввода-вывода или доступа через рефлексию.
     */
    @Override
    public void writeToFile(List<?> data, String fileName) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException
                    ("Data mustn't be empty");
        }

        Class<?> _class = data.get(0).getClass();

        List<Field> annotatedFields = getAnnotatedFields(_class);

        if (annotatedFields.isEmpty()) {
            throw new IllegalArgumentException
                    ("There is no annotated fields in class " + _class.getSimpleName());
        }

        annotatedFields.sort(Comparator.comparingInt(f -> f.getAnnotation(Column.class).position()));

        List<String> columnNames = annotatedFields
                .stream()
                .map(f -> {
                    String name = f.getAnnotation(Column.class).columnName();
                    return name == null || name.isBlank()
                            ? f.getName()
                            : name;
                })
                .toList();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

            writer.write(buildCsvLine(columnNames));
            writer.newLine();

            for (Object obj : data) {
                List<String> values = new ArrayList<>(annotatedFields.size());
                for (Field field : annotatedFields) {
                    field.setAccessible(true);
                    Object rawValue = field.get(obj);
                    String str = (rawValue == null) ? "" : rawValue.toString();
                    values.add(escapeCsv(str));
                }
                writer.write(buildCsvLine(values));
                writer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to write CSV in file " + fileName, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to get an access with reflection", e);
        }
    }

    /**
     *Возвращает список полей класса, аннотированных {@link Column}.
     * @param _class класс для анализа полей
     * @return список аннотированных полей (м/б пустым)
     */
    private List<Field> getAnnotatedFields(Class<?> _class) {
        Field[] allFields = _class.getDeclaredFields();
        List<Field> annotatedFields = new ArrayList<>();
        for (Field field : allFields) {
            if (field.isAnnotationPresent(Column.class)) {
                annotatedFields.add(field);
            }
        }

        return annotatedFields;
    }

    /**
     *Строит строку Csv из списка значений.
     * @param values список значений для объединения.
     * @return строка в формате Csv (значения, разделенные запятыми)
     */
    private String buildCsvLine(List<String> values) {
        return String.join(",", values);
    }

    /**
     * Экранирует значение для корректного отображения в Csv.
     * Оборачивается значение в кавычки, если оно содержит запятые, кавычки, переносы строк.
     * @param value исходное значение для экранирования.
     * @return экранированное значение, готовое для записи в Csv
     */
    private String escapeCsv(String value) {
        boolean needQuotes = value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r");
        if (needQuotes) {
            String doubled = value.replace("\"", "\"\"");
            return "\"" + doubled + "\"";
        } else {
            return value;
        }
    }
}
