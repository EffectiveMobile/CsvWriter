package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import org.writer.annotation.CsvWritable;

@Data
@Builder
@AllArgsConstructor
@CsvWritable
public class Student {
    private String name;
    private List<String> score;
}