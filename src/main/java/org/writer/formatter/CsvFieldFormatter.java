package org.writer.formatter;

import org.writer.exception.CsvDataException;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Formats field values into CSV-compatible strings.
 * Supports collections and arrays by converting them into semicolon-separated values.
 */
public class CsvFieldFormatter {

    /**
     * Formats a value for CSV output.
     *
     * @param value the field value to format
     * @return the formatted CSV string
     * @throws CsvDataException if the value is null or an empty collection/array
     */
    public String format(Object value) {
        if (value == null) {
            throw new CsvDataException("CSV field value is null.");
        }

        if (value instanceof Collection<?> collection) {
            return formatCollection(collection);
        }

        if (value.getClass().isArray()) {
            return formatArray(value);
        }

        return value.toString();
    }

    /**
     * Formats a collection as a semicolon-separated string.
     *
     * @param collection the collection to format
     * @return the formatted string
     * @throws CsvDataException if the collection is empty
     */
    private String formatCollection(Collection<?> collection) {
        if (collection.isEmpty()) {
            throw new CsvDataException("CSV field contains an empty collection.");
        }
        return collection.stream()
                .map(Object::toString)
                .collect(Collectors.joining(";"));
    }

    /**
     * Formats an array as a semicolon-separated string.
     *
     * @param array the array to format
     * @return the formatted string
     * @throws CsvDataException if the array is empty
     */
    private String formatArray(Object array) {
        int length = Array.getLength(array);

        if (length == 0) {
            throw new CsvDataException("CSV field contains an empty array.");
        }
        return IntStream.range(0, length)
                .mapToObj(i -> Array.get(array, i).toString())
                .collect(Collectors.joining(";"));
    }
}