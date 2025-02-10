package org.writer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.writer.model.Person;
import org.writer.util.DataGeneration;
import org.writer.util.FileCreation;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WritableServiceImplTest {

    private WritableServiceImpl writableService;
    private List<Person> personList;
    private static final String TEST_DIRECTORY = "src" + File.separator
            + "test" + File.separator
            + "java" + File.separator
            + "org" + File.separator
            + "writer" + File.separator
            + "resources" + File.separator
            + "test_out";

    @BeforeEach
    public void setUp() {
        writableService = new WritableServiceImpl(new FormatterLineServiceImpl());
        personList = DataGeneration.generatePerson(5);
        FileCreation.createFile(TEST_DIRECTORY);
    }

    @Test
    void writeDataToFileTest() {
        String fileName = TEST_DIRECTORY + File.separator + "test.csv";

        assertDoesNotThrow(() -> writableService.writeToFile(personList, fileName));

        File file = new File(fileName);
        assertTrue(file.exists());
        assertTrue(file.isFile());

        List<String> lines = assertDoesNotThrow(() -> Files.readAllLines(Paths.get(fileName)));
        assertFalse(lines.isEmpty());
        assertEquals(personList.size() + 1, lines.size());

        String expectedHeaders = String.join(",", "Firstname", "Lastname", "Day of birth", "Month of birth", "Year of birth");
        assertEquals(expectedHeaders, lines.get(0));
    }

    @Test
    void nullDataTest() {
        String fileName = TEST_DIRECTORY + File.separator + "test_null.csv";

        personList.get(0).setFirstName(null);
        personList.get(0).setMonthOfBirth(null);

        assertDoesNotThrow(() -> writableService.writeToFile(personList, fileName));

        File file = new File(fileName);
        assertTrue(file.exists());
        assertTrue(file.isFile());

        List<String> lines = assertDoesNotThrow(() -> Files.readAllLines(Paths.get(fileName)));
        assertFalse(lines.isEmpty());
        assertEquals(personList.size() + 1, lines.size());

        String expectedHeaders = String.join(",", "Firstname", "Lastname", "Day of birth", "Month of birth", "Year of birth");
        assertEquals(expectedHeaders, lines.get(0));
    }

    @Test
    void generatingManyData() {
        String fileName = TEST_DIRECTORY + File.separator + "many_data.csv";
        List<Person> largePersonList = DataGeneration.generatePerson(2000);

        assertDoesNotThrow(() -> writableService.writeToFile(largePersonList, fileName));

        File file = new File(fileName);
        assertTrue(file.exists());
        assertTrue(file.isFile());

        List<String> lines = assertDoesNotThrow(() -> Files.readAllLines(Paths.get(fileName)));
        assertEquals(largePersonList.size() + 1, lines.size());
    }

}
