package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.CsvColumn;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Student {

    @CsvColumn(order = 0, name = "Name")
    private String name;

    @CsvColumn(order = 1, name = "Scores")
    private List<String> score;
}