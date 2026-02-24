package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.ColumnCsv;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Student {

    @ColumnCsv(name = "имя")
    private String name;

    @ColumnCsv(name = "успеваемость")
    private List<String> score;
}