package org.writer.service;

import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.writer.Writable;
import org.writer.annotation.CsvWritable;

/**
 * {@code CsvWriter} provides implementation of {@code @Writeable} interface
 * <p>
 *   CsvWriter provides method to save data in CSV format using {@code CsvWritable} annotation marks
 * </p>
 */
public class CsvWriter implements Writable {

  /**
   * Saves list of objects in CSV formatted file
   * @param data list of values to be saved
   * @param fileName path or name of file
   * @throws IllegalArgumentException when list contains objects from different classes
   * @throws RuntimeException when can not write to file or can not read object field
   */
  @Override
  public void writeToFile(List<?> data, String fileName) {
    if (data == null || data.isEmpty()) {
      return;
    }

    Object obj = data.stream().filter(Objects::nonNull).findFirst().orElse(null);
    if (obj == null) {
      return;
    }

    Class<?> targetClass = obj.getClass();

    boolean isClassWriteable =
        targetClass.isAnnotationPresent(CsvWritable.class) && targetClass.getAnnotation(
            CsvWritable.class).value();

    List<Field> writeableFields = Arrays.stream(targetClass.getDeclaredFields())
        .filter(field -> isClassWriteable || field
            .isAnnotationPresent(CsvWritable.class) && field.getAnnotation(
            CsvWritable.class).value()).peek(AccessibleObject::trySetAccessible).toList();

    if (writeableFields.isEmpty()) {
      return;
    }

    String result =
        writeableFields.stream().map(Field::getName).collect(Collectors.joining(",", "", "\n"))
            + data.stream()
            .map(writeableObj -> {

                  if (!writeableObj.getClass().equals(obj.getClass())) {
                    throw new IllegalArgumentException("Object type inconsistent. Expected %s but found %s".formatted(targetClass, obj.getClass().getName()) );
                  }

                  return writeableFields.stream()
                      .map(field -> getFormattedValue(field, writeableObj))
                      .collect(Collectors.joining(","));
                }
            ).collect(Collectors.joining("\n"));
    try {
      Files.writeString(Path.of(fileName + ".csv"), result);
    } catch (IOException e) {
      throw new RuntimeException("Failed to write CSV file: " + fileName, e);
    }
  }

  private String getFormattedValue(Field field, Object object) {
    Object fieldVal;
    try {
      fieldVal = field.get(object);
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Could not read field: " + field.getName(),
          e);
    }

    if (fieldVal == null) {
      return "";
    }

    String stringVal;

    if (fieldVal instanceof Collection<?>) {
      stringVal = ((Collection<?>) fieldVal).stream()
          .map(item -> item == null ? "" : item.toString())
          .collect(Collectors.joining(";"));
    } else {
      stringVal = String.valueOf(fieldVal);
    }

    boolean needsQuotes =
        stringVal.contains("\n")
            || stringVal.contains("\"")
            || stringVal.contains(",")
            || stringVal.contains("\r");

    if (needsQuotes) {
      stringVal = stringVal.replace("\"", "\"\"");
      stringVal = "\"" + stringVal + "\"";
    }

    return stringVal;
  }

}

