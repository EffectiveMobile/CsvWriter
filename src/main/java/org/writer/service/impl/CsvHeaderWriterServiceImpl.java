package org.writer.service.impl;

import lombok.RequiredArgsConstructor;
import org.writer.exception.CsvFileWriteException;
import org.writer.exception.handler.CsvErrorHandler;
import org.writer.service.CsvHeaderWriterService;
import org.writer.validation.validator.CsvFieldValidator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.StringJoiner;

/**
 * Service for writing CSV headers based on field names.
 */
@RequiredArgsConstructor
public class CsvHeaderWriterServiceImpl implements CsvHeaderWriterService {
    private final BufferedWriter bufferedWriter;
    private final CsvErrorHandler csvErrorHandler;

    /**
     * Writes the headers (field names) to the CSV file.
     *
     * @param fields the list of fields to include as headers.
     */
    @Override
    public void writeHeaders(List<Field> fields) {
        try {
            var headerLine = new StringJoiner(",");

            for (Field field : fields) {
                if (CsvFieldValidator.isValidField(field)) {
                    headerLine.add(field.getName());
                }
            }
            bufferedWriter.write(headerLine.toString());
            bufferedWriter.newLine();
        } catch (IOException e) {
            csvErrorHandler.handleError("Error writing CSV headers", e, CsvFileWriteException.class);
        }
    }
}
