package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotations.CSVColumn;
import org.writer.annotations.CSVReport;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@CSVReport
public class Student {

    @CSVColumn(columnName = "name")
    private String name;

    @CSVColumn(columnName = "score")
    private List<String> score;
}