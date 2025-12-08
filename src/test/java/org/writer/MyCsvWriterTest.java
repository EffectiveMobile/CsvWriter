package org.writer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("MyCsvWriter тесты")
class MyCsvWriterTest {

    @TempDir
    Path tempDir;

    private CsvSerializer mockSerializer;
    private MyCsvWriter writer;
    private Path testFilePath;
    private Person testPerson;
    private Student testStudent;

    @BeforeEach
    void setUp() {
        mockSerializer = mock(CsvSerializer.class);
        writer = new MyCsvWriter(mockSerializer);
        testFilePath = tempDir.resolve("test.csv");

        testPerson = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .monthOfBirth(Months.JANUARY)
                .yearOfBirth(1990)
                .build();

        testStudent = Student.builder()
                .name("Alice")
                .score(Arrays.asList("A", "B", "C"))
                .build();
    }

    @Test
    @DisplayName("Успешная запись Person в файл")
    void writeToFile_withPersons_writesToFile() throws IOException {
        when(mockSerializer.serialize(anyList())).thenReturn("First name;Last name\nJohn;Doe");

        writer.writeToFile(List.of(testPerson), testFilePath.toString());

        assertThat(testFilePath).exists();
        String content = Files.readString(testFilePath);
        assertThat(content).isEqualTo("First name;Last name\nJohn;Doe");
    }

    @Test
    @DisplayName("Создание директорий при необходимости")
    void writeToFile_withNonExistentDirectory_createsDirectories() throws IOException {
        when(mockSerializer.serialize(anyList())).thenReturn("content");

        Path nestedDir = tempDir.resolve("nested").resolve("deep");
        Path filePath = nestedDir.resolve("test.csv");

        writer.writeToFile(List.of(testPerson), filePath.toString());

        assertThat(nestedDir).exists();
        assertThat(filePath).exists();
    }

    @Test
    @DisplayName("Перезапись существующего файла")
    void writeToFile_existingFile_overwritesContent() throws IOException {
        when(mockSerializer.serialize(anyList())).thenReturn("New content");

        Files.writeString(testFilePath, "Old content");

        writer.writeToFile(List.of(testPerson), testFilePath.toString());

        String content = Files.readString(testFilePath);
        assertThat(content).isEqualTo("New content");
    }

    @Test
    @DisplayName("Запись пустого списка в файл")
    void writeToFile_emptyList_writesHeaderOnly() throws IOException {
        when(mockSerializer.serialize(anyList())).thenReturn("Header1;Header2");

        writer.writeToFile(List.of(), testFilePath.toString());

        String content = Files.readString(testFilePath);
        assertThat(content).isEqualTo("Header1;Header2");
    }

    @Test
    @DisplayName("Обработка неверного пути")
    void writeToFile_ioException_throwsRuntimeException() {
        when(mockSerializer.serialize(anyList())).thenReturn("content");

        String invalidPath = "WRONG_PATH://invalid/system/path/test.csv";

        assertThatThrownBy(() -> writer.writeToFile(List.of(testPerson), invalidPath))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to write CSV file");
    }

    @ParameterizedTest
    @ValueSource(strings = {"  ", "\t", "\n"})
    @NullAndEmptySource
    @DisplayName("Пустое имя файла выбрасывает исключение")
    void writeToFile_whitespaceFileName_throwsException(String fileName) {
        assertThatThrownBy(() -> writer.writeToFile(List.of(testPerson), fileName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("File name cannot ");
    }

    @Test
    @DisplayName("Запись Student с коллекциями в файл")
    void writeToFile_withStudents_writesToFile() throws IOException {
        CsvSerializer realSerializer = new CsvSerializer();
        MyCsvWriter realWriter = new MyCsvWriter(realSerializer);

        List<Student> students = List.of(testStudent);

        realWriter.writeToFile(students, testFilePath.toString());

        String content = Files.readString(testFilePath);
        assertThat(content).isEqualTo("Name;Score\nAlice;A,B,C");
    }
}