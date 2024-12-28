package org.writer.service;

import org.junit.jupiter.api.Test;
import org.writer.util.ClassLabel;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ClassManipulatorTest {

    public ClassManipulator classManipulator = new ClassManipulator();

    @Test
    void classScanner() {

       List<?> list = classManipulator.classScanner("org.writer.model", ClassLabel.class);

       assertEquals(3, list.size(), "value: 3");
       assertEquals("class org.writer.model.Person", list.get(0).toString(),
               "value: class org.writer.model.Person");
    }
}