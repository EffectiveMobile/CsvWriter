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
import org.writer.model.CsvModel;
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
import java.util.StringJoiner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
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

        mockedStaticValidator.when(() -> CsvFieldValidator.isValidField(any(Field.class)))
                .thenReturn(true);
        mockedStaticReflectionUtil.when(() -> CsvReflectionUtil.getFieldValue(any(CsvModel.class), any(Field.class)))
                .thenAnswer(invocation -> {
                    Field field = invocation.getArgument(1);
                    field.setAccessible(true);
                    return field.get(employee);
                });
        when(csvFieldFormatter.format(any())).thenAnswer(invocation -> {
            var argument = invocation.getArgument(0);
            return argument != null ?
                    argument.toString() :
                    "";
        });

        csvRowWriterService.writeRow(fields, employee);

        var expectedRowLine = new StringJoiner(",")
                .add("1")
                .add("HR")
                .add("5000")
                .toString();
        verify(bufferedWriter).write(expectedRowLine);
        verify(bufferedWriter).newLine();
    }

    @Test
    public void testWriteRowForPersonSuccess() throws IOException, NoSuchFieldException {
        List<Field> fields = getFields(Person.class, "id", "firstName", "lastName", "dayOfBirth", "monthOfBirth",
                "yearOfBirth");
        Person person = new Person("John", "Doe", 15, Months.JANUARY, 1990);
        person.setId("1");

        mockedStaticValidator.when(() -> CsvFieldValidator.isValidField(any(Field.class)))
                .thenReturn(true);
        mockedStaticReflectionUtil.when(() -> CsvReflectionUtil.getFieldValue(any(CsvModel.class), any(Field.class)))
                .thenAnswer(invocation -> {
                    Field field = invocation.getArgument(1);
                    field.setAccessible(true);
                    return field.get(person);
                });
        when(csvFieldFormatter.format(any())).thenAnswer(invocation -> {
            var argument = invocation.getArgument(0);
            return argument != null ?
                    argument.toString() :
                    "";
        });

        csvRowWriterService.writeRow(fields, person);

        var expectedRowLine = new StringJoiner(",")
                .add("1")
                .add("John")
                .add("Doe")
                .add("15")
                .add("JANUARY")
                .add("1990")
                .toString();
        verify(bufferedWriter).write(expectedRowLine);
        verify(bufferedWriter).newLine();
    }

    @Test
    public void testWriteRowForStudentSuccess() throws IOException, NoSuchFieldException {
        List<Field> fields = getFields(Student.class, "id", "name", "score");
        Student student = new Student("Alice", List.of("90", "85"));
        student.setId("1");

        mockedStaticValidator.when(() -> CsvFieldValidator.isValidField(any(Field.class)))
                .thenReturn(true);
        mockedStaticReflectionUtil.when(() -> CsvReflectionUtil.getFieldValue(any(CsvModel.class), any(Field.class)))
                .thenAnswer(invocation -> {
                    Field field = invocation.getArgument(1);
                    field.setAccessible(true);
                    return field.get(student);
                });
        when(csvFieldFormatter.format(any())).thenAnswer(invocation -> {
            var argument = invocation.getArgument(0);
            return argument != null ?
                    argument.toString() :
                    "";
        });

        csvRowWriterService.writeRow(fields, student);

        var expectedRowLine = new StringJoiner(",")
                .add("1")
                .add("Alice")
                .add("[90, 85]")
                .toString();
        verify(bufferedWriter).write(expectedRowLine);
        verify(bufferedWriter).newLine();
    }

    @Test
    public void testWriteRowWithIOException() throws IOException, NoSuchFieldException {
        List<Field> fields = getFields(Employee.class, "id", "department", "salary");
        Employee employee = new Employee("HR", new BigDecimal("5000"));
        employee.setId("1");

        mockedStaticValidator.when(() -> CsvFieldValidator.isValidField(any(Field.class)))
                .thenReturn(true);
        mockedStaticReflectionUtil.when(() -> CsvReflectionUtil.getFieldValue(any(CsvModel.class), any(Field.class)))
                .thenAnswer(invocation -> {
                    Field field = invocation.getArgument(1);
                    field.setAccessible(true);
                    return field.get(employee);
                });
        when(csvFieldFormatter.format(any())).thenAnswer(invocation -> {
            var argument = invocation.getArgument(0);
            return argument != null ?
                    argument.toString() :
                    "";
        });

        doThrow(new IOException("Test exception")).when(bufferedWriter)
                .write(any(String.class));

        csvRowWriterService.writeRow(fields, employee);

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