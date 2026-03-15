package org.writer;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WritableImplTest {
    private final Writable writable = new WritableImpl();

    static class TestData{
        @Csv(name = "name")
        private final String name;
        @Csv(name = "age")
        private final int age;

        private byte ignoredData;

        public TestData(String name, int age){
            this.name = name;
            this.age = age;
        }
    }

    @Test
    void testWriteWithNullData() {

        assertThrows(IllegalArgumentException.class,
            () -> writable.writeToFile(null, "some.csv"));

    }

    @Test
    void testWriteWithEmptyData() {

        assertThrows(IllegalArgumentException.class,
            () -> writable.writeToFile(new ArrayList<>(), "some.csv"));

    }

    @Test
    void testWriteSuccess() throws IOException {
        String fileName = "some.csv";
        List<TestData> testData = new ArrayList<>();
        testData.add(new TestData("name1", 1));
        testData.add(new TestData("name2", 2));
        testData.add(new TestData("name3", 3));

        writable.writeToFile(testData, fileName);

        List<String> lines = Files.readAllLines(Path.of(fileName));

        assertEquals(4, lines.size());
        assertEquals("name,age", lines.get(0));
        assertEquals("name1,1", lines.get(1));
        assertEquals("name2,2", lines.get(2));
        assertEquals("name3,3", lines.get(3));
    }
}