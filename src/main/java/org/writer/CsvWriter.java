package org.writer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.writer.csv.CsvConverter;
import org.writer.csv.SimpleCsvConverter;
import org.writer.exception.WriteToFileException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.List;

/**
 * Class provides write object's data to CSV (Comma-Separated Values ) file.
 * Class supports the writing to csv file objects of POJO classes, strings, primitives and their boxed classes.
 * Attempting to pass empty collection or null to a parameter data or fileName causes an unchecked
 * {@link IllegalArgumentException}. Errors when writing to csv file cause {@link WriteToFileException}.
 *
 * @Author Alexei Shvariov
 * @Date 26.06.2025
 * @Version 1.0
 */

@AllArgsConstructor
@Slf4j
public class CsvWriter implements Writable {

    /**
     * Instance of CsvConverter for conversion different objects to string data for writing from csv file.
     * Object should be set in CsvWriter's constructor*/
    private CsvConverter<String> csvConverter;

    /**
     * Method for writing objects data to csv file.
     * @param data - collection of objects or strings or primitives or their boxed types.
     * @param fileName - path of file for writing csv data, include directory path, file name and extension.
     * @exception IllegalArgumentException trows if parameters data and filename is null or empty,
     * or if parameter fileName not contains file name or file extension.
     * @exception WriteToFileException throws when
     */
    @Override
    public void writeToFile(List<?> data, String fileName)  {
        if (csvConverter == null) {
            csvConverter = new SimpleCsvConverter();
        }
        checkFilePath(fileName);
        try (FileWriter writer = new FileWriter(fileName)){
            writer.write(csvConverter.toCsvFileData(data));
            log.info("CSV data successfully written to file {}", fileName);
        } catch (IOException ex) {
            final String errorMessage = "Error writing to a file: " + ex.getMessage();
            log.error(errorMessage);
            throw new WriteToFileException(errorMessage);
        }
    }

    /**
     * Checks that filePath not null or empty and contains directory path, file name and extension.
     * Method create filePath directories if not exists.
     * @param filePath - path of file for writing csv data, include directory path, file name and extension.
     * @throws IllegalArgumentException if filePath is null or empty, if file path not contains file name or extension.
     * */
    private void checkFilePath(String filePath) {
        checkFilePathNotNullOrNotEmpty(filePath);
        final int lastFileSeparatorIndex = filePath.lastIndexOf(FileSystems.getDefault().getSeparator());
        checkFilePathNotContainsDirectoryWithoutFileName(filePath, lastFileSeparatorIndex);
        if (lastFileSeparatorIndex >= 0) {
            createDirectoryIfNotExists(filePath, lastFileSeparatorIndex);
        }
        checkFileNameContainsNameAndExtension(filePath, lastFileSeparatorIndex);
    }

    /**Method create filePath directories if not exists.
     * @param filePath - path of file for writing csv data, include directory path, file name and extension.
     * @param lastFileSeparatorIndex - index of filePath string where last found FileSeparator char sequence.
     *                              Defines the location in filePath string where the path is separated from the file name.
     * */
    private void createDirectoryIfNotExists(String filePath, int lastFileSeparatorIndex) {
        final String directoryPath = filePath.substring(0, lastFileSeparatorIndex);
        final File directory = new File(directoryPath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                log.info("Created directory: {}", directory.getPath());
            }
        }
    }

    /**
     * Checks that filePath not null or not empty.
     * @param filePath - path of file for writing csv data, include directory path, file name and extension.
     * @throws IllegalArgumentException if filePath is null or empty.
     * */
    private void checkFilePathNotNullOrNotEmpty(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            final String errorMessage = "File name cannot be null or empty.";
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    /**
     * Checks that filePath contains only directory without file name and extension.
     * @param filePath - path of file for writing csv data, include directory path, file name and extension.
     * @param lastFileSeparatorIndex - index of filePath string where last found FileSeparator char sequence.
     *                              Defines the location in filePath string where the path is separated from the file name.
     * @throws IllegalArgumentException if file name and extension is empty.
     * */
    private void checkFilePathNotContainsDirectoryWithoutFileName(String filePath, int lastFileSeparatorIndex) {
        final String onlyFileName = filePath.substring(lastFileSeparatorIndex + 1);
        if (onlyFileName.isEmpty()) {
            String errorMessage = "File path cannot contains only directory.";
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    /**
     * Checks that filePath contains not empty filename and not empty extension.
     * @param filePath - path of file for writing csv data, include directory path, file name and extension.
     * @param lastFileSeparatorIndex - index of filePath string where last found FileSeparator char sequence.
     *                              Defines the location in filePath string where the path is separated from the file name.
     * @throws IllegalArgumentException if file name is empty or extension is empty.
     * */
    private void checkFileNameContainsNameAndExtension(String filePath, int lastFileSeparatorIndex) {
        final String onlyFileName = filePath.substring(lastFileSeparatorIndex + 1);
        final String[] fileNameParts = onlyFileName.split("\\.");
        if (fileNameParts.length == 1) {
            final String errorMessage = "File name must contain file extension";
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        if (fileNameParts[0].trim().isEmpty()) {
            final String errorMessage ="File name cannot contain only extension";
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }


}
