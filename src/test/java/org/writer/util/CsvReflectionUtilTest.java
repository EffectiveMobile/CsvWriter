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
        private static final String field1 = "test" + random.nextInt(1000);
        private static final int field2 = random.nextInt(1000);
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
            assertTrue(field.getName()
                            .equals("field1") || field.getName()
                            .equals("field2"),
                    "Field names should be 'field1' or 'field2'");
        }
    }

    @Test
    public void testGetFieldValueSuccess() {
        try {
            var field1 = TestClass.class.getDeclaredField("field1");
            field1.setAccessible(true);
            var fieldValue1 = CsvReflectionUtil.getFieldValue(testObject, field1);

            assertNotNull(fieldValue1, "The value of 'field1' should not be null");
            assertInstanceOf(String.class, fieldValue1, "The value of 'field1' should be an String");
            assertEquals(TestClass.field1, fieldValue1, "The value of 'field1' should match the original value");

            var field2 = TestClass.class.getDeclaredField("field2");
            field2.setAccessible(true);
            var fieldValue2 = CsvReflectionUtil.getFieldValue(testObject, field2);

            assertNotNull(fieldValue2, "The value of 'field2' should not be null");
            assertInstanceOf(Integer.class, fieldValue2, "The value of 'field2' should be an integer");
            assertEquals(TestClass.field2, fieldValue2, "The value of 'field2' should match the original value");

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
