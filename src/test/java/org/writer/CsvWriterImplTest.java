package org.writer;

import org.junit.jupiter.api.Test;
import org.writer.model.Months;
import org.writer.model.Person;
import org.writer.model.Student;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class CsvWriterImplTest {

	private final CsvWriterImpl writer = new CsvWriterImpl();


	@Test
	void testWriteToFile_Student() {
		Student student1 = Student.builder()
				.name("Иван")
				.score(Arrays.asList("5", "4", "5"))
				.build();

		Student student2 = Student.builder()
				.name("Мария")
				.score(Arrays.asList("5", "5", "5"))
				.build();

		List<Student> students = Arrays.asList(student1, student2);

		writer.writeToFile(students, "students.csv");

		assertTrue(new File("students.csv").exists());
	}

	@Test
	void testWriteToFile_Person()  {
		Person person1 = Person.builder()
				.firstName("Иван")
				.lastName("Иванов")
				.dayOfBirth(1)
				.monthOfBirth(Months.JANUARY)
				.yearOfBirth(2000)
				.build();

		Person person2 = Person.builder()
				.firstName("Мария")
				.lastName("Мария")
				.dayOfBirth(1)
				.monthOfBirth(Months.JANUARY)
				.yearOfBirth(2000)
				.build();

		List<Person> persons = Arrays.asList(person1, person2);

		writer.writeToFile(persons, "persons.csv");

		assertTrue(new File("persons.csv").exists());
	}


}