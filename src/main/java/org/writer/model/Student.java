package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.Column;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Student {

    @Column(columnName = "Name", position = 1)
    private String name;

    @Column(columnName = "Score", position = 2)
    private List<String> score;
}