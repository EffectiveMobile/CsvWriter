package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.CsvColumn;

import java.util.List;

/**
 *  Класс Student представляет модель чкловека
 *  <p> Благодаря аннотациям @CsvColumn, которые определяют названия столбцов,
 *  пользователь может быть записан в CSV файл.</p>
 */
@Data
@Builder
@AllArgsConstructor
public class Student {

    @CsvColumn(name = "Name")
    private String name;

    @CsvColumn(name = "Scores")
    private List<String> score;
}
