package org.writer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.writer.csv.CsvConverter;
import org.writer.csv.SimpleCsvConverter;
import org.writer.exception.DirectoryNotFoundException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.List;

/**
 * @ClassName CsvWriter
 * @Author Alexei Shvariov
 * @Date 26.06.2025
 * @Version 1.0
 */

@AllArgsConstructor
@Slf4j
public class CsvWriter implements Writable {
    private CsvConverter csvConverter;

    @Override
    public void writeToFile(List<?> data, String fileName) {
        if (csvConverter == null) {
            csvConverter = new SimpleCsvConverter();
        }
        checkFileName(fileName);
        try (FileWriter writer = new FileWriter(fileName)){
            writer.write(csvConverter.toCsvString(data));
            log.info("CSV data successfully written to file {}", fileName);
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
