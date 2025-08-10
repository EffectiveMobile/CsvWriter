package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.CsvField;

import java.util.List;

/**
 * A class that describes a student and their grades.
 * Serialized to CSV using the @CsvField annotation.
 */
@Data
@Builder
@AllArgsConstructor
public class Student {

    /**
     * Student name
     */
    @CsvField(name = "Name")
    private String name;

    /**
     * List of student grades
     */
    @CsvField(name = "Score")
    private List<String> score;
}
