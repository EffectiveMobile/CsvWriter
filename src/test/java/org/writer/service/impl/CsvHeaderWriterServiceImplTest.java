package org.writer.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.writer.exception.CsvFileWriteException;
import org.writer.exception.handler.CsvErrorHandler;
import org.writer.model.Employee;
import org.writer.model.Person;
import org.writer.model.Student;
import org.writer.validation.validator.CsvFieldValidator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CsvHeaderWriterServiceImplTest {

    @Mock
    private BufferedWriter bufferedWriter;

    @Mock
    private CsvErrorHandler csvErrorHandler;

    @InjectMocks
    private CsvHeaderWriterServiceImpl csvHeaderWriterService;

    private MockedStatic<CsvFieldValidator> mockedStatic;

    @BeforeEach
    public void setUp() {
        Mockito.reset(bufferedWriter, csvErrorHandler);
        mockedStatic = Mockito.mockStatic(CsvFieldValidator.class);
    }

    @AfterEach
    public void tearDown() {
        mockedStatic.close();
    }

    @Test
    public void testWriteHeadersForEmployeeSuccess() throws IOException, NoSuchFieldException {
        List<Field> fields = getFields(Employee.class, "department", "salary");

        for (Field field : fields) {
            mockedStatic.when(() -> CsvFieldValidator.isValidField(eq(field)))
                    .thenReturn(true);

            mockedStatic.when(() -> CsvFieldValidator.getFieldHeader(eq(field)))
                    .thenReturn(field.getName());
        }

        csvHeaderWriterService.writeHeaders(fields);

        String expectedHeaderLine = "department,salary";

        verify(bufferedWriter).write(eq(expectedHeaderLine));
        verify(bufferedWriter).newLine();
    }

    @Test
    public void testWriteHeadersForPersonSuccess() throws IOException, NoSuchFieldException {
        List<Field> fields = getFields(Person.class, "firstName", "lastName", "dayOfBirth", "monthOfBirth",
                "yearOfBirth");

        for (Field field : fields) {
            mockedStatic.when(() -> CsvFieldValidator.isValidField(eq(field)))
                    .thenReturn(true);

            mockedStatic.when(() -> CsvFieldValidator.getFieldHeader(eq(field)))
                    .thenReturn(field.getName());
        }

        csvHeaderWriterService.writeHeaders(fields);

        String expectedHeaderLine = "firstName,lastName,dayOfBirth,monthOfBirth,yearOfBirth";

        verify(bufferedWriter).write(eq(expectedHeaderLine));
        verify(bufferedWriter).newLine();
    }

    @Test
    public void testWriteHeadersForStudentSuccess() throws IOException, NoSuchFieldException {
        List<Field> fields = getFields(Student.class, "name", "score");

        for (Field field : fields) {
            mockedStatic.when(() -> CsvFieldValidator.isValidField(eq(field)))
                    .thenReturn(true);

            mockedStatic.when(() -> CsvFieldValidator.getFieldHeader(eq(field)))
                    .thenReturn(field.getName());
        }

        csvHeaderWriterService.writeHeaders(fields);

        String expectedHeaderLine = "name,score";

        verify(bufferedWriter).write(eq(expectedHeaderLine));
        verify(bufferedWriter).newLine();
    }

    @Test
    public void testWriteHeadersWithIOException() throws IOException, NoSuchFieldException {
        List<Field> fields = getFields(Employee.class, "department", "salary");

        for (Field field : fields) {
            mockedStatic.when(() -> CsvFieldValidator.isValidField(eq(field)))
                    .thenReturn(true);

            mockedStatic.when(() -> CsvFieldValidator.getFieldHeader(eq(field)))
                    .thenReturn(field.getName());
        }

        String expectedHeaderLine = "department,salary";
        var testException = new IOException("Test exception");

        var mockException = mock(CsvFileWriteException.class);

        doThrow(testException).when(bufferedWriter)
                .write(eq(expectedHeaderLine));

        when(csvErrorHandler.handleError(
                eq("Error writing CSV headers"),
                eq(testException),
                eq(CsvFileWriteException.class)
        )).thenThrow(mockException);

        assertThrows(CsvFileWriteException.class, () -> csvHeaderWriterService.writeHeaders(fields));

        verify(csvErrorHandler).handleError(eq("Error writing CSV headers"), eq(testException),
                eq(CsvFileWriteException.class));
    }


    private List<Field> getFields(Class<?> clazz, String... fieldNames) throws NoSuchFieldException {
        List<Field> fields = new ArrayList<>();

        for (String fieldName : fieldNames) {
            fields.add(getFieldFromClassHierarchy(clazz, fieldName));
        }
        return fields;
    }

    private Field getFieldFromClassHierarchy(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> currentClass = clazz;

        while (currentClass != null) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        if (clazz != null) {
            throw new NoSuchFieldException(
                    "Field '" + fieldName + "' not found in class hierarchy of " + clazz.getName());
        } else {
            throw new NoSuchFieldException("Field '" + fieldName + "' not found and class is null");
        }
    }
}