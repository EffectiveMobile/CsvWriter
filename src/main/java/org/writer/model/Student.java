package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.CsvField;

import java.util.List;

/**
 * Класс, представляющий студента с его именем и списком оценок,
 * с аннотациями для сериализации в CSV.
 */
@Data
@Builder
@AllArgsConstructor
public class Student {

    @CsvField(name = "Name")
    private String name;

    @CsvField(name = "Scores")
    private List<String> score;
}