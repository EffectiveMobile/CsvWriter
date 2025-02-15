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
import java.util.StringJoiner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

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
        List<Field> fields = getFields(Employee.class, "id", "department", "salary");
        mockedStatic.when(() -> CsvFieldValidator.isValidField(any(Field.class)))
                .thenReturn(true);

        csvHeaderWriterService.writeHeaders(fields);

        var expectedHeaderLine = new StringJoiner(",")
                .add("id")
                .add("department")
                .add("salary")
                .toString();
        verify(bufferedWriter).write(expectedHeaderLine);
        verify(bufferedWriter).newLine();
    }

    @Test
    public void testWriteHeadersForPersonSuccess() throws IOException, NoSuchFieldException {
        List<Field> fields = getFields(Person.class, "id", "firstName", "lastName", "dayOfBirth", "monthOfBirth",
                "yearOfBirth");
        mockedStatic.when(() -> CsvFieldValidator.isValidField(any(Field.class)))
                .thenReturn(true);

        csvHeaderWriterService.writeHeaders(fields);

        var expectedHeaderLine = new StringJoiner(",")
                .add("id")
                .add("firstName")
                .add("lastName")
                .add("dayOfBirth")
                .add("monthOfBirth")
                .add("yearOfBirth")
                .toString();
        verify(bufferedWriter).write(expectedHeaderLine);
        verify(bufferedWriter).newLine();
    }

    @Test
    public void testWriteHeadersForStudentSuccess() throws IOException, NoSuchFieldException {
        List<Field> fields = getFields(Student.class, "id", "name", "score");
        mockedStatic.when(() -> CsvFieldValidator.isValidField(any(Field.class)))
                .thenReturn(true);

        csvHeaderWriterService.writeHeaders(fields);

        var expectedHeaderLine = new StringJoiner(",")
                .add("id")
                .add("name")
                .add("score")
                .toString();
        verify(bufferedWriter).write(expectedHeaderLine);
        verify(bufferedWriter).newLine();
    }

    @Test
    public void testWriteHeadersWithIOException() throws IOException, NoSuchFieldException {
        List<Field> fields = getFields(Employee.class, "id", "department", "salary");
        mockedStatic.when(() -> CsvFieldValidator.isValidField(any(Field.class)))
                .thenReturn(true);
        doThrow(new IOException("Test exception")).when(bufferedWriter)
                .write(any(String.class));

        csvHeaderWriterService.writeHeaders(fields);

        verify(csvErrorHandler).handleError(any(String.class), any(IOException.class), eq(CsvFileWriteException.class));
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
            throw new NoSuchFieldException("Field '" + fieldName + "' not found in class hierarchy of " + clazz.getName());
        } else {
            throw new NoSuchFieldException("Field '" + fieldName + "' not found and class is null");
        }
    }
}