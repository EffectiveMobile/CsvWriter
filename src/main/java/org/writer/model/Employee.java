package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.validation.annotation.ValidCsvField;

import java.math.BigDecimal;

/**
 * Represents an employee entity.
 * Includes department and salary information.
 */
@Data
@Builder
@AllArgsConstructor
public class Employee {

    @ValidCsvField
    private String department;

    @ValidCsvField
    private BigDecimal salary;
}
