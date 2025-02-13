package org.writer.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.writer.annotation.CsvFieldOrder;
import org.writer.validation.annotation.ValidCsvField;

/**
 * Abstract base class for CSV models.
 * Provides a common structure for all CSV entities, including a unique identifier.
 */
@Getter
@Setter
@ToString
@SuperBuilder
@EqualsAndHashCode
@NoArgsConstructor
public abstract class CsvModel {

    @ValidCsvField
    @CsvFieldOrder(1)
    private String id;
}
