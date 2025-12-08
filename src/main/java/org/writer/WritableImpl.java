package org.writer;

import org.writer.annotation.CsvColumn;
import org.writer.exception.NoDataCSVException;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация интерфейса {@link Writable}, которая позволяет сериализовать список объектов
 * в CSV‑файл с использованием аннотации {@link CsvColumn}.
 * <p>
 * Алгоритм работы:
 * <ol>
 *   <li>Проверяется, что список данных не пустой, иначе выбрасывается {@link NoDataCSVException}.</li>
 *   <li>Через Reflection извлекаются все поля класса первого объекта.</li>
 *   <li>Формируется строка заголовков CSV на основе значений {@link CsvColumn#name()}.</li>
 *   <li>Для каждого объекта формируется строка с его значениями.</li>
 *   <li>Результат записывается в указанный файл.</li>
 * </ol>
 */
public class WritableImpl implements Writable {

    /**
     * Записывает список объектов в CSV‑файл.
     * <p>
     * Для сериализации учитываются только те поля, которые помечены аннотацией {@link CsvColumn}.
     * Если список пустой или равен {@code null}, выбрасывается {@link NoDataCSVException}.
     *
     * @param data     список объектов для записи
     * @param fileName имя файла, в который будет записан результат
     * @throws IOException           если произошла ошибка при записи в файл
     * @throws NoDataCSVException    если список {@code data} пустой или равен {@code null}
     */
    @Override
    public void writeToFile(List<?> data, String fileName) throws IOException {
        if (data == null || data.isEmpty()) throw new NoDataCSVException();

        Class<?> clazz = data.get(0).getClass();
        List<Field> fields = List.of(clazz.getDeclaredFields());

        String header = fields.stream()
                .filter(field -> field.isAnnotationPresent(CsvColumn.class))
                .map(field -> field.getAnnotation(CsvColumn.class).name())
                .collect(Collectors.joining(","));
        header += "\n";

        String rows = data.stream()
                .map(obj -> fields.stream()
                        .filter(field -> field.isAnnotationPresent(CsvColumn.class))
                        .map(field -> {
                            try {
                                field.setAccessible(true);
                                var value = field.get(obj);
                                return String.valueOf(value);
                            } catch (IllegalAccessException e) {
                                return "NO_ACCESS";
                            }
                        }).collect(Collectors.joining(","))
                ).collect(Collectors.joining("\n"));
        String csv = header + rows;

        try (var fileWriter = new FileWriter(fileName)) {
            fileWriter.write(csv);
        }
    }
}
