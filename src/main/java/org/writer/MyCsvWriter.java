package org.writer;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Реализация класса для записи отчетов в csv файл.
 */
@RequiredArgsConstructor
public class MyCsvWriter implements Writable {

    private final CsvSerializer serializer;

    @Override
    public void writeToFile(List<?> data, String fileName) {
        validateFileName(fileName);

        try {
            String csvContent = serializer.serialize(data);

            Path path = Paths.get(fileName);

            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }

            Files.write(
                    path,
                    csvContent.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            );

            System.out.println("Successfully wrote " + data.size() + " items to " + fileName);

        } catch (IOException e) {
            throw new RuntimeException("Failed to write CSV file: " + fileName, e);
        }
    }

    /**
     * Проверяет имя файла
     */
    private void validateFileName(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("File name cannot be null");
        }
        if (fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be empty");
        }
    }
}
