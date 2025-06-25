package org.writer;

import lombok.AllArgsConstructor;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class CsvPrinter {

    private final static String CLASS_SUPPORT_ANNOTATION = "org.writer.annotation.Csv";
    private final CsvConverter csvConverter = new SimpleCsvConverter();
    List<?> objects;

    public String printToString() throws IllegalAccessException {
        StringBuilder fileData =  new StringBuilder();
        for (var object : objects) {
            if (isAnnotatedByCsv(object)) {
                fileData.append(csvConverter.convertToCsvRow(object));
                fileData.append(System.lineSeparator());
            } else {
                System.out.println("Класс "+ object.getClass().getName() + " не помечен как поддерживающий печать в CSV."
                        + " Данные объекта не экспортированы в CSV");
            }
        }
        return fileData.toString();
    }

    private boolean isAnnotatedByCsv(Object object) {
        List<String> annotationsNames = Arrays.stream(object.getClass().getDeclaredAnnotations())
                .map(Annotation::annotationType)
                .map(Class::getName)
                .toList();
        return annotationsNames.contains(CLASS_SUPPORT_ANNOTATION);
    }




}
