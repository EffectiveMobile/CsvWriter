package org.writer.service;

import java.util.List;

/**
 * Интерфейс для форматирования данных перед записью.
 */
public interface Formatter {
    String formatHeaders(List<?> data);
    List<String> formatData(List<?> data);
}
