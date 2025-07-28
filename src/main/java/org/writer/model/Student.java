package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotations.CSV;
import org.writer.annotations.CsvListField;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@CSV
public class Student {

    private String name;

    @CsvListField
    private List<Integer> score;
}