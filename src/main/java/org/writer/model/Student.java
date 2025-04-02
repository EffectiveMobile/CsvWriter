package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.Annotations.CsvField;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Student {
    @CsvField(name = "name")
    private String name;
    @CsvField(name = "score")
    private List<String> score;
}