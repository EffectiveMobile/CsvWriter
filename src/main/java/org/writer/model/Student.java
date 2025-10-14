package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.CsvColumn;
import org.writer.annotation.CsvSerializable;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@CsvSerializable
public class Student {

    @CsvColumn(name = "Имя")
    private String name;

    @CsvColumn(name = "Оценки")
    private List<String> score;
}