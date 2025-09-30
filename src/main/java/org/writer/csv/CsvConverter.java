package org.writer.csv;

import java.util.List;

/**
 * Interface for implementation by classes,
 * whose should provide conversion different objects to data for writing from csv file.
 * Implemented classes should exclude fields marked with
 * {@link org.writer.csv.annotation.CsvExclude} annotation when converting objects.
 * Attempting to pass empty collection or null to a parameter causes an unchecked exception,
 * typically an {@link IllegalArgumentException}. When working with Reflection API,
 * the following exceptions may be thrown: {@link IllegalAccessException},
 * {@link java.lang.reflect.InaccessibleObjectException}
 * Type parameters:
 * <T> – the type of data obtained as a result of object conversion.
 * Return data type must be declared in implemented class.
 *
 * @author Alexei Shvariov
 * @version 1.0
 */
public interface CsvConverter<T> {

    /**
     * @param objects - list of objects that to be converted into data for writing to a csv file.
     * @return data of type defined by the implementation class
     * @throws IllegalArgumentException, {@link IllegalAccessException}, {@link java.lang.reflect.InaccessibleObjectException}
     * */
    T toCsvFileData(List<?> objects);
}
