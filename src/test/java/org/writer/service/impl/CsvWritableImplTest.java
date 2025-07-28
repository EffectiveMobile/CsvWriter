package org.writer.service.impl; // Убедитесь, что пакет совпадает или тест имеет доступ


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.writer.model.Person;
import org.writer.model.Student;


import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.writer.service.impl.constants.ConstantsForCsvWritableImpl.DATA;
import static org.writer.service.impl.constants.ConstantsForCsvWritableImpl.DATA_WITH_LIST_FIELD;


class CsvWritableImplTest {

    private Method writeDataMethod;
    private CsvWritableImpl csvWriterInstance;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws Exception {

        Class<?> csvWriterClass = CsvWritableImpl.class;

        writeDataMethod = csvWriterClass.getDeclaredMethod("writeData",
                List.class, BufferedWriter.class, Field[].class);
        writeDataMethod.setAccessible(true);

        csvWriterInstance = new CsvWritableImpl();
    }


    @Test
    void testWriteDataWithListFields() throws Exception {

        File tempFile = tempDir.resolve("test.csv").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            Field[] fields = Student.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
            }
            writeDataMethod.invoke(csvWriterInstance, DATA_WITH_LIST_FIELD, writer, fields);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
            String line1 = reader.readLine();
            String line2 = reader.readLine();

            assertEquals("Oleg1,\"1, 2, 3, 4\"", line1);
            assertEquals("Oleg2,\"5, 6, 7, 8\"", line2);
        }
    }

    @Test
    void testWriteData() throws Exception {

        File tempFile = tempDir.resolve("test.csv").toFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            Field[] fields = Person.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
            }
            writeDataMethod.invoke(csvWriterInstance, DATA, writer, fields);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
            String line1 = reader.readLine();
            String line2 = reader.readLine();
            String line3 = reader.readLine();

            assertEquals("Karl1,Ivanov1,15,JANUARY,1999", line1);
            assertEquals("Karl2,Ivanov2,15,MARCH,2005", line2);
            assertEquals("Karl3,Ivanov3,15,APRIL,1957", line3);
        }
    }

}