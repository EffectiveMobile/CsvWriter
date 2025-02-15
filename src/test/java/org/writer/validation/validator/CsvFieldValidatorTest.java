package org.writer.validation.validator;

import org.junit.jupiter.api.Test;
import org.writer.validation.annotation.ValidCsvField;

import java.util.Random;

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
}