package writer.csv;

import writer.Writable;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс для записи информации в файл в формате CSV.
 */
public class CsvWriterService implements Writable {

    @Override
    public void writeToFile(List<?> data,String fileName) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Data list cannot be null or empty");
        }
        Field[] fields = data.get(0).getClass().getDeclaredFields();

        try (FileWriter writer = new FileWriter(fileName,true)) {
            for (Object item : data) {
                List<String> values = Arrays.stream(fields)
                        .filter(field -> field.isAnnotationPresent(Csv.class) && field.getAnnotation(Csv.class).include())
                        .map(field -> {
                            field.setAccessible(true);
                            try {
                                Object value = field.get(item);
                                if (value instanceof List) {
                                    return ((List<?>) value)
                                            .stream()
                                            .map(Object::toString)
                                            .collect(Collectors.joining(";"));
                                }
                                return value != null ? value.toString() : "";
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException("Error accessing field value", e);
                            }
                        }).toList();
                writer.write(String.join(",", values) + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing CSV file", e);
        }
    }
}
