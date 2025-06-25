package org.writer;

import java.util.List;

public interface CsvConverter {
    String toCsvString(List<?> objects) throws IllegalAccessException;
}
