package org.writer;

import org.writer.model.Person;
import org.writer.model.Student;
import org.writer.service.CsvFormatter;
import org.writer.service.CsvWriter;
import org.writer.service.Formatter;
import org.writer.util.DataGenerator;
import org.writer.util.FileUtil;

import java.io.File;
import java.util.List;

/**
 * Класс для генерации данных и записи их в CSV-файлы.
 * <p>
 * Создаёт и записывает данные о людях и студентах в отдельные CSV-файлы.
 */
public class Main {
    private static final String OUTPUT_DIRECTORY = "output";
    private static final String PERSONS_FILE = "persons.csv";
    private static final String STUDENTS_FILE = "students.csv";


    /**
     * Точка входа в приложение.
     * <p>
     * Генерирует данные для людей и студентов и записывает их в CSV-файлы.
     *
     */
    public static void main(String[] args) {

        Formatter formatter = new CsvFormatter();
        CsvWriter csvWriter = new CsvWriter(formatter);


        FileUtil.createDirectoryIfNotExists(OUTPUT_DIRECTORY);


        writePersonsToFile(csvWriter);
        writeStudentsToFile(csvWriter);
    }

    /**
     * Записывает данные о людях в CSV-файл.
     * <p>
     * Генерирует 10 объектов {@link Person} и записывает их в файл {@code persons.csv}.
     *
     * @param csvWriter сервис для записи данных в CSV.
     */
    private static void writePersonsToFile(CsvWriter csvWriter) {
        List<Person> persons = DataGenerator.generatePersons(10);
        String personsFilePath = OUTPUT_DIRECTORY + File.separator + PERSONS_FILE;
        csvWriter.writeToFile(persons, personsFilePath);
    }


    /**
     * Записывает данные о студентах в CSV-файл.
     * <p>
     * Генерирует 5 объектов {@link Student} и записывает их в файл {@code students.csv}.
     *
     * @param csvWriter сервис для записи данных в CSV.
     */
    private static void writeStudentsToFile(CsvWriter csvWriter) {
        List<Student> students = DataGenerator.generateStudents(5);
        String studentsFilePath = OUTPUT_DIRECTORY + File.separator + STUDENTS_FILE;
        csvWriter.writeToFile(students, studentsFilePath);
    }
}