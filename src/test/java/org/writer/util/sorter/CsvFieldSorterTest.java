package org.writer.util.sorter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.writer.annotation.CsvFieldOrder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CsvFieldSorterTest {
    private List<Field> fields;
    private static final Random random = new Random();

    @BeforeEach
    public void setUp() throws NoSuchFieldException {
        class TestClass {
            @CsvFieldOrder(2)
            private static final String field1 = "test" + random.nextInt(1000);
            @CsvFieldOrder(1)
            private static final int field2 = random.nextInt(1000);
            private static final double field3 = random.nextDouble() * 1000;
        }

        fields = new ArrayList<>();
        fields.add(TestClass.class.getDeclaredField("field1"));
        fields.add(TestClass.class.getDeclaredField("field2"));
        fields.add(TestClass.class.getDeclaredField("field3"));
    }

    @Test
    public void testSortFieldsSuccess() {
        CsvFieldSorter.sortFields(fields);

        assertEquals("field2", fields.get(0)
                .getName());
        assertEquals("field1", fields.get(1)
                .getName());
        assertEquals("field3", fields.get(2)
                .getName());
    }
}