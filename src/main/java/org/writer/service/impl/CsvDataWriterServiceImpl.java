package org.writer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.writer.exception.CsvDataException;
import org.writer.exception.CsvFileWriteException;
import org.writer.exception.CsvUnexpectedException;
import org.writer.exception.CsvWriterException;
import org.writer.exception.handler.CsvErrorHandler;
import org.writer.model.CsvModel;
import org.writer.service.CsvDataWriterService;
import org.writer.service.CsvWriterToFileService;
import org.writer.util.data.EmployeeDataUtil;
import org.writer.util.data.PersonDataUtil;
import org.writer.util.data.StudentDataUtil;

import java.util.List;

/**
 * Implementation of the {@link CsvDataWriterService} interface.
 * Provides methods to write specific types of data (people, students, employees) to CSV files.
 * Uses a {@link CsvWriterToFileService} to handle the actual file writing.
 */
@Slf4j
@RequiredArgsConstructor
public class CsvDataWriterServiceImpl implements CsvDataWriterService {
    private final CsvErrorHandler csvErrorHandler;
    private final CsvWriterToFileService csvWriterToFileService;

    /**
     * Writes people data to a CSV file.
     *
     * @param fileName the name of the file to write the data to.
     */
    @Override
    public void writePeopleToFile(String fileName) {
        List<CsvModel> people = PersonDataUtil.getPeople();
        writeToFile(people, fileName);
    }

    /**
     * Writes students data to a CSV file.
     *
     * @param fileName the name of the file to write the data to.
     */
    @Override
    public void writeStudentsToFile(String fileName) {
        List<CsvModel> students = StudentDataUtil.getStudents();
        writeToFile(students, fileName);
    }

    /**
     * Writes employees data to a CSV file.
     *
     * @param fileName the name of the file to write the data to.
     */
    @Override
    public void writeEmployeesToFile(String fileName) {
        List<CsvModel> employees = EmployeeDataUtil.getEmployees();
        writeToFile(employees, fileName);
    }

    /**
     * Helper method to write a list of data objects to a CSV file.
     * Handles exceptions and logs the result of the operation.
     *
     * @param data     the list of data objects to write.
     * @param fileName the name of the file to write the data to.
     */
    private void writeToFile(List<? extends CsvModel> data, String fileName) {
        try {
            csvWriterToFileService.writeToFile(data, fileName);
            log.info("CSV file generated successfully: {}", fileName);
        } catch (CsvDataException | CsvFileWriteException | CsvUnexpectedException ex) {
            csvErrorHandler.handleError("Error generating CSV file: " + fileName, ex, CsvWriterException.class);
        }
    }
}
