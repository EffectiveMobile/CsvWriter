package org.writer.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.Collection;

/**
 * Is Collection of utility methods for get information about object's fields
 * using java reflection API.
 *
 * @author Alexei Shvariov
 * @version 1.0
 */

@SuppressWarnings("squid:S3011") // Рефлексия используется с целью выполнения задания.
public class ReflectionUtils {

    private ReflectionUtils() {
    }

    /**
     * Get array of declared fields for object's class.
     * @param object - any class extends Object.
     * @return array of {@link Field} class elements.
     * */
    public static Field[] getAllFields(Object object) {
        return object.getClass().getDeclaredFields();
    }

    /**
     * @param field - object class {@link Field}.
     * @return true if the field class extends or implements {@link Collection},
     * otherwise returns false
     * */
    public static boolean isCollection(Field field){
        return Collection.class.isAssignableFrom(field.getType());
    }

    /**
     * Allows access to field for reflection utilities and retrieves object field value.
     * @param object - instance of class whose field value needs to be retrieved
     * @param objectField - object class {@link Field}.
     * @return value for object class field.
     * */
    public static Object getPrivateFieldValue(Object object, Field objectField) throws IllegalAccessException, InaccessibleObjectException {
        objectField.setAccessible(true);
        return objectField.get(object);
    }


}
