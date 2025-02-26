package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.validation.annotation.ValidCsvField;

import java.util.List;

/**
 * Represents a student entity.
 * Includes student-specific details such as name and scores.
 */
@Data
@Builder
@AllArgsConstructor
public class Student {

    @ValidCsvField
    private String name;

    @ValidCsvField
    private List<String> score;
}