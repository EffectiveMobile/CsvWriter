package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.writer.annotation.CsvFieldOrder;
import org.writer.validation.annotation.ValidCsvField;

import java.math.BigDecimal;

/**
 * Represents an employee entity.
 * Extends {@link CsvModel} and includes department and salary information.
 */
@Data
@ToString(callSuper = true)
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Employee extends CsvModel {

    @ValidCsvField
    @CsvFieldOrder(2)
    private String department;

    @ValidCsvField
    @CsvFieldOrder(3)
    private BigDecimal salary;
}
