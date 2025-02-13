package org.writer;

import org.writer.service.CsvDataWriterService;
import org.writer.service.CsvWriterService;
import org.writer.service.impl.CsvDataWriterServiceImpl;
import org.writer.service.impl.CsvWriterServiceImpl;

/**
 * Entry point for the CSV writer application.
 * Demonstrates the usage of {@link CsvDataWriterService} to generate CSV files for people, students, and employees.
 */
public class Main {

    /**
     * Main method to execute the CSV writing process.
     *
     * @param args command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        CsvWriterService csvWriterService = new CsvWriterServiceImpl();
        CsvDataWriterService csvDataWriterService = new CsvDataWriterServiceImpl(csvWriterService);

        csvDataWriterService.writePeopleToFile("people.csv");
        csvDataWriterService.writeStudentsToFile("students.csv");
        csvDataWriterService.writeEmployeesToFile("employees.csv");
    }
}