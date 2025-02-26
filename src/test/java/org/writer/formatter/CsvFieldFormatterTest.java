package org.writer.formatter;

import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.writer.exception.CsvDataException;
import org.writer.model.Employee;
import org.writer.util.data.EmployeeDataUtil;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CsvFieldFormatterTest {
    private static final Faker FAKER = new Faker();
    private CsvFieldFormatter csvFieldFormatter;

    @BeforeEach
    public void setUp() {
        csvFieldFormatter = new CsvFieldFormatter();
    }

    @ParameterizedTest
    @MethodSource("provideValidTestData")
    public void testFormatWithValidCases(Object value, String expected) {
        String result = csvFieldFormatter.format(value);

        if (value instanceof Set) {
            Set<String> expectedSet = new TreeSet<>(List.of(expected.split(";")));
            Set<String> actualSet = new TreeSet<>(List.of(result.split(";")));

            assertEquals(expectedSet, actualSet);
        } else {
            assertEquals(expected, result);
        }
    }

    @ParameterizedTest
    @MethodSource("provideInvalidTestData")
    public void testFormatWithInvalidCases(Object value) {
        assertThrows(CsvDataException.class, () -> csvFieldFormatter.format(value));
    }

    private static Stream<Arguments> provideValidTestData() {
        String randomWord = FAKER.lorem().word();
        int randomInt = FAKER.number().numberBetween(100, 999);
        double randomDouble = FAKER.number().randomDouble(2, 10, 999);

        List<String> randomWordsList = generateRandomWords();
        String expectedWordsList = String.join(";", randomWordsList);

        Set<String> randomWordsSet = new HashSet<>(generateRandomWords());
        String expectedWordsSet = String.join(";", randomWordsSet);

        List<Employee> employees = EmployeeDataUtil.getEmployees(3);

        String expectedEmployeesString = employees.stream()
                .map(Employee::toString)
                .collect(Collectors.joining(";"));

        return Stream.of(
                Arguments.of(randomWord, randomWord),
                Arguments.of(randomInt, String.valueOf(randomInt)),
                Arguments.of(randomDouble, String.valueOf(randomDouble)),
                Arguments.of(randomWordsList, expectedWordsList),
                Arguments.of(randomWordsSet, expectedWordsSet),
                Arguments.of(employees, expectedEmployeesString)
        );
    }

    private static Stream<Arguments> provideInvalidTestData() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(List.of()),
                Arguments.of(Set.of()),
                Arguments.of(new LinkedList<>()),
                Arguments.of((Object) new String[]{}),
                Arguments.of(new Object[]{new int[]{}})
        );
    }

    private static List<String> generateRandomWords() {
        return IntStream.range(0, 3)
                .mapToObj(i -> FAKER.lorem()
                        .word())
                .toList();
    }
}