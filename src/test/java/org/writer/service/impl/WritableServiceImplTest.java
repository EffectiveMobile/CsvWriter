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
import org.writer.exception.CsvDataException;
import org.writer.exception.CsvUnexpectedException;
import org.writer.exception.handler.CsvErrorHandler;
import org.writer.model.Employee;
import org.writer.service.CsvHeaderWriterService;
import org.writer.service.CsvRowWriterService;
import org.writer.util.CsvReflectionUtil;
import org.writer.util.data.EmployeeDataUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WritableServiceImplTest {

    @Mock
    private CsvErrorHandler csvErrorHandler;

    @Mock
    private CsvRowWriterService csvRowWriterService;

    @Mock
    private CsvHeaderWriterService csvHeaderWriterService;

    @InjectMocks
    private WritableServiceImpl csvWriterToFileService;

    private MockedStatic<CsvReflectionUtil> mockedStaticReflectionUtil;

    @BeforeEach
    public void setUp() {
        mockedStaticReflectionUtil = Mockito.mockStatic(CsvReflectionUtil.class);
    }

    @AfterEach
    public void tearDown() {
        mockedStaticReflectionUtil.close();
    }

    @Test
    public void testWriteToFileSuccess() throws NoSuchFieldException {
        Employee firstRandomEmployee = EmployeeDataUtil.getEmployees(4).get(0);
        Employee secondRandomEmployee = EmployeeDataUtil.getEmployees(4).get(1);

        List<Employee> employees = List.of(firstRandomEmployee, secondRandomEmployee);
        List<Field> fields = List.of(
                Employee.class.getDeclaredField("department"),
                Employee.class.getDeclaredField("salary")
        );

        mockedStaticReflectionUtil.when(() -> CsvReflectionUtil.getAllFields(Employee.class))
                .thenReturn(fields);

        csvWriterToFileService.writeToFile(employees, "test.csv");

        verify(csvHeaderWriterService).writeHeaders(fields);
        verify(csvRowWriterService).writeRow(fields, firstRandomEmployee);
        verify(csvRowWriterService).writeRow(fields, secondRandomEmployee);
    }

    @Test
    public void testWriteToFileWithException() throws NoSuchFieldException {
        Employee firstRandomEmployee = EmployeeDataUtil.getEmployees(4).get(0);
        Employee secondRandomEmployee = EmployeeDataUtil.getEmployees(4).get(1);

        List<Field> fields = List.of(
                Employee.class.getDeclaredField("department"),
                Employee.class.getDeclaredField("salary")
        );

        List<Employee> employees = List.of(firstRandomEmployee, secondRandomEmployee);

        mockedStaticReflectionUtil.when(() -> CsvReflectionUtil.getAllFields(Employee.class))
                .thenReturn(fields);

        var testException = new RuntimeException("Test exception");

        doThrow(testException).when(csvRowWriterService)
                .writeRow(eq(fields), argThat(obj -> obj instanceof Employee));

        doThrow(new CsvUnexpectedException("Wrapped exception", testException)).when(csvErrorHandler)
                .handleError(
                        argThat(message -> message.contains("Unexpected error while writing to CSV file")),
                        eq(testException),
                        eq(CsvUnexpectedException.class)
                );

        assertThrows(CsvUnexpectedException.class,
                () -> csvWriterToFileService.writeToFile(employees, "employees.csv"));

        verify(csvErrorHandler).handleError(
                argThat(message -> message.contains("Unexpected error while writing to CSV file")),
                eq(testException),
                eq(CsvUnexpectedException.class)
        );
    }

    @Test
    public void testWriteToFileWithEmptyData() {
        List<Employee> employees = new ArrayList<>();

        var exception = assertThrows(CsvDataException.class, () ->
                csvWriterToFileService.writeToFile(employees, "test.csv")
        );

        assertEquals("Data list is null or empty. Provided data: []", exception.getMessage());
    }

    @Test
    public void testWriteToFileWithEmptyFilename() {
        List<Employee> employees = EmployeeDataUtil.getEmployees(4);

        var exception = assertThrows(CsvDataException.class, () ->
                csvWriterToFileService.writeToFile(employees, "")
        );

        assertEquals("Filename is null or empty. Provided filename: ''", exception.getMessage());
    }
}