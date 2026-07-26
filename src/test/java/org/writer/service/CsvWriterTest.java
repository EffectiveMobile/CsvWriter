package org.writer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.writer.model.Company;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

class CsvWriterTest {

  private final Faker faker = new Faker();
  private CsvWriter csvWriter;
  private Path csvPath;

  @BeforeEach
  void setUp(@TempDir Path tempDir) {
    this.csvWriter = new CsvWriter();
    this.csvPath = tempDir.resolve("file.csv");

  }

  @Test
  void writeToFile_EmptyFields() throws IOException {
    List<Student> students = List.of(
        new Student(null, List.of("A", "B")),
        new Student("Vladimir", null));

    List<Company> companies = List.of(
        new Company("123PL Limited", "Field View, Castledon Road, Downham, Billericay, CM11 1LH", null),
        new Company("2B&C Logistics Ltd", null, "08229231"));

    csvWriter.writeToFile(students, csvPath.toString());

    List<String> lines = Files.readAllLines(csvPath);

    assertEquals(3, lines.size());
    assertEquals(",A;B", lines.get(1));
    assertEquals("Vladimir,", lines.get(2));

    csvWriter.writeToFile(companies, csvPath.toString());

    lines = Files.readAllLines(csvPath);

    assertEquals(3, lines.size());
    assertEquals("123PL Limited,\"Field View, Castledon Road, Downham, Billericay, CM11 1LH\",", lines.get(1));
    assertEquals("2B&C Logistics Ltd,,08229231", lines.get(2));


  }

  @Test
  void writeToFile_ValidStructure() throws IOException {

    List<Student> students = List.of(
        new Student("Alexey", List.of("A", "B")),
        new Student("Vladimir", List.of("D", "A", "C", "B")),
        new Student("Daniil", Collections.emptyList()),
        new Student("Matvey", List.of("E")));

    csvWriter.writeToFile(students, csvPath.toString());

    List<String> lines = Files.readAllLines(csvPath);

    assertEquals(5, lines.size());
    assertEquals("Alexey,A;B", lines.get(1));
    assertEquals("Vladimir,D;A;C;B", lines.get(2));
    assertEquals("Daniil,", lines.get(3));
    assertEquals("Matvey,E", lines.get(4));

    List<Person> persons = List.of(
        new Person("First", "Last", 12, Months.APRIL, 1999),
        new Person("Name", "Test", 29, Months.MARCH, 2000),
        new Person("Some", "Person", 28, Months.FEBRUARY, 1956),
        new Person("John", "Doe", 1, Months.JANUARY, 1981)
    );

    csvWriter.writeToFile(persons, csvPath.toString());

    lines = Files.readAllLines(csvPath);

    assertEquals(5, lines.size());
    assertEquals("First,12,APRIL", lines.get(1));
    assertEquals("Name,29,MARCH", lines.get(2));
    assertEquals("Some,28,FEBRUARY", lines.get(3));
    assertEquals("John,1,JANUARY", lines.get(4));

    List<Company> companies = List.of(
        new Company("123PL Limited", "Field View, Castledon Road, Downham, Billericay, CM11 1LH", "07682974"),
        new Company("2B&C Logistics Ltd", "5 Copperhouse Court, Caldecotte, Milton Keynes, MK7 8NL", "08229231"),
        new Company("Benappi Fine Art Limited", "5th Floor 86 Jermyn Street, London, SW1Y 6AW", "08522336")
    );

    csvWriter.writeToFile(companies, csvPath.toString());

    lines = Files.readAllLines(csvPath);

    assertEquals(4, lines.size());
    assertEquals("123PL Limited,\"Field View, Castledon Road, Downham, Billericay, CM11 1LH\",07682974", lines.get(1));
    assertEquals("2B&C Logistics Ltd,\"5 Copperhouse Court, Caldecotte, Milton Keynes, MK7 8NL\",08229231", lines.get(2));
    assertEquals("Benappi Fine Art Limited,\"5th Floor 86 Jermyn Street, London, SW1Y 6AW\",08522336", lines.get(3));
  }
}