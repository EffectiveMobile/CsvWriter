package org.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;


public class WritableImpl implements Writable {

    /**
     * Writes CSV file from List.
     *
     * @param data List containing data to be written into CSV file.
     * @param fileName Name of CSV file.
     */
    @Override
    public void writeToFile(List<?> data, String fileName) {
        List<List<String>> fullDataList = new ArrayList<>();
        Set<String> fieldNames = new LinkedHashSet<>();

        for (Object object : data) {
            List<String> subList = new ArrayList<>();
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                fieldNames.add(field.getName());
                Object fieldValue;
                try {
                    fieldValue = field.get(object);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                String stringToAdd;
                if (fieldValue instanceof Collection<?> collection) {
                    String joined = collection.stream()
                            .map(Object::toString)
                            .map(this::escapeCsv)
                            .collect(Collectors.joining(", "));
                    stringToAdd = escapeCsv(joined);
                } else {
                    stringToAdd = escapeCsv(fieldValue.toString());
                }
                subList.add(stringToAdd);

            }
            fullDataList.add(subList);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.append(String.join(",", fieldNames));
            writer.newLine();

            List<String> tempList = fullDataList.stream()
                    .map(strings -> String.join(",", strings))
                    .toList();
            for (String string : tempList) {
                writer.append(string);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains("\"") || value.contains(",") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
