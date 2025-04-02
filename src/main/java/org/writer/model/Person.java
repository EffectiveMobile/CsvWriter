package org.writer.model;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.Annotations.CsvField;

@Data
@Builder
@AllArgsConstructor
public class Person {
    @CsvField(name = "firstName")
    private String firstName;
    @CsvField(name = "lastName")
    private String lastName;
    @CsvField(name = "dayOfBirth")
    private int dayOfBirth;
    @CsvField(name = "monthOfBirth")
    private Months monthOfBirth;
    @CsvField(name = "yearOfBirth")
    private int yearOfBirth;

}
