package org.writer;

import lombok.RequiredArgsConstructor;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class StringCsvWriter implements Writable {
    private final CsvConverter csvConverter;

    @Override
    public void writeToFile(List<?> data, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)){
            writer.write(csvConverter.toCsvString(data));
        } catch (IOException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
