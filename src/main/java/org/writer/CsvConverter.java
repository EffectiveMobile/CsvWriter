package org.writer;

public interface CsvConverter {
    String convertToCsvRow(Object object) throws IllegalAccessException;
}
