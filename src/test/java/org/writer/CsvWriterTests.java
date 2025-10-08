package org.writer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.datafaker.Faker;
import net.datafaker.providers.base.Options;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.writer.annotations.CollectionMapping;
import org.writer.annotations.CollectionMappingStrategy;
import org.writer.annotations.CsvInclude;
import org.writer.annotations.EnumMapping;
import org.writer.annotations.EnumMappingStrategy;
import org.writer.annotations.Escape;
import org.writer.annotations.EscapeStrategy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.IntStream;

public class CsvWriterTests {

    private static final String TEST_FILE_NAME = "testFileName.csv";

    private List<String> readCsv() {
        List<String> csvTable = new ArrayList<>();

        try (Scanner scanner = new Scanner(Paths.get(System.getProperty("user.dir"), TEST_FILE_NAME).toFile())) {
            while (scanner.hasNextLine()) {
                csvTable.add(scanner.nextLine());
            }
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error", e);
        }

        return csvTable;
    }

    @AfterEach
    public void afterEach() throws IOException {
        Path testFilePath = Paths.get(System.getProperty("user.dir"), TEST_FILE_NAME);
        Files.deleteIfExists(testFilePath);
    }

    @Test
    public void invalidArguments_emptyData_shouldThrowException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new CsvWriter().writeToFile(List.of(), "file"));
    }

    @Test
    public void invalidArguments_nullData_shouldThrowException() {
        Assertions.assertThrows(NullPointerException.class, () -> new CsvWriter().writeToFile(null, "file"));
    }

    @Test
    public void invalidArguments_blankFileName_shouldThrowException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new CsvWriter().writeToFile(List.of(new Object()), "    "));
    }

    @Test
    public void invalidArguments_nullFileName_shouldThrowException() {
        Assertions.assertThrows(NullPointerException.class, () -> new CsvWriter().writeToFile(List.of(new Object()), null));
    }

    @Test
    public void classWithFieldsOfPrimitiveTypes_defaultStrategies_shouldWriteToCsvFile() {

        @AllArgsConstructor
        @Getter
        class PrimitiveClass {
            @CsvInclude
            private int value;
            @CsvInclude
            private double anotherValue;
        }

        PrimitiveClass primitiveClass = new PrimitiveClass(1, 1.2);
        new CsvWriter().writeToFile(List.of(primitiveClass), TEST_FILE_NAME);

        List<String> csvTable = readCsv();

        Assertions.assertEquals("1,1.2", csvTable.get(0));
    }

    @Test
    public void classWithSimpleField_escapeStrategyQuoting_shouldWriteToCsvFile() {

        @AllArgsConstructor
        @Getter
        class SampleClass {
            @CsvInclude(escape = @Escape(strategy = EscapeStrategy.QUOTE_ESCAPING))
            private String value;
        }

        SampleClass sampleClass = new SampleClass("some_value");
        new CsvWriter().writeToFile(List.of(sampleClass), TEST_FILE_NAME);

        List<String> csvTable = readCsv();

        Assertions.assertEquals("\"some_value\"", csvTable.get(0));
    }

    @Test
    public void classWithEnumsValues_differentMappingStrategies_shouldWriteToCsvFile() {

        enum FirstEnum {
            FIRST_ENUM_VALUE
        }

        enum SecondEnum {
            SECOND_ENUM_VALUE
        }

        @AllArgsConstructor
        @Getter
        class SampleClass {
            @CsvInclude(enumMapping = @EnumMapping(strategy = EnumMappingStrategy.NAME))
            private FirstEnum firstEnum;
            @CsvInclude(enumMapping = @EnumMapping(strategy = EnumMappingStrategy.ORDINAL))
            private SecondEnum secondEnum;
        }

        SampleClass sampleClass = new SampleClass(FirstEnum.FIRST_ENUM_VALUE, SecondEnum.SECOND_ENUM_VALUE);
        new CsvWriter().writeToFile(List.of(sampleClass), TEST_FILE_NAME);

        List<String> csvTable = readCsv();

        Assertions.assertEquals("FIRST_ENUM_VALUE,0", csvTable.get(0));
    }

    @Test
    public void classWithObjectsValues_differentEscapeStrategies_shouldWriteToCsvFile() {

        @AllArgsConstructor
        class ColumnEscape {
            private int value1;
            private int value2;
            private int value3;

            @Override
            public String toString() {
                return value1 + "," + value2 + "," + value3;
            }
        }

        @AllArgsConstructor
        class QuoteEscape {
            private int value1;
            private int value2;

            @Override
            public String toString() {
                return value1 + "," + value2;
            }
        }

        @AllArgsConstructor
        @Getter
        class SampleClass {
            @CsvInclude(escape = @Escape(strategy = EscapeStrategy.COLUMN_ESCAPING))
            private ColumnEscape columnEscape;
            @CsvInclude(escape = @Escape(strategy = EscapeStrategy.QUOTE_ESCAPING))
            private QuoteEscape quoteEscape;
        }

        SampleClass sampleClass = new SampleClass(new ColumnEscape(1, 2, 3), new QuoteEscape(1, 2));
        new CsvWriter().writeToFile(List.of(sampleClass), TEST_FILE_NAME);

        List<String> csvTable = readCsv();

        Assertions.assertEquals("1,2,3,\"1,2\"", csvTable.get(0));
    }

    @Test
    public void classWithCollections_differentMappingStrategies_shouldWriteToCsvFile() {

        @AllArgsConstructor
        @Getter
        class SampleClass {
            @CsvInclude(collectionMapping = @CollectionMapping(strategy = CollectionMappingStrategy.ITEM_COLUMN))
            private List<String> list;
            @CsvInclude(collectionMapping = @CollectionMapping(strategy = CollectionMappingStrategy.STRINGIFY))
            private List<Integer> listOfInt;
        }

        SampleClass sampleClass = new SampleClass(List.of("1", "2", "some_value"), List.of(1, 2));
        new CsvWriter().writeToFile(List.of(sampleClass), TEST_FILE_NAME);

        List<String> csvTable = readCsv();

        Assertions.assertEquals("1,2,some_value,\"[1, 2]\"", csvTable.get(0));
    }

    @Test
    public void largeSetOfFakeData_allTypesOfFieldAndMappingStrategies_shouldWriteToCsvFile() {

        enum FakeEnum {
            FIRST,
            SECOND,
            THIRD
        }

        @AllArgsConstructor
        @Getter
        class FakeData {
            @CsvInclude
            private final String name;
            @CsvInclude(escape = @Escape(strategy = EscapeStrategy.QUOTE_ESCAPING))
            private final String surname;
            @CsvInclude(enumMapping = @EnumMapping(strategy = EnumMappingStrategy.NAME))
            private final FakeEnum fakeEnumFirst;
            @CsvInclude(enumMapping = @EnumMapping(strategy = EnumMappingStrategy.ORDINAL))
            private final FakeEnum fakeEnumSecond;
            @CsvInclude(collectionMapping = @CollectionMapping(strategy = CollectionMappingStrategy.ITEM_COLUMN))
            private final List<String> stringList;
            @CsvInclude(collectionMapping = @CollectionMapping(strategy = CollectionMappingStrategy.STRINGIFY))
            private final Collection<Integer> integerCollection;
        }

        Faker faker = new Faker();
        Options options = faker.options();
        List<FakeData> fakeData = IntStream.range(0, 100)
                .mapToObj(i -> new FakeData(
                        faker.name().firstName(), faker.name().lastName(),
                        options.option(FakeEnum.class), options.option(FakeEnum.class),
                        faker.collection(() -> faker.animal().name(), () -> faker.address().streetName()).generate(),
                        faker.collection(() -> faker.number().digit(), () -> faker.number().negative()).generate()
                )).toList();

        new CsvWriter().writeToFile(fakeData, TEST_FILE_NAME);
        List<String> csvTable = readCsv();

        for (int i = 0; i < 100; i++) {
            FakeData item = fakeData.get(i);
            Assertions.assertEquals(
                    String.format(
                            "%s,\"%s\",%s,%s,%s,\"%s\"",
                            item.getName(), item.getSurname(), item.getFakeEnumFirst(),
                            item.getFakeEnumSecond().ordinal(), String.join(",", item.getStringList()),
                            item.getIntegerCollection().toString()
                    ), csvTable.get(i)
            );
        }
    }

}
