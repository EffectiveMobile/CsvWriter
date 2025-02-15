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
import org.writer.model.CsvModel;
import org.writer.model.Employee;
import org.writer.service.CsvHeaderWriterService;
import org.writer.service.CsvRowWriterService;
import org.writer.util.CsvReflectionUtil;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CsvWriterToFileServiceImplTest {

    @Mock
    private CsvErrorHandler csvErrorHandler;

    @Mock
    private CsvRowWriterService csvRowWriterService;

    @Mock
    private CsvHeaderWriterService csvHeaderWriterService;

    @InjectMocks
    private CsvWriterToFileServiceImpl csvWriterToFileService;

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
        Employee employee1 = new Employee("HR", new BigDecimal("5000"));
        employee1.setId("1");
        Employee employee2 = new Employee("Finance", new BigDecimal("6000"));
        employee2.setId("2");

        List<Employee> employees = List.of(employee1, employee2);
        List<Field> fields = List.of(
                CsvModel.class.getDeclaredField("id"),
                Employee.class.getDeclaredField("department"),
                Employee.class.getDeclaredField("salary")
        );

        mockedStaticReflectionUtil.when(() -> CsvReflectionUtil.getAllFields(any()))
                .thenReturn(fields);

        csvWriterToFileService.writeToFile(employees, "test.csv");

        verify(csvHeaderWriterService).writeHeaders(fields);
        verify(csvRowWriterService).writeRow(fields, employee1);
        verify(csvRowWriterService).writeRow(fields, employee2);
    }

    @Test
    public void testWriteToFileWithException() throws NoSuchFieldException {
        Employee employee1 = new Employee("HR", new BigDecimal("5000"));
        employee1.setId("1");
        Employee employee2 = new Employee("Finance", new BigDecimal("6000"));
        employee2.setId("2");

        List<Employee> employees = List.of(employee1, employee2);
        List<Field> fields = List.of(
                CsvModel.class.getDeclaredField("id"),
                Employee.class.getDeclaredField("department"),
                Employee.class.getDeclaredField("salary")
        );

        mockedStaticReflectionUtil.when(() -> CsvReflectionUtil.getAllFields(any()))
                .thenReturn(fields);
        doThrow(new RuntimeException("Test exception")).when(csvRowWriterService)
                .writeRow(any(), any());

        csvWriterToFileService.writeToFile(employees, "test.csv");

        verify(csvErrorHandler).handleError(anyString(), any(RuntimeException.class), eq(CsvUnexpectedException.class));
    }

    @Test
    public void testWriteToFileWithEmptyData() {
        List<Employee> employees = new ArrayList<>();

        try {
            csvWriterToFileService.writeToFile(employees, "test.csv");
        } catch (CsvDataException e) {
            assertEquals("Data list is empty or null", e.getMessage());
        }
    }
}