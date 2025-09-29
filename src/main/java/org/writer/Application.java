package org.writer;

import org.writer.model.Person;
import org.writer.model.Student;
import org.writer.util.TestDataGenerator;
import org.writer.writer.CsvWriterImpl;
import org.writer.writer.Writable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class Application {
    private static final String OUTPUT_DIR = "output";

    public static void main(String[] args) {
        System.out.println();

        createOutputDir();

        Writable csvWriter = new CsvWriterImpl();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

        // Person
        String personsTimestamp = LocalDateTime.now().format(formatter);
        String personsFileName = OUTPUT_DIR + "/persons-" + personsTimestamp + ".csv";
        int personsCount = TestDataGenerator.generateRandomInt(2, 100);

        System.out.println("Генерация случайных Person...");
        List<Person> persons = TestDataGenerator.generatePersons(personsCount);

        System.out.println("Запись " + personsCount + " объектов Person в файл " + personsFileName + "...");
        csvWriter.writeToFile(persons, personsFileName);
        System.out.println("Файл "+ personsFileName + " успешно создан");
        System.out.println("---------------------------------------------");

        // Student
        String studentsTimestamp = LocalDateTime.now().format(formatter);
        String studentsFileName = OUTPUT_DIR + "/students-" + studentsTimestamp + ".csv";
        int studentsCount = TestDataGenerator.generateRandomInt(2, 100);

        System.out.println("Генерация случайных Student...");
        List<Student> students = TestDataGenerator.generateStudents(studentsCount);

        System.out.println("Запись " + studentsCount + " объектов Student в файл " + studentsFileName + "...");
        csvWriter.writeToFile(students, studentsFileName);
        System.out.println("Файл "+ studentsFileName + " успешно создан");

        System.out.println();
    }

    /**
     * Создаёт директорию для вывода, если она ещё не существует.
     *
     * @throws RuntimeException если не удалось создать директорию.
     */
    private static void createOutputDir() {
        Path outputPath = Path.of(OUTPUT_DIR);
        if (!Files.exists(outputPath)) {
            try {
                Files.createDirectories(outputPath);
                System.out.println("Создана директория для вывода: " + OUTPUT_DIR);
                System.out.println();
            } catch (IOException e) {
                throw new RuntimeException("Не удалось создать директорию для вывода: " + OUTPUT_DIR, e);
            }
        }
    }
}