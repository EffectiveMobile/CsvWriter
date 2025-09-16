package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.CsvField;

import java.util.List;

/**
 * Класс {@code Student} представляет модель данных студента, используемую для сериализации и десериализации
 * в CSV-файл. Содержит информацию об имени студента и списке его оценок.
 *
 * <p>
 * Класс использует аннотацию {@link org.writer.annotation.CsvField} для указания соответствия полей
 * столбцам CSV-файла, включая их имена и порядок. Поддерживает автоматическую генерацию геттеров, сеттеров,
 * методов {@code toString}, {@code equals} и {@code hashCode} благодаря аннотации {@link lombok.Data}.
 * Также предоставляет конструктор и паттерн Builder благодаря аннотациям {@link lombok.AllArgsConstructor}
 * и {@link lombok.Builder}.
 * </p>
 */
@Data
@Builder
@AllArgsConstructor
public class Student {

    /**
     * Имя студента. Соответствует столбцу "name" в CSV-файле с порядком 1.
     */
    @CsvField(columnName = "name", order = 1)
    private String name;

    /**
     * Список оценок студента. Соответствует столбцу "score" в CSV-файле с порядком 2.
     */
    @CsvField(columnName = "score", order = 2)
    private List<String> score;
}