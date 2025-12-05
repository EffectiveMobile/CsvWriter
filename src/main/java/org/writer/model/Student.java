package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotations.CsvField;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Student {
    @CsvField("Name")
    private String name;

    @CsvField("Score")
    private List<String> score;
}