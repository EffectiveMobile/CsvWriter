package org.writer.service;

import org.writer.Writable;
import org.writer.annotation.CsvColumn;
import org.writer.exception.WriterException;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CsvFileWriter implements Writable {

    /**
     * A method for converting reports to CSV
     *
     * @param data - list of users or students
     * @param fileName - name of the generated file
     * @throws WriterException - possible exception when generating CSV
     */
    @Override
    public void writeToFile(List<?> data, String fileName) {
        if (data == null || data.isEmpty()) {
            throw new WriterException("Data is null or empty");
        }
        if (fileName.isBlank()) {
            throw new WriterException("File Name is null or empty");
        }
        Class<?> clazz = data.get(0).getClass();
        List<Field> fields = getAnnotatedFields(clazz);
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(getHeader(fields));
            fileWriter.write("\n");
            for (Object dataItem : data) {
                fileWriter.write(getRow(dataItem, fields));
                fileWriter.write("\n");
            }
        } catch (IOException e) {
            throw new WriterException(e.getMessage());
        }
    }

    //Getting fields with CsvColumn annotation
    private List<Field> getAnnotatedFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(CsvColumn.class)) {
                fields.add(field);
            }
        }
        fields.sort(Comparator.comparing(
                file -> file.getAnnotation(CsvColumn.class).order()));
        return fields;
    }

    //Getting field headers
    private String getHeader(List<Field> fields) {
        List<String> headers = new ArrayList<>();
        for (Field field : fields) {
            headers.add(field.getAnnotation(CsvColumn.class).name());
        }
        return String.join(",", headers);
    }

    //Converting data to a string
    private String getRow(Object dataItem, List<Field> fields) {
        List<String> rows = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object val = field.get(dataItem);
                rows.add(val != null ? val.toString() : "");
            } catch (IllegalAccessException e) {
                rows.add("");
            }
        }
        return String.join(",", rows);
    }
}
