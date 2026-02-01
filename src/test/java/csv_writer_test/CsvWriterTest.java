package csv_writer_test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.writer.model.CsvWriter;
import org.writer.model.Person;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;


public class CsvWriterTest {

    /**
     * Here we are testing if the objects are printed successfully
     * inside csv file
     * */
    @Test
    public void whenTypeOfFileCorrectWriteToCsvTest() throws IOException {
        Person annotatedPerson = new Person();
        CsvWriter csvWriter = new CsvWriter();
        csvWriter.writeToCsv(List.of(annotatedPerson), "Persons.csv");
        File file = new File("Persons.csv");
        assertTrue(file.exists());
    }


    /**
     * Here we are testing if the objects are not successfully printed
     * inside csv file if file type is wrong
     * */
    @Test
    public void throwWhenTypeOfFileIsInCorrectWriteToCsvTest(){
        Person annotatedPerson = new Person();
        CsvWriter csvWriter = new CsvWriter();
        assertThrows(IllegalArgumentException.class, () ->  csvWriter.writeToCsv(List.of(annotatedPerson), "Persons.txt"));
    }

}
