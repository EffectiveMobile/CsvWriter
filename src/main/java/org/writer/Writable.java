package org.writer;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public interface Writable extends Closeable {

    void write(List<?> data) throws IOException;
}
