package org.writer;

import org.junit.jupiter.api.Test;
import org.writer.model.Months;
import org.writer.model.Person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvWriterTest {

    @Test
    void testWriteCsv() throws IOException {
        List<Person> people = List.of(
                new Person("Max", "Orlov", 10, Months.JANUARY, 1999),
                new Person("Oleg", "Ivanov", 15, Months.MARCH, 1985)
        );

        Path tempFile = Files.createTempFile("test", ".csv");
        Writable writer = new CsvWriter();
        writer.writeToFile(people, tempFile.toString());

        String content = Files.readString(tempFile);

        assertTrue(content.contains("firstName,lastName,dayOfBirth,monthOfBirth,yearOfBirth"));
        assertTrue(content.contains("Max"));
        assertTrue(content.contains("Oleg"));
    }
}