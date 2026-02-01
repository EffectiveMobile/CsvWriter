package org.writer;

import java.io.IOException;
import java.util.List;

public interface Writable {

    void writeToCsv(List<?> data,String fileName) throws IOException;

}
