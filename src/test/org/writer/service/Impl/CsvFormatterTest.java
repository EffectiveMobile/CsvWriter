package org.writer.service.Impl;

import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.service.Formatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class CsvFormatterTest {

    private Formatter formatter;
    private Faker faker;
    private final String HEADER = "first_name, last_name, date_of_birth, month_of_birth, year_of_birth";
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    @BeforeEach
    void setUp() {
        formatter = new CsvFormatter();
        faker = new Faker(new Locale("en"));
    }

    @Test
    @DisplayName("Formatting correct data in csv formatting")
    void testFormatting_WithValidPersonData() {
        List<Person> data = Arrays.asList(
                Person.builder()
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .dayOfBirth(faker.number().numberBetween(1, 31))
                        .monthOfBirth(faker.options().option(Months.class))
                        .yearOfBirth(faker.number().numberBetween(1965, 2025))
                        .build(),
                Person.builder()
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .dayOfBirth(faker.number().numberBetween(1, 31))
                        .monthOfBirth(faker.options().option(Months.class))
                        .yearOfBirth(faker.number().numberBetween(1965, 2025))
                        .build()
        );

        List<String> result = formatter.formatting(data);

        assertNotNull(result);
        assertEquals(3, result.size()); //Header + 2 rows
        assertEquals(HEADER, result.get(0));
        assertEquals(String.format("%s, %s, %d, %s, %d",
                        data.get(0).getFirstName(), data.get(0).getLastName(), data.get(0).getDayOfBirth(), data.get(0).getMonthOfBirth(), data.get(0).getYearOfBirth()),
                result.get(1));
        assertEquals(String.format("%s, %s, %d, %s, %d",
                        data.get(1).getFirstName(), data.get(1).getLastName(), data.get(1).getDayOfBirth(), data.get(1).getMonthOfBirth(), data.get(1).getYearOfBirth()),
                result.get(2));
    }

    @Test
    @DisplayName("The input data is null")
    void testFormatting_WithNullInput() {
        List<String> result = formatter.formatting(null);
        assertNull(result);
    }

    @Test
    @DisplayName("The input data is empty")
    void testFormatting_WithEmptyInput() {
        List<String> result = formatter.formatting(Collections.emptyList());
        assertNull(result);
    }

    @Test
    @DisplayName("Formatting data with special symbol")
    void testFormatting_WithSpecialCharacters() {
        List<Person> data = List.of(
                Person.builder()
                        .firstName("John, Doe")
                        .lastName("Smith \"Jr.\"")
                        .dayOfBirth(10)
                        .monthOfBirth(Months.DECEMBER)
                        .yearOfBirth(2000)
                        .build()
        );

        List<String> result = formatter.formatting(data);

        assertNotNull(result);
        assertEquals(2, result.size()); //Header + 1 row
        assertEquals(HEADER, result.get(0));
        assertEquals("\"John, Doe\", \"Smith \"\"Jr.\"\"\", 10, DECEMBER, 2000", result.get(1));
    }

    @Test
    @DisplayName("Formatting objects with empty fields")
    void setFormatting_WithNullFields() {
        List<Person> data = List.of(
                Person.builder()
                        .firstName(null)
                        .lastName(null)
                        .dayOfBirth(0)
                        .monthOfBirth(null)
                        .yearOfBirth(0)
                        .build()
        );

        List<String> result = formatter.formatting(data);

        assertNotNull(result);
        assertEquals(2, result.size()); //Header + 1 row
        assertEquals(HEADER, result.get(0));
        assertEquals(", , 0, , 0", result.get(1));
    }

    @Test
    @DisplayName("Formatting with a large data set")
    void testFormatting_WithLargeDataset() {
        List<Person> data = IntStream.range(0, 1000)
                .mapToObj(i -> {
                    LocalDate birthDate = LocalDate.parse(
                            faker.timeAndDate().birthday(1, 99, "YYYY/MM/dd"),
                            dateFormat);
                    return Person.builder()
                            .firstName(faker.name().firstName())
                            .lastName(faker.name().lastName())
                            .dayOfBirth(birthDate.getDayOfMonth())
                            .monthOfBirth(Months.values()[birthDate.getMonthValue() - 1])
                            .yearOfBirth(birthDate.getYear())
                            .build();
                })
                .toList();

        List<String> result = formatter.formatting(data);

        assertNotNull(result);
        assertEquals(1001, result.size()); //Header + 1000 row
        assertEquals(HEADER, result.get(0));

        Person samplePerson = data.get(0);
        String expectedRow = String.format("%s, %s, %d, %s, %d",
                samplePerson.getFirstName(),
                samplePerson.getLastName(),
                samplePerson.getDayOfBirth(),
                samplePerson.getMonthOfBirth(),
                samplePerson.getYearOfBirth());
        assertTrue(result.contains(expectedRow));
    }
}
