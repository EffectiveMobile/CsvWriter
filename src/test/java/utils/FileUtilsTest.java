package utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.writer.utils.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileUtilsTest {

    @TempDir
    static Path tempDir;
    static List<String> teslList;

    @BeforeAll
    public static void beforeAll() {
        teslList = List.of("qwerty", "12345");
    }

    @Test
    @DisplayName("Write data to an existing file.")
    public void testWriteToExistFile() throws IOException {
        Path path = tempDir.resolve("exist.txt");
        Files.createFile(path);
        FileUtil.writeLinesToFile(path.toString(), teslList);

        List<String> result = Files.readAllLines(path);
        Assertions.assertEquals(teslList, result, "the data of the files must match.");
    }

    @Test
    @DisplayName("Create file if it does not exist.")
    public void testCreateFile() throws IOException {
        Path path = tempDir.resolve("notExist.txt");
        FileUtil.writeLinesToFile(path.toString(), teslList);

        boolean result = Files.exists(path);
        Assertions.assertTrue(result, "The file must be created.");
    }

    @Test
    @DisplayName("Write data to an not existing file.")
    public void testWriteToNotExistFile() throws IOException {
        Path path = tempDir.resolve("notExist.txt");
        FileUtil.writeLinesToFile(path.toString(), teslList);

        List<String> result = Files.readAllLines(path);
        Assertions.assertEquals(teslList, result, "the data of the files must match.");
    }

    @Test
    @DisplayName("Writing to file at wrong address.")
    public void testWriteToFileWrongAddress() {
        Assertions.assertThrows(IOException.class, ()-> FileUtil.writeLinesToFile(tempDir.toString(), teslList));
    }
}
