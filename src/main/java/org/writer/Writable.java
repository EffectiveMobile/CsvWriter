package org.writer;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface  Writable {

   default void writeToFile(List<?> data){
       CsvMapper csvMapper = new CsvMapper();
       Class<?> clazz = data.get(0).getClass();
       CsvSchema csvSchema = csvMapper.schemaFor(clazz).withHeader();
       try {
           csvMapper.writer(csvSchema)
                   .writeValue(new File("data/" + clazz.getSimpleName()), data);
       } catch (IOException e) {
           throw new RuntimeException("Failed to write CSV file: " + e.getMessage(), e);
       }
   }

}
