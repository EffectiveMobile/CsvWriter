package org.writer;

import org.writer.annotation.CsvColumn;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/*
Реализация интерфейса Writable
*/

public class CsvWriter implements Writable {

    @Override
    public void writeToFile(List<?> data, String fileName) {

        if(isNull(data) && data.isEmpty()){

            throw new RuntimeException("List is empty!");
        }

        Class<?> aClass = data.get(0).getClass();
        Field[] fields = aClass.getDeclaredFields();

        try(FileWriter writer = new FileWriter(fileName)){

            StringBuilder header = new StringBuilder();

            for (Field field : fields) {
                if (field.isAnnotationPresent(CsvColumn.class)) {

                    CsvColumn column = field.getAnnotation(CsvColumn.class);
                    if (header.length() > 0) {
                        header.append(",");
                    }

                    header.append(column.name());
                }
            }

            header.append("\n");
            writer.write(header.toString());

            for (Object object : data) {
                String row = Arrays.stream(fields)
                        .filter(f -> f.isAnnotationPresent(CsvColumn.class))
                        .map(f -> {
                            f.setAccessible(true);
                            try {
                                Object value = f.get(object);
                                if (value instanceof List<?>) {
                                    return ((List<?>) value).stream()
                                            .map(Object::toString)
                                            .collect(Collectors.joining("|"));
                                }
                                return value != null ? value.toString() : "";
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.joining(","));
                writer.write(row + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
