package org.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Вспомогательный класс, реализующий интерфейс {@code Writable}.
 * <p>
 *     Этот класс предоставляет функциональность для записи списка объектов в файл в формате CSV.
 *     Объекты списка должны быть экземплярами классов, помеченных аннотацией {@code CSV}.
 * </p>
 *
 * <h4>Пример использования:</h4>
 * <pre>{@code
 * List<Person> list = List.of(
 *      new Person("Alex", "Smith", 2, Months.APRIL, 2001),
 *      new Person("Bob", "Cole", 15, Months.DECEMBER, 1994));
 * CsvWriter writer = new CsvWriter();
 * writer.writeToFile(list, "output.csv");
 * }</pre>
 *
 * @author Даниил Астафьев
 * @version 1.0
 * @see org.writer.Writable
 * @see CSV
 */
public class CsvWriter implements Writable {

    /**
     * Разделитель между значениями в сгенерированном CSV файле.
     */
    private static final String SEPARATOR = ",";
    /**
     * Знак прочерка для отсутствующих значений объекта в сгенерированном CSV файле.
     */
    private static final String DASH = "-";

    /**
     * Записывает список объектов в CSV файл.
     * <p>
     *     В первой строке файла будут содержаться заголовки, которые являются названиями полей.
     *     В каждой следующей строке CSV файла будут записаны данные каждого объекта.
     * </p>
     * @param data     список объектов, которые должны быть записаны в файл. Объекты списка должны быть экземплярами классов, помеченных аннотацией {@code CSV}.
     * @param fileName имя файла, в который должны быть записаны данные в формате CSV.
     * @throws RuntimeException если случается {@link IOException} при записи данных в файл или список содержит объекты,
     * принадлежащие классам без аннотации {@code CSV}.
     * @see CSV
     */
    @Override
    public void writeToFile(List<?> data, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            boolean isHeaderLineExist = false;
            String headersLine;
            for (Object object : data) {
                Class<?> clazz = object.getClass();
                if (clazz.isAnnotationPresent(CSV.class)) {
                    if (!isHeaderLineExist) {
                        headersLine = convertHeadersToCsv(object);
                        isHeaderLineExist = true;
                        writer.write(headersLine + "\n");
                    }
                    String csvLine = convertObjectToCsv(object);
                    writer.write(csvLine + "\n");
                } else {
                    throw new IOException("Passed class is not annotated by @CSV: " + clazz);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write CSV file: " + e.getMessage());
        }
    }

    /**
     * Преобразует данные объекта в строку CSV формата.
     * <p>
     *     С помощью Reflection API мы получаем доступ ко всем полям объекта, их значения заносим в строку и в качестве разделителя используем запятую.
     *     Если же какое-либо значение у объекта не было задано, то мы заменяем его на прочерк.
     *     Отдельно стоит отметить случай, когда одним из полей объекта является коллекция {@link List}.
     *     Для того, чтобы в качестве разделителей между элементами коллекции использовалась не запятая (базовый разделитель значений формата CSV),
     *     а пробел (для того, чтобы идентифицировать список значений как одно неделимое значение в файле, относящееся к конкретному столбцу), используется Reflection API
     *     и Stream API.
     * </p>
     * @param object Объект, значения которого приводятся к CSV формату.
     * @return Объект типа {@link String}, который представляет строку в формате CSV.
     * @throws RuntimeException если отказано в доступе к значению поля объекта.
     */
    private String convertObjectToCsv(Object object) {
        Class<?> clazz = object.getClass();
        StringBuilder line = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(object);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access to field value: " + e.getMessage());
            }

            if (value != null && isList(field)) {
                List<?> list = (List<?>) value;

                String result = list.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(" ", "[", "]"));

                if (!line.isEmpty()) {
                    line.append(SEPARATOR);
                }
                line.append(result);
                continue;
            }

            if (!line.isEmpty()) {
                line.append(SEPARATOR);
            }
            if (value != null) {
                line.append(value);
            } else {
                line.append(DASH);
            }
        }

        return line.toString();
    }

    /**
     * Преобразует названия полей объекта в строку CSV формата (используется в качестве названий столбцов в файле).
     * <p>
     *     С помощью Reflection API мы получаем доступ ко всем полям объекта, их наименования заносим в строку и в качестве разделителя используем запятую.
     * </p>
     * @param object Объект, названия полей которого приводятся к CSV формату.
     * @return Объект типа {@link String}, который представляет строку в формате CSV.
     */
    private String convertHeadersToCsv(Object object) {
        Class<?> clazz = object.getClass();
        StringBuilder line = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            line.append(fields[i].getName());
            if (i < fields.length - 1) {
                line.append(SEPARATOR);
            }
        }

        return line.toString();
    }

    /**
     * Проверяет, является ли тип поля классом {@link List}, используя функционал Reflection API.
     * @param field объект типа {@link Field}, представлящий поле класса.
     * @return значение {@code true}, если тип поля {@link List}(без учета параметризации). Иначе - {@code false}.
     */
    private boolean isList(Field field) {
        if (List.class.isAssignableFrom(field.getType())) {
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
                ParameterizedType pType = (ParameterizedType) genericType;

                return pType.getRawType() == List.class;
            }
        }
        return false;
    }
}
