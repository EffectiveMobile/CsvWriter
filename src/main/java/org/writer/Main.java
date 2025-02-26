package org.writer;

import lombok.extern.slf4j.Slf4j;
import org.writer.exception.CsvFileWriteException;
import org.writer.exception.handler.CsvErrorHandler;
import org.writer.formatter.CsvFieldFormatter;
import org.writer.service.CsvHeaderWriterService;
import org.writer.service.CsvRowWriterService;
import org.writer.service.Writable;
import org.writer.service.impl.CsvHeaderWriterServiceImpl;
import org.writer.service.impl.CsvRowWriterServiceImpl;
import org.writer.service.impl.WritableServiceImpl;
import org.writer.util.data.EmployeeDataUtil;
import org.writer.util.data.PersonDataUtil;
import org.writer.util.data.StudentDataUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Entry point for the CSV file generation application.
 * This class generates sample data for people, students, and employees,
 * and writes it to separate CSV files using the {@link Writable} service.
 */
@Slf4j
public class Main {
    private static final String PEOPLE_FILE_NAME = "people.csv";
    private static final String STUDENTS_FILE_NAME = "students.csv";
    private static final String EMPLOYEES_FILE_NAME = "employees.csv";

    private static final int PEOPLE_COUNT = 4;
    private static final int STUDENTS_COUNT = 5;
    private static final int EMPLOYEES_COUNT = 3;


    /**
     * Entry point of the application.
     * Generates sample data and writes it to CSV files.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        log.info("CSV file generation started.");

        var errorHandler = new CsvErrorHandler();
        var fieldFormatter = new CsvFieldFormatter();

        try (BufferedWriter peopleWriter = new BufferedWriter(new FileWriter(PEOPLE_FILE_NAME));
             BufferedWriter studentsWriter = new BufferedWriter(new FileWriter(STUDENTS_FILE_NAME));
             BufferedWriter employeesWriter = new BufferedWriter(new FileWriter(EMPLOYEES_FILE_NAME))) {

            log.info("File writers initialized successfully.");

            var peopleFileWriter = createWritableService(peopleWriter, errorHandler, fieldFormatter);
            var studentsFileWriter = createWritableService(studentsWriter, errorHandler, fieldFormatter);
            var employeesFileWriter = createWritableService(employeesWriter, errorHandler, fieldFormatter);

            log.info("Writable services created.");

            peopleFileWriter
                    .writeToFile(PersonDataUtil.getPeople(PEOPLE_COUNT), PEOPLE_FILE_NAME);
            log.info("People data written to {}", PEOPLE_FILE_NAME);

            studentsFileWriter
                    .writeToFile(StudentDataUtil.getStudents(STUDENTS_COUNT), STUDENTS_FILE_NAME);
            log.info("Student data written to {}", STUDENTS_FILE_NAME);

            employeesFileWriter
                    .writeToFile(EmployeeDataUtil.getEmployees(EMPLOYEES_COUNT), EMPLOYEES_FILE_NAME);
            log.info("Employee data written to {}", EMPLOYEES_FILE_NAME);

        } catch (IOException ex) {
            throw errorHandler.handleError("Failed to write data to one of the files", ex, CsvFileWriteException.class);
        }
    }

    /**
     * Creates an instance of {@link WritableServiceImpl} with the necessary dependencies.
     *
     * @param bufferedWriter the writer used for writing data to the CSV file.
     * @param errorHandler   the error handler for handling CSV-related errors.
     * @param fieldFormatter the formatter for processing CSV field values.
     * @return an instance of {@link Writable} configured for CSV file writing.
     */
    private static Writable createWritableService(BufferedWriter bufferedWriter, CsvErrorHandler errorHandler,
                                                  CsvFieldFormatter fieldFormatter) {

        CsvHeaderWriterService headerWriter = new CsvHeaderWriterServiceImpl(bufferedWriter, errorHandler);
        CsvRowWriterService rowWriter = new CsvRowWriterServiceImpl(bufferedWriter, errorHandler, fieldFormatter);

        return new WritableServiceImpl(errorHandler, rowWriter, headerWriter);
    }
}