package org.writer.exception;

import java.io.IOException;

public class WritingToFileException extends RuntimeException {
    public WritingToFileException(String message, IOException e) {
        super(message, e);
    }
}
