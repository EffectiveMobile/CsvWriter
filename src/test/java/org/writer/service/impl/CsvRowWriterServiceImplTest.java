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
import org.writer.formatter.CsvFieldFormatter;
import org.writer.model.Employee;
import org.writer.model.Person;
import org.writer.model.Student;
import org.writer.model.enums.Months;
import org.writer.util.CsvReflectionUtil;
import org.writer.validation.validator.CsvFieldValidator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CsvRowWriterServiceImplTest {

    @Mock
    private BufferedWriter bufferedWriter;

    @Mock
    private CsvErrorHandler csvErrorHandler;

    @Mock
    private CsvFieldFormatter csvFieldFormatter;

    @InjectMocks
    private CsvRowWriterServiceImpl csvRowWriterService;

    private MockedStatic<CsvFieldValidator> mockedStaticValidator;
    private MockedStatic<CsvReflectionUtil> mockedStaticReflectionUtil;

    @BeforeEach
    public void setUp() {
        Mockito.reset(bufferedWriter, csvErrorHandler, csvFieldFormatter);
        mockedStaticValidator = Mockito.mockStatic(CsvFieldValidator.class);
        mockedStaticReflectionUtil = Mockito.mockStatic(CsvReflectionUtil.class);
    }

    @AfterEach
    public void tearDown() {
        mockedStaticValidator.close();
        mockedStaticReflectionUtil.close();
    }

    @Test
    public void testWriteRowForEmployeeSuccess() throws IOException, NoSuchFieldException {
        List<Field> fields = getFields(Employee.class, "id", "department", "salary");
        Employee employee = new Employee("HR", new BigDecimal("5000"));
        employee.setId("1");

        for (Field field : fields) {
            mockedStaticValidator.when(() -> CsvFieldValidator.isValidField(field))
                    .thenReturn(true);
            mockedStaticReflectionUtil.when(() -> CsvReflectionUtil.getFieldValue(employee, field))
                    .thenAnswer(invocation -> {
                        field.setAccessible(true);
                        return field.get(employee);
                    });
        }

        when(csvFieldFormatter.format("1")).thenReturn("1");
        when(csvFieldFormatter.format("HR")).thenReturn("HR");
        when(csvFieldFormatter.format(new BigDecimal("5000"))).thenReturn("5000");

        csvRowWriterService.writeRow(fields, employee);

        String expectedRowLine = String.join(",", "1", "HR", "5000");
        verify(bufferedWriter).write(expectedRowLine);
        verify(bufferedWriter).newLine();
    }

    @Test
    public void testWriteRowForPersonSuccess() throws IOException, NoSuchFieldException {
        List<Field> fields = getFields(Person.class, "id", "firstName", "lastName", "dayOfBirth", "monthOfBirth",
                "yearOfBirth");
        Person person = new Person("John", "Doe", 15, Months.JANUARY, 1990);
        person.setId("1");

        for (Field field : fields) {
            mockedStaticValidator.when(() -> CsvFieldValidator.isValidField(field))
                    .thenReturn(true);
            mockedStaticReflectionUtil.when(() -> CsvReflectionUtil.getFieldValue(person, field))
                    .thenAnswer(invocation -> {
                        field.setAccessible(true);
                        return field.get(person);
                    });
        }

        when(csvFieldFormatter.format("1")).thenReturn("1");
        when(csvFieldFormatter.format("John")).thenReturn("John");
        when(csvFieldFormatter.format("Doe")).thenReturn("Doe");
        when(csvFieldFormatter.format(15)).thenReturn("15");
        when(csvFieldFormatter.format(Months.JANUARY)).thenReturn("JANUARY");
        when(csvFieldFormatter.format(1990)).thenReturn("1990");

        csvRowWriterService.writeRow(fields, person);

        String expectedRowLine = String.join(",", "1", "John", "Doe", "15", "JANUARY", "1990");
        verify(bufferedWriter).write(expectedRowLine);
        verify(bufferedWriter).newLine();
    }

    @Test
    public void testWriteRowForStudentSuccess() throws IOException, NoSuchFieldException {
        List<Field> fields = getFields(Student.class, "id", "name", "score");
        Student student = new Student("Alice", List.of("90", "85"));
        student.setId("1");

        for (Field field : fields) {
            mockedStaticValidator.when(() -> CsvFieldValidator.isValidField(field))
                    .thenReturn(true);
            mockedStaticReflectionUtil.when(() -> CsvReflectionUtil.getFieldValue(student, field))
                    .thenAnswer(invocation -> {
                        field.setAccessible(true);
                        return field.get(student);
                    });
        }

        when(csvFieldFormatter.format("1")).thenReturn("1");
        when(csvFieldFormatter.format("Alice")).thenReturn("Alice");
        when(csvFieldFormatter.format(List.of("90", "85"))).thenReturn("[90, 85]");

        csvRowWriterService.writeRow(fields, student);

        String expectedRowLine = String.join(",", "1", "Alice", "[90, 85]");
        verify(bufferedWriter).write(expectedRowLine);
        verify(bufferedWriter).newLine();
    }

    @Test
    public void testWriteRowWithIOException() throws IOException, NoSuchFieldException {
        List<Field> fields = getFields(Employee.class, "id", "department", "salary");
        Employee employee = new Employee("HR", new BigDecimal("5000"));
        employee.setId("1");

        for (Field field : fields) {
            mockedStaticValidator.when(() -> CsvFieldValidator.isValidField(field))
                    .thenReturn(true);
            mockedStaticReflectionUtil.when(() -> CsvReflectionUtil.getFieldValue(employee, field))
                    .thenAnswer(invocation -> {
                        field.setAccessible(true);
                        return field.get(employee);
                    });
        }

        when(csvFieldFormatter.format("1")).thenReturn("1");
        when(csvFieldFormatter.format("HR")).thenReturn("HR");
        when(csvFieldFormatter.format(new BigDecimal("5000"))).thenReturn("5000");

        IOException testException = new IOException("Test exception");
        CsvFileWriteException mockCsvFileWriteException = mock(CsvFileWriteException.class);

        doThrow(testException).when(bufferedWriter)
                .write("1,HR,5000");

        doThrow(mockCsvFileWriteException).when(csvErrorHandler)
                .handleError(
                        eq("Error writing CSV row"),
                        eq(testException),
                        eq(CsvFileWriteException.class)
                );

        assertThrows(CsvFileWriteException.class, () -> csvRowWriterService.writeRow(fields, employee));

        verify(csvErrorHandler).handleError(
                eq("Error writing CSV row"),
                eq(testException),
                eq(CsvFileWriteException.class)
        );
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