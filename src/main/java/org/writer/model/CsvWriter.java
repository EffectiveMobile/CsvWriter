package org.writer.model;

import org.writer.Writable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * This class has a function that writes objects inside csv file
 * and then creates it
 * otherwise it throws illegalArgumentException
 * */
public class CsvWriter implements Writable {

    @Override
    public void writeToCsv(List<?> data,String fileName) throws IOException {
        if(fileName.contains(".csv")) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
                bw.write(data.toString());
            }
        }else {
           throw new IllegalArgumentException("Wrong file type");
        }
    }
}
