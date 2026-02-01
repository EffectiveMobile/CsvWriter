package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.datafaker.Faker;
import org.writer.annotations.CsvColumn;

@Data
@Builder
@AllArgsConstructor
public class Person {

    private static Faker faker = new Faker();

    @CsvColumn(name = "firstName")
    private String firstName;

    @CsvColumn(name = "lastName")
    private String lastName;

    @CsvColumn(name = "dateOfBirth")
    private int dayOfBirth;

    @CsvColumn(name = "monthOfBirth")
    private Months monthOfBirth;

    @CsvColumn(name = "yearOfBirth")
    private int yearOfBirth;

    public Person() {
        this.firstName = faker.name().firstName();
        this.lastName = faker.name().lastName();
        this.dayOfBirth = faker.number().numberBetween(1,31);
        this.monthOfBirth = faker.options().option(Months.class);
        this.yearOfBirth = faker.number().numberBetween(2005,1970);
    }


}