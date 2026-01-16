package org.writer.exception;

/**
 *{@link WriteToFileException} should throws when throws IOException while writing data to a file.
 *{@link WriteToFileException} are unchecked exceptions.
 * Unchecked exceptions do not need to be declared in a method or constructor's throws clause
 * if they can be thrown by the execution of the method or constructor and
 * propagate outside the method or constructor boundary*/
public class WriteToFileException extends RuntimeException {

    /**
     * Constructs a new {@link WriteToFileException} with the specified detail message.
     * @param message – the detail message. The detail message is saved for later retrieval by the getMessage() method.
     * */
    public WriteToFileException(String message) {
        super(message);
    }
}
