package org.writer.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

public class ReflectionUtils {

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
        return Arrays.stream(object.getClass().getDeclaredAnnotations())
                .map(Annotation::annotationType)
                .map(Class::getName)
                .anyMatch(name -> name.equals(annotationFullName));
    }

}
