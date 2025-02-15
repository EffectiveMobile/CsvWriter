package org.writer.formatter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for formatting field values into CSV-compatible strings.
 * Handles special cases such as lists, converting them into a single string.
 */
public class CsvFieldFormatter {

    /**
     * Formats a field value for CSV output.
     *
     * @param value the field value to format.
     * @return the formatted value as a string.
     */
    public String format(Object value) {
        if (value instanceof List) {
            return ((List<?>) value).stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(";"));
        }
        return value.toString();
    }
}
