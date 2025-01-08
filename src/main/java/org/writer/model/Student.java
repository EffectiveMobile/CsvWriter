package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.CsvField;
import org.writer.annotation.CsvWritable;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@CsvWritable
public class Student {

    @CsvField(header = "Full name")
    private String name;

    @CsvField(header = "List of scores")
    private List<String> score;
}