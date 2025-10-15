package org.writer;

import java.util.List;

public class CsvDocument implements Writable{
    @Override
    public void writeToFile(List<?> data, String fileName) {
        System.out.println("write object as CSV");
    }
}
