package org.writer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("CsvSerializer тесты")
class CsvSerializerTest {

    private final CsvSerializer csvSerializer = new CsvSerializer();

    @Test
    @DisplayName("Сериализация Person с аннотациями")
    void serialize_personWithAnnotations_returnsCorrectCsv() {
        Person person = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .dayOfBirth(15)
                .monthOfBirth(Months.JANUARY)
                .yearOfBirth(1990)
                .build();
        List<Person> persons = List.of(person);

        String result = csvSerializer.serialize(persons);

        assertThat(result).isEqualTo(
                "First name;Last name;monthOfBirth;Birth year\n" +
                "John;Doe;JANUARY;1990"
        );
    }

    @Test
    @DisplayName("Игнорирование поля dayOfBirth в Person")
    void serialize_person_ignoresDayOfBirth() {
        Person person = Person.builder()
                .firstName("Jane")
                .lastName("Smith")
                .dayOfBirth(25)
                .monthOfBirth(Months.DECEMBER)
                .yearOfBirth(1985)
                .build();
        List<Person> persons = List.of(person);

        String result = csvSerializer.serialize(persons);

        assertThat(result)
                .doesNotContain("Birth day")
                .doesNotContain("25")
                .contains("First name;Last name;monthOfBirth;Birth year");
    }

    @Test
    @DisplayName("Сериализация нескольких Person")
    void serialize_multiplePersons_returnsCorrectCsv() {
        List<Person> persons = Arrays.asList(
                Person.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .monthOfBirth(Months.JANUARY)
                        .yearOfBirth(1990)
                        .build(),
                Person.builder()
                        .firstName("Jane")
                        .lastName("Smith")
                        .monthOfBirth(Months.DECEMBER)
                        .yearOfBirth(1985)
                        .build()
        );

        String result = csvSerializer.serialize(persons);

        assertThat(result).isEqualTo(
                """
                        First name;Last name;monthOfBirth;Birth year
                        John;Doe;JANUARY;1990
                        Jane;Smith;DECEMBER;1985"""
        );
    }

    @Test
    @DisplayName("Сериализация Student с коллекцией")
    void serialize_studentWithList_returnsCorrectCsv() {
        Student student = Student.builder()
                .name("Alice")
                .score(Arrays.asList("A", "B", "C"))
                .build();
        List<Student> students = List.of(student);

        String result = csvSerializer.serialize(students);

        assertThat(result).isEqualTo(
                "Name;Score\n" +
                "Alice;A,B,C"
        );
    }

    @Test
    @DisplayName("Сериализация нескольких Student")
    void serialize_multipleStudents_returnsCorrectCsv() {
        List<Student> students = Arrays.asList(
                Student.builder()
                        .name("Alice")
                        .score(Arrays.asList("A", "B"))
                        .build(),
                Student.builder()
                        .name("Bob")
                        .score(Arrays.asList("C", "D", "E"))
                        .build()
        );

        String result = csvSerializer.serialize(students);

        assertThat(result).isEqualTo(
                """
                        Name;Score
                        Alice;A,B
                        Bob;C,D,E"""
        );
    }

    @Test
    @DisplayName("Экранирование специальных символов в Person")
    void serialize_personWithSpecialCharacters_escapesCorrectly() {
        Person person = Person.builder()
                .firstName("John;Doe")
                .lastName("Smith\"Jr\"")
                .monthOfBirth(Months.JANUARY)
                .yearOfBirth(1990)
                .build();
        List<Person> persons = List.of(person);

        String result = csvSerializer.serialize(persons);

        assertThat(result)
                .contains("John;Doe")
                .contains("\"Smith\"\"Jr\"\"\"");
    }

    @Test
    @DisplayName("Обработка null значений в Person")
    void serialize_personWithNullValues_handlesCorrectly() {
        Person person = Person.builder()
                .firstName(null)
                .lastName("Doe")
                .monthOfBirth(null)
                .yearOfBirth(1990)
                .build();
        List<Person> persons = List.of(person);

        String result = csvSerializer.serialize(persons);

        assertThat(result).isEqualTo(
                """
                First name;Last name;monthOfBirth;Birth year
                ;Doe;;1990"""
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Выбрасывание исключения для пустого списка")
    void serialize_emptyOrNullList_throwsException(List<Person> persons) {
        assertThatThrownBy(() -> csvSerializer.serialize(persons))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Смешанные объекты разных типов выбрасывают исключение")
    void serialize_mixedObjectTypes_throwsException() {
        List<Object> objects = Arrays.asList(
                Person.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .build(),
                Student.builder()
                        .name("Alice")
                        .score(List.of("A"))
                        .build()
        );

        assertThatThrownBy(() -> csvSerializer.serialize(objects))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("All objects must be of the same type");
    }

    @Test
    @DisplayName("Сериализация Person без месяца рождения")
    void serialize_personWithoutMonth_handlesCorrectly() {
        Person person = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .yearOfBirth(1990)
                .build();
        List<Person> persons = List.of(person);

        String result = csvSerializer.serialize(persons);

        assertThat(result).isEqualTo(
                """
                First name;Last name;monthOfBirth;Birth year
                John;Doe;;1990"""
        );
    }
}