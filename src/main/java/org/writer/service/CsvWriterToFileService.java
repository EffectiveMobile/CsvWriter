package org.writer.service;

import org.writer.model.CsvModel;

import java.util.List;

/**
 * Service for writing data to CSV files.
 * Provides a method to write a list of {@link CsvModel} objects to a specified file.
 */
public interface CsvWriterToFileService {

    /**
     * Writes a list of data objects to a CSV file.
     *
     * @param data     the list of data objects to write. Each object must extend {@link CsvModel}.
     * @param fileName the name of the file to write the data to.
     */
    void writeToFile(List<? extends CsvModel> data, String fileName);
}
