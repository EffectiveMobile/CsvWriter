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
import org.writer.util.CsvReflectionUtil;
import org.writer.util.data.EmployeeDataUtil;
import org.writer.util.data.PersonDataUtil;
import org.writer.util.data.StudentDataUtil;
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
        List<Field> fields = getFields(Employee.class, "department", "salary");
        var employee = EmployeeDataUtil.getEmployees(3).get(0);

        for (Field field : fields) {
            mockedStaticValidator.when(() -> CsvFieldValidator.isValidField(field))
                    .thenReturn(true);
            mockedStaticReflectionUtil.when(() -> CsvReflectionUtil.getFieldValue(employee, field))
                    .thenAnswer(invocation -> {
                        field.setAccessible(true);
                        return field.get(employee);
                    });
        }

        when(csvFieldFormatter.format(employee.getDepartment())).thenReturn(employee.getDepartment());
        when(csvFieldFormatter.format(employee.getSalary())).thenReturn(employee.getSalary().toString());

        csvRowWriterService.writeRow(fields, employee);

        String expectedRowLine = String.join(",", employee.getDepartment(), employee.getSalary().toString());
        verify(bufferedWriter).write(expectedRowLine);
        verify(bufferedWriter).newLine();
    }

    @Test
    public void testWriteRowForPersonSuccess() throws IOException, NoSuchFieldException {
        List<Field> fields = getFields(Person.class, "firstName", "lastName", "dayOfBirth", "monthOfBirth",
                "yearOfBirth");
        var person = PersonDataUtil.getPeople(4).get(0);

        for (Field field : fields) {
            mockedStaticValidator.when(() -> CsvFieldValidator.isValidField(field))
                    .thenReturn(true);
            mockedStaticReflectionUtil.when(() -> CsvReflectionUtil.getFieldValue(person, field))
                    .thenAnswer(invocation -> {
                        field.setAccessible(true);
                        return field.get(person);
                    });
        }

        when(csvFieldFormatter.format(person.getFirstName())).thenReturn(person.getFirstName());
        when(csvFieldFormatter.format(person.getLastName())).thenReturn(person.getLastName());
        when(csvFieldFormatter.format(person.getDayOfBirth())).thenReturn(String.valueOf(person.getDayOfBirth()));
        when(csvFieldFormatter.format(person.getMonthOfBirth())).thenReturn(person.getMonthOfBirth().toString());
        when(csvFieldFormatter.format(person.getYearOfBirth())).thenReturn(String.valueOf(person.getYearOfBirth()));

        csvRowWriterService.writeRow(fields, person);

        String expectedRowLine = String.join(",", person.getFirstName(), person.getLastName(),
                String.valueOf(person.getDayOfBirth()), person.getMonthOfBirth().toString(),
                String.valueOf(person.getYearOfBirth()));
        verify(bufferedWriter).write(expectedRowLine);
        verify(bufferedWriter).newLine();
    }

    @Test
    public void testWriteRowForStudentSuccess() throws IOException, NoSuchFieldException {
        List<Field> fields = getFields(Student.class, "name", "score");
        var student = StudentDataUtil.getStudents(5).get(0);

        for (Field field : fields) {
            mockedStaticValidator.when(() -> CsvFieldValidator.isValidField(field))
                    .thenReturn(true);
            mockedStaticReflectionUtil.when(() -> CsvReflectionUtil.getFieldValue(student, field))
                    .thenAnswer(invocation -> {
                        field.setAccessible(true);
                        return field.get(student);
                    });
        }

        when(csvFieldFormatter.format(student.getName())).thenReturn(student.getName());
        when(csvFieldFormatter.format(student.getScore())).thenReturn(student.getScore().toString());

        csvRowWriterService.writeRow(fields, student);

        String expectedRowLine = String.join(",", student.getName(), student.getScore().toString());
        verify(bufferedWriter).write(expectedRowLine);
        verify(bufferedWriter).newLine();
    }

    @Test
    public void testWriteRowWithIOException() throws IOException, NoSuchFieldException {
        List<Field> fields = getFields(Employee.class, "department", "salary");
        var employee = EmployeeDataUtil.getEmployees(3).get(0);

        for (Field field : fields) {
            mockedStaticValidator.when(() -> CsvFieldValidator.isValidField(field))
                    .thenReturn(true);
            mockedStaticReflectionUtil.when(() -> CsvReflectionUtil.getFieldValue(employee, field))
                    .thenAnswer(invocation -> {
                        field.setAccessible(true);
                        return field.get(employee);
                    });
        }

        when(csvFieldFormatter.format(employee.getDepartment())).thenReturn(employee.getDepartment());
        when(csvFieldFormatter.format(employee.getSalary())).thenReturn(employee.getSalary().toString());

        var testException = new IOException("Test exception");
        var mockCsvFileWriteException = mock(CsvFileWriteException.class);

        doThrow(testException).when(bufferedWriter)
                .write(employee.getDepartment() + "," + employee.getSalary());

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