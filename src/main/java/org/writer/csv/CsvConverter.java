package org.writer.csv;

import java.util.List;

public interface CsvConverter {
    String toCsvString(List<?> objects);
}
