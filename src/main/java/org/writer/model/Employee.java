package org.writer.model;

import org.writer.annotations.CSVColumn;
import org.writer.annotations.CSVReport;

import java.time.LocalDate;
import java.util.UUID;

@CSVReport
public record Employee(@CSVColumn(columnName = "id") UUID id,
                       @CSVColumn(columnName = "full name of the employee") String fullname,
                       @CSVColumn(columnName = "date of employment") LocalDate dateOfEmployment ) {
}
