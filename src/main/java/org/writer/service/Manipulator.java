package org.writer.service;

import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс для поиска и фильтрации полей в классах помеченных аннотацией FieldLabel
 * @param <T> универсальный тип
 */
public class Manipulator<T> {

    @SneakyThrows
    public List<Object> manipulate(List<Class<?>> classes, List<Object> objects) {

        return objects.stream()
                .filter(i->classes.contains(i.getClass()))
                .toList();
    }
    
    @SneakyThrows
    public Map<String, Object> manipulator(List<Class<?>> classes, List<Object> objects) {
        
        Map<String, Object> result = new HashMap<>();

        for (Class<?> aClass : classes) {

            Object o = new Object();

            for (Object object : objects) {
                if (object.getClass() == aClass) {

                    o = objects.get(objects.indexOf(object));
                }

            }

            result.put(aClass.getSimpleName(), o);
        }
        
        return result;
    }
}
