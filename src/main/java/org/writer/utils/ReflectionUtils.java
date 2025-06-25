package org.writer.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ReflectionUtils {

    public static Class<?> getClassType(Object object) {
        return object.getClass();
    }

    public static Field[] getAllFields(Object object) {
        return object.getClass().getDeclaredFields();
    }

    public static boolean isCollection(Field field){
        return Collection.class.isAssignableFrom(field.getType());
    }

    public static Object getPrivateFieldValue(Object object, Field objectField) throws IllegalAccessException {
        objectField.setAccessible(true);
        return objectField.get(object);
    }

    public static boolean isAnnotatedBy(Object object, String annotationFullName) {
        List<String> annotationsNames = Arrays.stream(object.getClass().getDeclaredAnnotations())
                .map(Annotation::annotationType)
                .map(Class::getName)
                .toList();
        return annotationsNames.contains(annotationFullName);
    }

}
