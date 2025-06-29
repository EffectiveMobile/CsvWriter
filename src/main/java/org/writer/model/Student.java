package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.CsvField;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Student {

    @CsvField(name = "Name")
    private String name;

    @CsvField(name = "Score")
    private List<String> score;
}