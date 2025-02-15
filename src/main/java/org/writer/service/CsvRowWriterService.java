package org.writer.service;

import org.writer.model.CsvModel;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Service for writing a single row of data to a CSV file.
 */
public interface CsvRowWriterService {

    /**
     * Writes a row of data to the CSV file.
     *
     * @param fields the list of fields to include in the row.
     * @param object the data object to write as a row.
     */
    void writeRow(List<Field> fields, CsvModel object);
}
