package org.writer.service;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.writer.exception.IllegalCastException;
import org.writer.util.FieldMark;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Класс для создания csv
 */
public class CSVManipulator {

    public void createCSV(List<Object> objects, String filename) {
        List<String> values = new ArrayList<>();
        AtomicReference<String> head = new AtomicReference<>();
        objects.forEach(i -> {
            List<Field> field = FieldUtils.getFieldsListWithAnnotation(i.getClass(), FieldMark.class);
            head.set(this.headCreator(field));
            field.forEach(j -> {
                try {
                    j.setAccessible(true);
                    values.add(field.get(field.indexOf(j)).get(i).toString() + ";");
                } catch (IllegalAccessException e) {
                    try {
                        throw new IllegalCastException(e.getMessage());
                    } catch (IllegalCastException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            values.add(System.lineSeparator());
        });

        new WritableImpl().writeToFile(
                values, head.get(),
                filename);
    }

    public void createCSV1(Map<String, Object> objects) {

        objects.forEach((k, v) -> {
            List<Field> field = FieldUtils.getFieldsListWithAnnotation(objects.get(k).getClass(), FieldMark.class);
            List<String> values = new ArrayList<>();
            AtomicReference<String> fileName = new AtomicReference<>();
            AtomicReference<String> head = new AtomicReference<>();
            field.forEach(j -> {

                try {
                    j.setAccessible(true);
                    values.add(field.get(field.indexOf(j)).get(v).toString());
                    fileName.set(k);
                } catch (RuntimeException ex) {
                    throw new RuntimeException(ex);

            } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });
            new WritableImpl().writeToFile(values, "", fileName.get());

        });

    }

    public String headCreator(List<Field> field) {

        return field.stream().map(Field::getName).collect(Collectors.joining(";"));
    }
}
