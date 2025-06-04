package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.Column;

import java.util.List;

/**
 * Модель студента с оценками.
 * Содержит аннотации {@link Column} для экспорта в Csv.
 */
@Data
@Builder
@AllArgsConstructor
public class Student {

    /**
     * Полное имя студента
     */
    @Column(columnName = "Name", position = 1)
    private String name;

    /**
     * Список оценок студента.
     * При экспорте преобразуется в строку (например: "[A, B, C]")
     */
    @Column(columnName = "Score", position = 2)
    private List<String> score;
}