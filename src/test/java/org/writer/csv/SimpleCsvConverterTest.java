package org.writer.csv;

import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.writer.model.Person;
import org.writer.model.Student;
import org.writer.model.generators.PersonGenerator;
import org.writer.model.generators.StudentsGenerator;

import java.util.Collections;
import java.util.List;

/**
 * @author Alexei Shvariov
 * @version 1.0
 */
 class SimpleCsvConverterTest {
    private final PersonGenerator personGenerator = new PersonGenerator(new Faker());
    private final StudentsGenerator studentsGenerator = new StudentsGenerator(new Faker());

    @Test
    @DisplayName("Test toCsvFileData when data contains valid objects then conversion success")
     void testToCsvFileData_whenDataContainsValidObjects_thenConversionSuccess() {
        int columnHeadersRowNumber = 1;
        SimpleCsvConverter simpleCsvConverter = new SimpleCsvConverter();
        List<Person> persons = personGenerator.getPersonsList(5);

        String result = simpleCsvConverter.toCsvFileData(persons);
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        String[] resultRows = result.split(System.lineSeparator());
        Assertions.assertEquals(persons.size() + columnHeadersRowNumber, resultRows.length);
    }

    @Test
    @DisplayName("Test toCsvFileData when data is null then throw IllegalArgumentException")
     void testToCsvFileData_whenDataIsNull_thenThrowIllegalArgumentException() {
        SimpleCsvConverter simpleCsvConverter = new SimpleCsvConverter();
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> simpleCsvConverter.toCsvFileData(null));
    }

    @Test
    @DisplayName("Test toCsvFileData when data is empty then throw IllegalArgumentException")
     void testToCsvFileData_whenDataIsEmpty_thenThrowIllegalArgumentException() {
        SimpleCsvConverter simpleCsvConverter = new SimpleCsvConverter();

        List<Object> students = Collections.emptyList();
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> simpleCsvConverter.toCsvFileData(students));
    }

    @Test
    @DisplayName("Test toCsvFileData when data contains objects of different classes throw IllegalArgumentException")
     void testToCsvFileData_whenDataContainsObjectsOfDifferentClasses_thenThrowIllegalArgumentException() {
        final Person person = personGenerator.getPerson();
        final Student student = studentsGenerator.getStudent();
        final SimpleCsvConverter simpleCsvConverter = new SimpleCsvConverter();

       List<Object> students = List.of(person, student);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> simpleCsvConverter.toCsvFileData(students));
    }

    @Test
    @DisplayName("Test toCsvFileData when data contains strings then throw IllegalArgumentException")
     void testToCsvFileData_whenDataContainsStrings_thenConversionSuccess() {
        final SimpleCsvConverter simpleCsvConverter = new SimpleCsvConverter();
        final List<String> strings = List.of("String1", "String2", "String3");

        String result = simpleCsvConverter.toCsvFileData(strings);
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        String[] resultRows = result.split(System.lineSeparator());
        Assertions.assertEquals(strings.size(), resultRows.length);
    }

    @Test
    @DisplayName("Test toCsvFileData when data contains primitives then throw IllegalArgumentException")
     void testToCsvFileData_whenDataContainsPrimitives_thenConversionSuccess() {
        SimpleCsvConverter simpleCsvConverter = new SimpleCsvConverter();
        List<Double> numbers = List.of(10.2, 11.2, 12.3);

        String result = simpleCsvConverter.toCsvFileData(numbers);
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        String[] resultRows = result.split(System.lineSeparator());
        Assertions.assertEquals(numbers.size(), resultRows.length);
    }

    @Test
    @DisplayName("Test toCsvFileData when collection field convert to single cell" +
            " then collection convert valid and added column headers")
     void testToCsvFileData_whenCollectionFieldConvertToSingleCell_thenCollectionConvertValid() {
        final int columnHeadersRowNumber = 1;
        final SimpleCsvConverter simpleCsvConverter = new SimpleCsvConverter();
        simpleCsvConverter.setConvertCollectionAsMultipleCell(false);
        final List<Student> students = studentsGenerator.getStudentsList(5);

        final String result = simpleCsvConverter.toCsvFileData(students);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        final String[] resultRows = result.split(System.lineSeparator());
        Assertions.assertEquals(students.size() + columnHeadersRowNumber, resultRows.length);
        final String scoreFieldValue = students.getFirst().getScore().toString()
                .replaceAll(simpleCsvConverter.getCellSeparator(), " |");
        Assertions.assertTrue(result.contains(scoreFieldValue));
    }

    @Test
    @DisplayName("Test toCsvFileData when collection field convert to multiple cell then collection convert valid")
     void testToCsvFileData_whenCollectionFieldConvertToMultipleCells_thenCollectionConvertValid() {
        final List<String> score = List.of("32", "99");
        final List<Student> students = List.of(new Student("Petya Ivanov", score));
        int nameColumnNumber = 1;
        SimpleCsvConverter simpleCsvConverter = new SimpleCsvConverter();
        simpleCsvConverter.setConvertCollectionAsMultipleCell(true);

        String result = simpleCsvConverter.toCsvFileData(students);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        String[] resultRows = result.split(System.lineSeparator());
        Assertions.assertEquals(students.size(), resultRows.length);
        String[] columns = resultRows[0].split(",");
        Assertions.assertEquals(score.size() + nameColumnNumber, columns.length);
    }

    @Test
    @DisplayName("Test toCsvFileData when field marked CsvExclude annotation " +
            "then result and column headers do not contains excluded field")
     void testToCsvFileData_whenFieldMarkedCsvExcludeAnnotation_thenResultsAndColumnHeadersNotContainsFieldValue() {
        final String excludeFieldName = "passport";
        final SimpleCsvConverter simpleCsvConverter = new SimpleCsvConverter();
        simpleCsvConverter.setConvertCollectionAsMultipleCell(false);
        final List<Person> persons = personGenerator.getPersonsList(2);

        final String result = simpleCsvConverter.toCsvFileData(persons);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        final String columnHeaders = result.split(System.lineSeparator())[0];
        Assertions.assertFalse(columnHeaders.contains(excludeFieldName));
        String dataRow = result.split(System.lineSeparator())[1];
        int columnValueNumbers = dataRow.split(simpleCsvConverter.getCellSeparator()).length;
        int columnNumbers = columnHeaders.split(simpleCsvConverter.getCellSeparator()).length;
        Assertions.assertEquals(columnNumbers, columnValueNumbers);

    }
}
