package org.writer.service;

/**
 * Service for writing specific types of data (people, students, employees) to CSV files.
 * Extends the functionality of {@link CsvWriterToFileService} for specialized use cases.
 */
public interface CsvDataWriterService {

    /**
     * Writes people data to a CSV file.
     *
     * @param fileName the name of the file to write the data to.
     */
    void writePeopleToFile(String fileName);

    /**
     * Writes students data to a CSV file.
     *
     * @param fileName the name of the file to write the data to.
     */
    void writeStudentsToFile(String fileName);

    /**
     * Writes employees data to a CSV file.
     *
     * @param fileName the name of the file to write the data to.
     */
    void writeEmployeesToFile(String fileName);
}
