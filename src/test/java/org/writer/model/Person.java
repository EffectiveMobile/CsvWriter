package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.csv.CsvRecord;
import org.writer.annotation.constans.NamingStrategy;

@Data
@Builder
@AllArgsConstructor
@CsvRecord(defaultNamingStrategy = NamingStrategy.AS_IS_TO_SPACE_SEPARATED_CAPITALIZED)
public class Person {

    private String firstName;
    private String lastName;
    private int dayOfBirth;
    private Months monthOfBirth;
    private int yearOfBirth;
}
