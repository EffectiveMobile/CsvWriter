package org.writer.writer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.writer.annotation.CsvColumn;
import org.writer.annotation.CsvEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CsvWriterImplTest {
    private Writable csvWriter;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        csvWriter = new CsvWriterImpl();
    }

    @Test
    @DisplayName("Должен выбросить исключение, если список data null")
    void writeToFile_shouldThrowException_whenDataIsNull() {
        // Arrange
        String fileName = tempDir.resolve("test.csv").toString();

        // Act & Assert
        assertThatThrownBy(() -> csvWriter.writeToFile(null, fileName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("data не должен быть isEmpty или null");
    }

    @Test
    @DisplayName("Должен выбросить исключение, если список data isEmpty")
    void writeToFile_shouldThrowException_whenDataIsEmpty() {
        // Arrange
        List<?> data = Collections.emptyList();
        String fileName = tempDir.resolve("test.csv").toString();

        // Act & Assert
        assertThatThrownBy(() -> csvWriter.writeToFile(data, fileName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("data не должен быть isEmpty или null");
    }

    @Test
    @DisplayName("Должен выбросить исключение, если строка fileName null")
    void writeToFile_shouldThrowException_whenFileNameIsNull() {
        // Arrange
        List<Integer> data = List.of(1);

        // Act & Assert
        assertThatThrownBy(() -> csvWriter.writeToFile(data, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("fileName не должен быть isBlank или null");
    }

    @Test
    @DisplayName("Должен выбросить исключение, если строка fileName isBlank")
    void writeToFile_shouldThrowException_whenFileNameIsBlank() {
        // Arrange
        List<Integer> data = List.of(1);
        String fileName = "   ";

        // Act & Assert
        assertThatThrownBy(() -> csvWriter.writeToFile(data, fileName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("fileName не должен быть isBlank или null");
    }

    @Test
    @DisplayName("Должен выбросить исключение, если класс объектов в списке data не имеет аннотации @CsvEntity")
    void writeToFile_shouldThrowException_whenClassIsNotCsvEntity() {
        // Arrange
        class InvalidModel {}
        List<InvalidModel> invalidData = List.of(new InvalidModel());
        String fileName = tempDir.resolve("test.csv").toString();

        // Act & Assert
        assertThatThrownBy(() -> csvWriter.writeToFile(invalidData, fileName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(InvalidModel.class.getSimpleName() + " не имеет аннотации @CsvEntity");
    }

    @Test
    @DisplayName("Должен выбросить исключение, если в @CsvColumn есть дублирующийся \"name\"")
    void writeToFile_shouldThrowException_whenCsvColumnHasDuplicateName() {
        // Arrange
        @CsvEntity
        @SuppressWarnings("unused")
        class DuplicateNameModel {
            @CsvColumn(name = "field", order = 1)
            private Object field1;

            @CsvColumn(name = "field", order = 2)
            private Object field2;
        }
        List<DuplicateNameModel> data = List.of(new DuplicateNameModel());
        String fileName = tempDir.resolve("test.csv").toString();

        // Act & Assert
        assertThatThrownBy(() -> csvWriter.writeToFile(data, fileName))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("В классе " + DuplicateNameModel.class.getSimpleName() + " найдены дублирующиеся значения \"name\" в аннотациях @CsvColumn");
    }

    @Test
    @DisplayName("Должен выбросить исключение, если в CsvColumn есть дублирующийся \"order\"")
    void writeToFile_shouldThrowException_whenCsvColumnHasDuplicateOrder() {
        // Arrange
        @CsvEntity
        @SuppressWarnings("unused")
        class DuplicateOrderModel {
            @CsvColumn(name = "field1", order = 1)
            private Object field1;

            @CsvColumn(name = "field2", order = 1)
            private Object field2;
        }
        List<DuplicateOrderModel> data = List.of(new DuplicateOrderModel());
        String fileName = tempDir.resolve("test.csv").toString();

        // Act & Assert
        assertThatThrownBy(() -> csvWriter.writeToFile(data, fileName))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("В классе " + DuplicateOrderModel.class.getSimpleName() + " найдены дублирующиеся значения \"order\" в аннотациях @CsvColumn");
    }

    @Test
    @DisplayName("Должен корректно записывать простой список объектов в CSV-файл")
    void writeToFile_shouldWriteSimpleObjectsCorrectly() throws IOException {
        // Arrange
        @Data
        @AllArgsConstructor
        @CsvEntity
        class SimpleModel {
            @CsvColumn(name = "User ID", order = 1)
            private int id;

            @CsvColumn(name = "User Name", order = 2)
            private String name;
        }
        List<SimpleModel> data = List.of(
                new SimpleModel(101, "John Doe"),
                new SimpleModel(202, "Jane Smith")
        );
        Path filePath = tempDir.resolve("test.csv");

        String expectedHeader = "User ID,User Name";
        String expectedRow1 = "101,John Doe";
        String expectedRow2 = "202,Jane Smith";

        // Act
        csvWriter.writeToFile(data, filePath.toString());

        // Assert
        assertThat(filePath).exists().isRegularFile();

        List<String> actualRows = Files.readAllLines(filePath);
        assertThat(actualRows)
                .hasSize(3)
                .containsExactly(
                        expectedHeader,
                        expectedRow1,
                        expectedRow2
                );
    }

    @Test
    @DisplayName("Должен корректно экранировать значение с запятой")
    void writeToFile_shouldEscapeValueWithComma() throws IOException {
        // Arrange
        @Data
        @AllArgsConstructor
        @CsvEntity
        class ModelWithCommaValue {
            @CsvColumn(name = "commaField", order = 1)
            private String commaField;
        }

        List<ModelWithCommaValue> data = List.of(
                new ModelWithCommaValue("this is a comma,value")
        );
        Path filePath = tempDir.resolve("test.csv");

        String expectedHeader = "commaField";
        String expectedRow = "\"this is a comma,value\"";

        // Act
        csvWriter.writeToFile(data, filePath.toString());

        // Assert
        assertThat(filePath).exists().isRegularFile();

        List<String> actualRows = Files.readAllLines(filePath);
        assertThat(actualRows)
                .hasSize(2)
                .containsExactly(
                        expectedHeader,
                        expectedRow
                );
    }

    @Test
    @DisplayName("Должен корректно экранировать значение с кавычками")
    void writeToFile_shouldEscapeValueWithQuotes() throws IOException {
        // Arrange
        @Data
        @AllArgsConstructor
        @CsvEntity
        class ModelWithQuotesValue {
            @CsvColumn(name = "quotesField", order = 1)
            private String quotesField;
        }

        List<ModelWithQuotesValue> data = List.of(
                new ModelWithQuotesValue("this is a \"quotes\" value")
        );
        Path filePath = tempDir.resolve("test.csv");

        String expectedHeader = "quotesField";
        String expectedRow = "\"this is a \"\"quotes\"\" value\"";

        // Act
        csvWriter.writeToFile(data, filePath.toString());

        // Assert
        assertThat(filePath).exists().isRegularFile();

        List<String> actualRows = Files.readAllLines(filePath);
        assertThat(actualRows)
                .hasSize(2)
                .containsExactly(
                        expectedHeader,
                        expectedRow
                );
    }

    @Test
    @DisplayName("Должен корректно экранировать значение с новой строкой")
    void writeToFile_shouldEscapeValueWithNewLine() throws IOException {
        // Arrange
        @Data
        @AllArgsConstructor
        @CsvEntity
        class ModelWithNewLineValue {
            @CsvColumn(name = "newLineField", order = 1)
            private String newLineField;
        }

        List<ModelWithNewLineValue> data = List.of(
                new ModelWithNewLineValue("this is a\nnew line value")
        );
        Path filePath = tempDir.resolve("test.csv");

        String expectedHeader = "newLineField";
        String expectedRow = "\"this is a\nnew line value\"";
        String expectedFileContent = expectedHeader + System.lineSeparator() + expectedRow + System.lineSeparator();

        // Act
        csvWriter.writeToFile(data, filePath.toString());

        // Assert
        assertThat(filePath).exists().isRegularFile();

        String actualFileContent = Files.readString(filePath);
        assertThat(actualFileContent).isEqualTo(expectedFileContent);
    }

    @Data
    @Builder
    @CsvEntity
    private static class ComplexModel {
        @CsvColumn(name = "nullField", order = 1)
        private Object nullField;

        @CsvColumn(name = "list", order = 2)
        private Collection<String> list;

        @CsvColumn(name = "emptyCollection", order = 3)
        private Collection<?> emptyCollection;

        @CsvColumn(name = "map", order = 4)
        private Map<String, String> map;

        @CsvColumn(name = "emptyMap", order = 5)
        private Map<?, ?> emptyMap;
    }

    private List<ComplexModel> getComplexModels() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        ComplexModel complexModel = ComplexModel.builder()
                .nullField(null)
                .list(List.of("listValue1", "listValue2"))
                .emptyCollection(Collections.emptySet())
                .map(map)
                .emptyMap(Collections.emptyMap())
                .build();

        return List.of(complexModel);
    }

    @Test
    @DisplayName("Должен корректно обрабатывать null, Collection и Map")
    void writeToFile_shouldCorrectlyHandleNullsCollectionsAndMaps() throws IOException {
        // Arrange
        List<ComplexModel> data = getComplexModels();
        Path filePath = tempDir.resolve("test.csv");

        String expectedHeader = "nullField,list,emptyCollection,map,emptyMap";
        String expectedRow = ",listValue1;listValue2,,key1:value1;key2:value2,";

        // Act
        csvWriter.writeToFile(data, filePath.toString());

        // Assert
        assertThat(filePath).exists().isRegularFile();

        List<String> actualRows = Files.readAllLines(filePath);
        assertThat(actualRows)
                .hasSize(2)
                .containsExactly(
                        expectedHeader,
                        expectedRow
                );
    }
}
