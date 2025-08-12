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

    @CsvColumn(name = "Name", order = 1)
    private String name;

    @CsvColumn(name = "Score", order = 2)
    private List<String> score;
}