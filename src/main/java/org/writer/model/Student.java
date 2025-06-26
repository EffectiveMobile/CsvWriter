package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.Csv;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Csv
public class Student {

    private String name;

    private List<String> score;

}