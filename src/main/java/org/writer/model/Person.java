package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotations.CsvInclude;
import org.writer.annotations.EnumMapping;
import org.writer.annotations.EnumMappingStrategy;

@Data
@Builder
@AllArgsConstructor
public class Person {

    @CsvInclude
    private String firstName;

    @CsvInclude
    private String lastName;

    @CsvInclude
    private int dayOfBirth;

    @CsvInclude(enumMapping = @EnumMapping(strategy = EnumMappingStrategy.NAME))
    private Months monthOfBirth;

    @CsvInclude
    private int yearOfBirth;

}
