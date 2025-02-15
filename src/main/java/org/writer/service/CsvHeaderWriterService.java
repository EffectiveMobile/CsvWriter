package org.writer.service;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Service for writing headers to a CSV file.
 */

public interface CsvHeaderWriterService {

    /**
     * Writes the header row (field names) to the CSV file.
     *
     * @param fields the list of fields to include as headers.
     */
    void writeHeaders(List<Field> fields);
}
