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
import org.writer.exception.CsvWriterException;
import org.writer.exception.handler.CsvErrorHandler;
import org.writer.model.CsvModel;
import org.writer.service.CsvWriterToFileService;
import org.writer.util.data.EmployeeDataUtil;
import org.writer.util.data.PersonDataUtil;
import org.writer.util.data.StudentDataUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CsvDataWriterServiceImplTest {

    @Mock
    private CsvErrorHandler csvErrorHandler;

    @Mock
    private CsvWriterToFileService csvWriterToFileService;

    @InjectMocks
    private CsvDataWriterServiceImpl csvDataWriterService;

    private MockedStatic<PersonDataUtil> mockedStaticPersonDataUtil;
    private MockedStatic<StudentDataUtil> mockedStaticStudentDataUtil;
    private MockedStatic<EmployeeDataUtil> mockedStaticEmployeeDataUtil;

    @BeforeEach
    public void setUp() {
        mockedStaticPersonDataUtil = Mockito.mockStatic(PersonDataUtil.class);
        mockedStaticStudentDataUtil = Mockito.mockStatic(StudentDataUtil.class);
        mockedStaticEmployeeDataUtil = Mockito.mockStatic(EmployeeDataUtil.class);
    }

    @AfterEach
    public void tearDown() {
        mockedStaticPersonDataUtil.close();
        mockedStaticStudentDataUtil.close();
        mockedStaticEmployeeDataUtil.close();
    }

    @Test
    public void testWritePeopleToFileSuccess() {
        List<CsvModel> people = List.of(mock(CsvModel.class), mock(CsvModel.class));

        mockedStaticPersonDataUtil.when(PersonDataUtil::getPeople)
                .thenReturn(people);

        csvDataWriterService.writePeopleToFile("people.csv");

        verify(csvWriterToFileService).writeToFile(people, "people.csv");
    }

    @Test
    public void testWriteStudentsToFileSuccess() {
        List<CsvModel> students = List.of(mock(CsvModel.class), mock(CsvModel.class));

        mockedStaticStudentDataUtil.when(StudentDataUtil::getStudents)
                .thenReturn(students);

        csvDataWriterService.writeStudentsToFile("students.csv");

        verify(csvWriterToFileService).writeToFile(students, "students.csv");
    }

    @Test
    public void testWriteEmployeesToFileSuccess() {
        List<CsvModel> employees = List.of(mock(CsvModel.class), mock(CsvModel.class));

        mockedStaticEmployeeDataUtil.when(EmployeeDataUtil::getEmployees)
                .thenReturn(employees);

        csvDataWriterService.writeEmployeesToFile("employees.csv");

        verify(csvWriterToFileService).writeToFile(employees, "employees.csv");
    }

    @Test
    public void testWriteToFileWithException() {
        List<CsvModel> employees = List.of(mock(CsvModel.class), mock(CsvModel.class));

        mockedStaticEmployeeDataUtil.when(EmployeeDataUtil::getEmployees)
                .thenReturn(employees);

        CsvFileWriteException testException = new CsvFileWriteException("Test exception", new RuntimeException());

        CsvWriterException mockException = mock(CsvWriterException.class);

        doThrow(testException).when(csvWriterToFileService)
                .writeToFile(eq(employees), eq("employees.csv"));

        when(csvErrorHandler.handleError(
                eq("Error generating CSV file: employees.csv"),
                eq(testException),
                eq(CsvWriterException.class)
        )).thenThrow(mockException);

        assertThrows(CsvWriterException.class, () -> csvDataWriterService.writeEmployeesToFile("employees.csv"));

        verify(csvWriterToFileService).writeToFile(eq(employees), eq("employees.csv"));
        verify(csvErrorHandler).handleError(
                eq("Error generating CSV file: employees.csv"),
                eq(testException),
                eq(CsvWriterException.class)
        );
    }
}