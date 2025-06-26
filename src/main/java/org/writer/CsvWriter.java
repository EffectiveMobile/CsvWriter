package org.writer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.writer.csv.CsvConverter;
import org.writer.exception.DirectoryNotFoundException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.List;

/**
 * @ClassName Conf
 * @Author Alexei Shvariov
 * @Date 26.06.2025
 * @Version 1.0
 */

@RequiredArgsConstructor
@Slf4j
public class CsvWriter implements Writable {
    private final CsvConverter csvConverter;

    @Override
    public void writeToFile(List<?> data, String fileName) {
        checkFileName(fileName);
        try (FileWriter writer = new FileWriter(fileName)){
            writer.write(csvConverter.toCsvString(data));
            log.info("Данные успешно записаны в файл {}", fileName);
        } catch (IOException ex) {
            throw new RuntimeException("Error writing to a file: " + ex.getMessage());
        }
    }

    private void checkFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new RuntimeException("File name cannot be null or empty.");
        }
        int lastFileSeparatorIndex = fileName.lastIndexOf(FileSystems.getDefault().getSeparator());

        String onlyFileName = fileName.substring(lastFileSeparatorIndex + 1);
        if (onlyFileName.isEmpty()) {
            throw new RuntimeException("File name cannot be null or empty.");
        }
        if (lastFileSeparatorIndex >= 0) {
            String directory = fileName.substring(0, lastFileSeparatorIndex);
            if (!new File(directory).exists()) {
                throw new DirectoryNotFoundException("Folder " + directory + "  was not found." +
                        " Please check csv files storage directory path");
            }
        }
    }
}
