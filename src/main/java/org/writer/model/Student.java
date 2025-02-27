package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
/**
Модель студента
 */
@Data
@Builder
@AllArgsConstructor
public class Student {
    @CSVField(name = "name")
    private String name;
    @CSVField(name = "score")
    private List<String> score;
}