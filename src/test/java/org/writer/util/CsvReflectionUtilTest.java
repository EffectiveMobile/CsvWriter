package org.writer.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.writer.exception.CsvReflectionException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class CsvReflectionUtilTest {
    private static final Random random = new Random();

    private static class TestClass {
        private static final String stringTestField = "test" + random.nextInt(1000);
        private static final int intTestField = random.nextInt(1000);
    }

    private TestClass testObject;

    @BeforeEach
    public void setUp() {
        testObject = new TestClass();
    }

    @Test
    public void testGetAllFieldsSuccess() {
        List<Field> fields = CsvReflectionUtil.getAllFields(TestClass.class);

        assertNotNull(fields, "The fields list should not be null");
        assertEquals(2, fields.size(), "The fields list size should be 2");

        for (Field field : fields) {
            assertTrue(field.getName().equals("stringTestField") ||
                            field.getName().equals("intTestField"),
                    "Field names should be 'field1' or 'field2'");
        }
    }

    @Test
    public void testGetFieldValueSuccess() {
        try {
            var stringTestField = TestClass.class.getDeclaredField("stringTestField");
            stringTestField.setAccessible(true);
            var firstFieldValue = CsvReflectionUtil.getFieldValue(testObject, stringTestField);

            assertNotNull(firstFieldValue, "The value of 'stringTestField' should not be null");
            assertInstanceOf(String.class, firstFieldValue, "The value of 'stringTestField' should be an String");
            assertEquals(TestClass.stringTestField, firstFieldValue,
                    "The value of 'stringTestField' should match the original value");

            var intTestField = TestClass.class.getDeclaredField("intTestField");
            intTestField.setAccessible(true);
            var secondFieldValue = CsvReflectionUtil.getFieldValue(testObject, intTestField);

            assertNotNull(secondFieldValue, "The value of 'intTestField' should not be null");
            assertInstanceOf(Integer.class, secondFieldValue, "The value of 'intTestField' should be an integer");
            assertEquals(TestClass.intTestField, secondFieldValue,
                    "The value of 'intTestField' should match the original value");

        } catch (NoSuchFieldException ex) {
            fail("Fields 'field1' and 'field2' should exist and be accessible", ex);
        }
    }

    @Test
    public void testGetFieldValueFailure() {
        try {
            var nonExistentField = TestClass.class.getDeclaredField("nonExistentField");
            CsvReflectionUtil.getFieldValue(testObject, nonExistentField);
            fail("Expected an exception to be thrown");
        } catch (NoSuchFieldException ex) {
            assertNotNull(ex, "Expected NoSuchFieldException to be thrown");
        } catch (CsvReflectionException ex) {
            fail("Unexpected exception type", ex);
        }
    }
}
