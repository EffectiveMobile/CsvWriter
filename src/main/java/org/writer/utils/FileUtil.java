package org.writer.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Utility class for guaranteed writing of a list of data to a file.
 */
public class FileUtil {

    /**
     * Static method for writing a list of data to a file at a specified absolute address.
     * If the address or file does not exist, it will be created.
     * Truncates the file if it already exists.
     *
     * @param filePath Absolute address of the file to write to.
     * @param lines The data lines that will be written to the file.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    public static void writeLinesToFile(String filePath, Iterable<String> lines) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.notExists(path)){
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        }
        Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
