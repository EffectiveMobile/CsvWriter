package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.Csv;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Student {

    @Csv(name = "name")
    private String name;

    @Csv(name = "score")
    private List<String> score;
}