package org.writer.validation.validator;

import org.junit.jupiter.api.Test;
import org.writer.validation.annotation.ValidCsvField;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CsvFieldValidatorTest {
    private static final Random random = new Random();

    @Test
    public void testIsValidFieldWithAnnotatedField() throws NoSuchFieldException {
        class TestClass {

            @ValidCsvField
            private static final String validField = "test" + random.nextInt(1000);
        }

        var field = TestClass.class.getDeclaredField("validField");
        boolean result = CsvFieldValidator.isValidField(field);

        assertTrue(result);
    }

    @Test
    public void testIsValidFieldWithNonAnnotatedField() throws NoSuchFieldException {
        class TestClass {
            private static final String nonAnnotatedField = "test" + random.nextInt(1000);
        }

        var field = TestClass.class.getDeclaredField("nonAnnotatedField");
        boolean result = CsvFieldValidator.isValidField(field);

        assertFalse(result);
    }

    @Test
    public void testGetFieldHeaderWithAnnotatedFieldAndHeaderName() throws NoSuchFieldException {
        class TestClass {

            @ValidCsvField(headerName = "Custom Header")
            private static final String fieldWithHeader = "test" + random.nextInt(1000);
        }

        var field = TestClass.class.getDeclaredField("fieldWithHeader");
        String header = CsvFieldValidator.getFieldHeader(field);

        assertEquals("Custom Header", header);
    }

    @Test
    public void testGetFieldHeaderWithAnnotatedFieldButEmptyHeaderName() throws NoSuchFieldException {
        class TestClass {

            @ValidCsvField(headerName = "")
            private static final String fieldWithoutHeader = "test" + random.nextInt(1000);
        }

        var field = TestClass.class.getDeclaredField("fieldWithoutHeader");
        String header = CsvFieldValidator.getFieldHeader(field);

        assertEquals("fieldWithoutHeader", header);
    }

    @Test
    public void testGetFieldHeaderWithNonAnnotatedField() throws NoSuchFieldException {
        class TestClass {
            private static final String normalField = "test" + random.nextInt(1000);
        }

        var field = TestClass.class.getDeclaredField("normalField");
        String header = CsvFieldValidator.getFieldHeader(field);

        assertEquals("normalField", header);
    }
}