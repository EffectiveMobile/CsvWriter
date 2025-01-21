package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.CSV;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@CSV
public class Student {

    private String name;

    private List<String> score;
}