package org.writer;

import lombok.extern.slf4j.Slf4j;
import org.writer.exception.CsvFileWriteException;
import org.writer.exception.handler.CsvErrorHandler;
import org.writer.formatter.CsvFieldFormatter;
import org.writer.service.CsvDataWriterService;
import org.writer.service.CsvHeaderWriterService;
import org.writer.service.CsvRowWriterService;
import org.writer.service.CsvWriterToFileService;
import org.writer.service.impl.CsvDataWriterServiceImpl;
import org.writer.service.impl.CsvHeaderWriterServiceImpl;
import org.writer.service.impl.CsvRowWriterServiceImpl;
import org.writer.service.impl.CsvWriterToFileServiceImpl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Entry point for the CSV writer application.
 * This application demonstrates the usage of {@link CsvDataWriterService} to generate CSV files
 * for different entity types, including people, students, and employees.
 */
@Slf4j
public class Main {

    /**
     * Main method to execute the CSV writing process.
     *
     * @param args command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        var errorHandler = new CsvErrorHandler();
        var fieldFormatter = new CsvFieldFormatter();

        writeDataToFile("people.csv", errorHandler, fieldFormatter, DataType.PEOPLE);
        writeDataToFile("students.csv", errorHandler, fieldFormatter, DataType.STUDENTS);
        writeDataToFile("employees.csv", errorHandler, fieldFormatter, DataType.EMPLOYEES);
    }

    /**
     * Writes data to a CSV file based on the specified data type.
     *
     * @param fileName       the name of the file to write the data to.
     * @param errorHandler   the error handler for CSV operations.
     * @param fieldFormatter the formatter for CSV field values.
     * @param dataType       the type of data to write (PEOPLE, STUDENTS, EMPLOYEES).
     */
    private static void writeDataToFile(String fileName, CsvErrorHandler errorHandler, CsvFieldFormatter fieldFormatter,
                                        DataType dataType) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

            CsvWriterToFileService csvWriterToFileService = createCsvWriterToFileService(writer, errorHandler,
                    fieldFormatter);
            CsvDataWriterService csvDataWriterService = new CsvDataWriterServiceImpl(errorHandler,
                    csvWriterToFileService);

            String result = switch (dataType) {
                case PEOPLE -> {
                    csvDataWriterService.writePeopleToFile(fileName);
                    yield "File: " + fileName + " written successfully";
                }
                case STUDENTS -> {
                    csvDataWriterService.writeStudentsToFile(fileName);
                    yield "File: " + fileName + " written successfully";
                }
                case EMPLOYEES -> {
                    csvDataWriterService.writeEmployeesToFile(fileName);
                    yield "File: " + fileName + " written successfully";
                }
            };
            log.info(result);

        } catch (IOException ex) {
            throw errorHandler.handleError("Failed to write data to file: " + fileName, ex,
                    CsvFileWriteException.class);
        }
    }

    /**
     * Creates an instance of CsvWriterToFileServiceImpl with the required dependencies.
     *
     * @param writer         the BufferedWriter for file operations.
     * @param errorHandler   the error handler for CSV operations.
     * @param fieldFormatter the formatter for CSV field values.
     * @return an instance of CsvWriterToFileServiceImpl.
     */
    private static CsvWriterToFileService createCsvWriterToFileService(BufferedWriter writer,
                                                                       CsvErrorHandler errorHandler,
                                                                       CsvFieldFormatter fieldFormatter) {
        CsvHeaderWriterService headerWriterService = new CsvHeaderWriterServiceImpl(writer, errorHandler);
        CsvRowWriterService rowWriterService = new CsvRowWriterServiceImpl(writer, errorHandler, fieldFormatter);
        return new CsvWriterToFileServiceImpl(errorHandler, rowWriterService, headerWriterService);
    }

    /**
     * Enum to represent the type of data being written to the CSV file.
     */
    private enum DataType {
        PEOPLE, STUDENTS, EMPLOYEES
    }
}