package org.writer.service.impl;

import lombok.RequiredArgsConstructor;
import org.writer.exception.CsvDataException;
import org.writer.exception.CsvFileWriteException;
import org.writer.exception.CsvUnexpectedException;
import org.writer.exception.handler.CsvErrorHandler;
import org.writer.model.CsvModel;
import org.writer.service.CsvHeaderWriterService;
import org.writer.service.CsvRowWriterService;
import org.writer.service.CsvWriterToFileService;
import org.writer.util.CsvReflectionUtil;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Service for writing a list of data objects to a CSV file.
 */
@RequiredArgsConstructor
public class CsvWriterToFileServiceImpl implements CsvWriterToFileService {
    private final CsvErrorHandler csvErrorHandler;
    private final CsvRowWriterService csvRowWriterService;
    private final CsvHeaderWriterService csvHeaderWriterService;

    /**
     * Writes a list of data objects to a CSV file.
     *
     * @param data     the list of data objects to write. Each object must extend {@link CsvModel}.
     * @param fileName the name of the file to write the data to.
     * @throws CsvDataException       if the data list is empty or null.
     * @throws CsvFileWriteException  if an error occurs while writing to the file.
     * @throws CsvUnexpectedException if an unexpected error occurs during the process.
     */
    @Override
    public void writeToFile(List<? extends CsvModel> data, String fileName) {
        if (data == null || data.isEmpty()) {
            throw new CsvDataException("Data list is empty or null");
        }

        try {
            Class<?> clazz = data.get(0)
                    .getClass();
            List<Field> fields = CsvReflectionUtil.getAllFields(clazz);

            csvHeaderWriterService.writeHeaders(fields);

            for (CsvModel object : data) {
                csvRowWriterService.writeRow(fields, object);
            }

        } catch (Exception ex) {
            throw csvErrorHandler.handleError("Unexpected error while writing to CSV file: " + fileName, ex,
                    CsvUnexpectedException.class);
        }
    }
}