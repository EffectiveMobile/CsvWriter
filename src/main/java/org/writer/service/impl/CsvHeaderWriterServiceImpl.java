package org.writer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.writer.exception.CsvDataException;
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
@Slf4j
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
        if (fields == null || fields.isEmpty()) {
            throw new CsvDataException("Fields list is null or empty. Provided fields: " + fields);
        }

        try {
            var headerLine = new StringJoiner(",");

            for (Field field : fields) {
                if (CsvFieldValidator.isValidField(field)) {
                    String header = CsvFieldValidator.getFieldHeader(field);
                    headerLine.add(header);
                }
            }

            bufferedWriter.write(headerLine.toString());
            bufferedWriter.newLine();
            log.info("CSV headers written successfully: {}", headerLine);
        } catch (IOException ex) {
            throw csvErrorHandler.handleError("Error writing CSV headers", ex, CsvFileWriteException.class);
        }
    }
}