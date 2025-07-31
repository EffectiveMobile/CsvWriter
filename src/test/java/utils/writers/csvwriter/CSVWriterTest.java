package utils.writers.csvwriter;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.writer.annotations.CSVColumn;
import org.writer.annotations.CSVReport;
import org.writer.utils.writers.Writable;
import org.writer.utils.writers.csvwriter.CSVWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class CSVWriterTest {

    @TempDir
    static Path tempDir;

    static Writable<TestClassWithAnnotation> withAnnotationWritable;
    static Writable<TestClassWithoutAnnotation> withoutAnnotationWritable;

    static List<TestClassWithAnnotation> withAnnotationList;
    static List<TestClassWithoutAnnotation> withoutAnnotationList;

    @BeforeAll
    static void beforeAll() {
        withAnnotationWritable = new CSVWriter<>();
        withoutAnnotationWritable = new CSVWriter<>();
        withAnnotationList = List.of(
                new TestClassWithAnnotation("A", "B", "C"),
                new TestClassWithAnnotation("D", "E", "F")
        );
        withoutAnnotationList = List.of(
                new TestClassWithoutAnnotation("A", "B", "C"),
                new TestClassWithoutAnnotation("D", "E", "F")
        );
    }

    @Test
    @DisplayName("Write data to valid file path.")
    public void testWriteToValidPath() throws IOException {
        Path filePath = tempDir.resolve("valid_test.csv");
        withAnnotationWritable.writeToFile(withAnnotationList, filePath.toString());

        List<String> result = Files.readAllLines(filePath);
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals("column 1,column 2", result.get(0));
        Assertions.assertEquals("A,B", result.get(1));
        Assertions.assertEquals("D,E", result.get(2));
    }

    @Test
    @DisplayName("Write data with empty path.")
    public void testWriteWithEmptyPath() {
        String emptyPath = "";

        Assertions.assertThrows(NullPointerException.class, () ->
                withAnnotationWritable.writeToFile(withAnnotationList, emptyPath)
        );
    }

    @Test
    @DisplayName("Write data with path is null.")
    public void testWriteWithPathIsNull() {
        String nullPath = null;

        Assertions.assertThrows(NullPointerException.class, () ->
                withAnnotationWritable.writeToFile(withAnnotationList, nullPath)
        );
    }

    @Test
    @DisplayName("Write class without @CSVReport annotation.")
    public void testWriteClassWithoutAnnotation() {
        String path = tempDir.resolve("no_annotation.csv").toString();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                withoutAnnotationWritable.writeToFile(withoutAnnotationList, path)
        );
    }

    @Test
    @DisplayName("Write null data.")
    public void testWriteNullData() {
        String path = tempDir.resolve("null_data.csv").toString();
        List<TestClassWithAnnotation> nullData = null;

        Assertions.assertThrows(NullPointerException.class, () ->
                withAnnotationWritable.writeToFile(nullData, path)
        );
    }

    @Test
    @DisplayName("Write empty data.")
    public void testWriteEmptyData() {
        String path = tempDir.resolve("empty_data.csv").toString();
        List<TestClassWithAnnotation> emptyData = Collections.emptyList();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                withAnnotationWritable.writeToFile(emptyData, path)
        );
    }

    @Test
    @DisplayName("Check file format with correct filename.")
    public void testCorrectFilenameFormat() throws IOException {
        String path = tempDir.resolve("correct_name.csv").toString();

        withAnnotationWritable.writeToFile(withAnnotationList, path);

        Assertions.assertTrue(Paths.get(path).endsWith("correct_name.csv"));
    }

    @Test
    @DisplayName("Check file handling with incorrect filename.")
    public void testIncorrectFilenameFormat() throws IOException {
        String path = tempDir.resolve("incorrect_name").toString();

        withAnnotationWritable.writeToFile(withAnnotationList, path);

        Assertions.assertTrue(Files.notExists(Paths.get(path)));
        Assertions.assertTrue(Files.exists(Paths.get(path + ".csv")));
    }

    @CSVReport
    record TestClassWithAnnotation(
            @CSVColumn(columnName = "column 1") String column,
            @CSVColumn(columnName = "column 2") String column2,
            String column3
    ) {
    }

    record TestClassWithoutAnnotation(
            @CSVColumn(columnName = "column 1") String column,
            @CSVColumn(columnName = "column 2") String column2,
            String column3
    ) {
    }
}
