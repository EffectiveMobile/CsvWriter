package org.writer;

import java.util.List;

public class CSVWriterApi {

    public void writeToFile(List<?> data, String fileName) {
        Writable writable = new WritableImpl();
        writable.writeToFile(data, fileName);
    }
}
