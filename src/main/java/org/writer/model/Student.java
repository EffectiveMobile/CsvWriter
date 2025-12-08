package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.CsvColumn;

import java.util.List;

/**
 * Класс {@code Student} представляет модель студента,
 * данные которого могут быть сериализованы в CSV‑файл.
 * <p>
 * Для каждого поля используется аннотация {@link CsvColumn},
 * которая определяет название соответствующей колонки в CSV‑отчёте.
 */
@Data
@Builder
@AllArgsConstructor
public class Student {

    /**
     * Имя студента.
     */
    @CsvColumn(name = "Student name")
    private String name;

    /**
     * Список результатов (оценок) студента.
     */
    @CsvColumn(name = "Student score")
    private List<String> score;
}