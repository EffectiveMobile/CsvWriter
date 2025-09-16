package org.writer.service.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.writer.service.Writable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WritableImplTest {

    private Writable writable;

    @TempDir
    private Path tempDir;

    @BeforeEach
    void setUp() {
        writable = new WritableImpl();
    }

    @Test
    @DisplayName("Writing data to a new file")
    void testWriteToFile_NewFile() throws IOException {
        String fileName = tempDir.resolve("text.csv").toString();
        List<String> data = List.of("Header", "Line1", "Line2", "Line3");

        writable.writeToFile(data, fileName);

        Path currFile = Path.of(fileName);
        assertTrue(Files.exists(currFile));
        List<String> content = Files.readAllLines(currFile);
        assertEquals(data, content);
    }

    @Test
    @DisplayName("A file with that name exists. Creating a new file an index")
    void testWriteToFile_ExistsFile_CreatesNewWithIndex() throws IOException {
        String fileName = tempDir.resolve("test.csv").toString();
        List<String> data1 = List.of("Original");
        List<String> data2 = List.of("New Content");

        Path originalFile = Path.of(fileName);
        Files.write(originalFile, data1);
        assertTrue(Files.exists(originalFile));

        writable.writeToFile(data2, fileName);

        Path newFile = Path.of(tempDir.toString(), "test(1).csv");
        assertTrue(Files.exists(newFile));
        List<String> content = Files.readAllLines(newFile);
        assertEquals(data2, content);

        List<String> originalContent = Files.readAllLines(originalFile);
        assertEquals(data1, originalContent);
    }

    @Test
    @DisplayName("File without extension")
    void testWriteToFile_WithoutExtension() throws Exception {
        String fileName = tempDir.resolve("test").toString();
        List<String> data = List.of("Content");

        writable.writeToFile(data, fileName);

        Path currFile = Path.of(fileName);
        assertTrue(Files.exists(currFile));
        List<String> content = Files.readAllLines(currFile);
        assertEquals(data, content);
    }

    @Test
    @DisplayName("Create parent directories")
    void testWriteToFile_CreateParentDirectories() throws IOException {
        String fileName = tempDir.resolve("subdir1/subdir2/test.csv").toString();
        List<String> data = List.of("Content");

        writable.writeToFile(data, fileName);

        Path currFile = Path.of(fileName);
        assertTrue(Files.exists(currFile));
        List<String> content = Files.readAllLines(currFile);
        assertEquals(data, content);
    }

    @Test
    @DisplayName("Multiple indexing files")
    void testWriteToFile_MultipleIndexedFiles() throws IOException {
        String fileName = tempDir.resolve("test.csv").toString();
        List<String> data = List.of("Content");

        for (int i = 0; i < 3; i++) {
            writable.writeToFile(data,fileName);
        }

        assertTrue(Files.exists(Path.of(fileName)));
        assertTrue(Files.exists(Path.of(tempDir.toString(), "test(1).csv")));
        assertTrue(Files.exists(Path.of(tempDir.toString(), "test(2).csv")));
    }
}
