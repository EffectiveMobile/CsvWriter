package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.writer.annotation.CsvFieldOrder;
import org.writer.model.enums.Months;
import org.writer.validation.annotation.ValidCsvField;

/**
 * Represents a person entity.
 * Extends {@link CsvModel} and includes personal details such as name and date of birth.
 */
@Data
@ToString(callSuper = true)
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Person extends CsvModel {

    @ValidCsvField
    @CsvFieldOrder(2)
    private String firstName;

    @ValidCsvField
    @CsvFieldOrder(3)
    private String lastName;

    @ValidCsvField
    @CsvFieldOrder(4)
    private int dayOfBirth;

    @ValidCsvField
    @CsvFieldOrder(5)
    private Months monthOfBirth;

    @ValidCsvField
    @CsvFieldOrder(6)
    private int yearOfBirth;
}
