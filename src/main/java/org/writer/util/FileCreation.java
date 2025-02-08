package org.writer.util;

import org.writer.exception.DirectoryCreationException;

import java.io.File;

/**
 * Класс для создания директории с выходными файлами.
 */
public class FileCreation {

    /**
     * Создание новой директории, если ее не существует.
     *
     * @param directory - название директории.
     * @throws DirectoryCreationException - ошибка при создании директории.
     */
    public static void createFile(String directory) {
        File dir = new File(directory);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new DirectoryCreationException("Failed to create directory: " + directory);
            }
        }
    }
}
