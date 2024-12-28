package org.writer.service;

import org.junit.jupiter.api.Test;
import org.writer.util.ClassLabel;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class ManipulatorTest {

   ClassManipulator classManipulator = new ClassManipulator();
   Manipulator manipulator = new Manipulator();

    @Test
    void manipulate() {
//       List<?> list = classManipulator.classScanner("org.writer.model", ClassLabel.class);
//       Map<String, List<Field>> map = manipulator.manipulate(list);
//
//       map.values().forEach(System.out::println);
//
//       assertEquals("Person", map.keySet()
//                                          .stream()
//                                          .filter(i->i.equals("Person"))
//                                          .toList().get(0));
//
//
//        assertTrue(map.values()
//                .stream()
//                .flatMap(Collection::stream)
//                .anyMatch(iter -> iter.getName().equals("scores")), "value: true");
    }
}