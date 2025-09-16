package org.writer.service.Impl;

import org.writer.service.Writable;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Класс {@code WritableImpl} реализует интерфейс {@link Writable} для записи данных в файл.
 * Поддерживает создание уникального имени файла, если файл с указанным именем уже существует,
 * и создание родительских директорий, если они отсутствуют.
 *
 * @since 1.0
 */
public class WritableImpl implements Writable {

    @Override
    public void writeToFile(List<?> data, String fileName) throws IOException {
        Path filePath = Paths.get(fileName);
        filePath = getUniqueFilePath(filePath);

        if (filePath.getParent() != null) {
            Files.createDirectories(filePath.getParent());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            for (var obj : data) {
                writer.write(obj.toString());
                writer.newLine();
            }
        }
    }

    /**
     * Генерирует уникальный путь к файлу, добавляя суффикс с числом, если файл с исходным именем
     * уже существует.
     *
     * @param originPath исходный путь к файлу
     * @return уникальный путь к файлу
     */
    private Path getUniqueFilePath(Path originPath) {
        if (!Files.exists(originPath)) {
            return originPath;
        }

        String fileName = originPath.getFileName().toString();
        String parent = originPath.getParent() != null ? originPath.getParent().toString() : "";
        String nameWithoutExtension;
        String extension = "";

        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            nameWithoutExtension = fileName.substring(0, lastDotIndex);
            extension = fileName.substring(lastDotIndex);
        } else {
            nameWithoutExtension = fileName;
        }

        int counter = 1;
        Path newPath;
        do {
            String newFileName = String.format("%s(%d)%s", nameWithoutExtension, counter++, extension);
            newPath = Paths.get(parent, newFileName);
        } while (Files.exists(newPath));

        return newPath;
    }
}
