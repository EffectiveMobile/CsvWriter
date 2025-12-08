package org.writer.exception;

/**
 * Исключение {@code NoDataCSVException} выбрасывается в случаях,
 * когда отсутствуют данные для записи в CSV‑файл.
 */
public class NoDataCSVException extends RuntimeException {
    public static final String MSG = "There is no data to write to CSV";

    public NoDataCSVException() {
        super(MSG);
    }
}
