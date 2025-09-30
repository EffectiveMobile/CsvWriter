package org.writer.utils;

import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.writer.model.Person;
import org.writer.model.Student;
import org.writer.model.generators.PersonGenerator;
import org.writer.model.generators.StudentsGenerator;

import java.lang.reflect.Field;

/**
 * @author Alexei Shvariov
 * @version 1.0
 */
class ReflectionUtilsTest {
    private final PersonGenerator personGenerator = new PersonGenerator(new Faker());
    private final StudentsGenerator studentsGenerator = new StudentsGenerator(new Faker());

    @Test
    @DisplayName("Test getAllFields then  invoke method then return all fields array")
    void testGetAllFields_whenInvokeMethod_thenReturnAllFields() {
        Person person = personGenerator.getPerson();
        int personFieldNumber = 6;

        Field[] fields = ReflectionUtils.getAllFields(person);

        Assertions.assertEquals(personFieldNumber, fields.length);
    }

    @Test
    @DisplayName("Test isCollection when field is collection then return true")
    void testIsCollection_whenFieldIsCollection_thenReturnTrue() throws NoSuchFieldException {
        String collectionFieldName = "score";
        Student student = studentsGenerator.getStudent();
        Field collectionField = student.getClass().getDeclaredField(collectionFieldName);

        Assertions.assertTrue(ReflectionUtils.isCollection(collectionField));
    }

    @Test
    @DisplayName("Test isCollection when field is not collection then return false")
    void testIsCollection_whenFieldIsNotCollection_thenReturnFalse() throws NoSuchFieldException {
        String notCollectionFieldName = "name";
        Student student = studentsGenerator.getStudent();
        Field notCollection = student.getClass().getDeclaredField(notCollectionFieldName);

        Assertions.assertFalse(ReflectionUtils.isCollection(notCollection));
    }

    @Test
    @DisplayName("Test getPrivateFieldValue when invoke method then return valid value")
    void testGetPrivateFieldValue_whenInvokeMethod_thenReturnValidValue() throws NoSuchFieldException, IllegalAccessException {
        String fieldName = "name";
        Student student = studentsGenerator.getStudent();
        Field field = student.getClass().getDeclaredField(fieldName);

        Assertions.assertEquals(student.getName(), ReflectionUtils.getPrivateFieldValue(student, field));
    }



}
