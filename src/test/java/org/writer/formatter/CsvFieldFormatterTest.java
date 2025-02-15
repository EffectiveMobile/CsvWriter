package org.writer.formatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CsvFieldFormatterTest {
    private CsvFieldFormatter csvFieldFormatter;

    @BeforeEach
    public void setUp() {
        csvFieldFormatter = new CsvFieldFormatter();
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    public void testFormat(Object value, String expected) {
        String result = csvFieldFormatter.format(value);
        assertEquals(expected, result);
    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
                Arguments.of("test", "test"),
                Arguments.of(123, "123"),
                Arguments.of(new BigDecimal("123.45"), "123.45"),
                Arguments.of(List.of("apple", "banana", "cherry"), "apple;banana;cherry"),
                Arguments.of(List.of(), "")
        );
    }
}