package org.writer.annotations;

/**
 * Определяет стратегию для экранирования полей, строковое представление которых содержит запятые
 * </br></br>
 * {@link EscapeStrategy#QUOTE_ESCAPING} - стратегия экранирования, которая оборачивает значение в кавычки.
 * Например: {@code fieldName,1} станет {@code "fieldName,1"}
 * </br></br>
 * {@link EscapeStrategy#COLUMN_ESCAPING} - стратегия экранирования, которая записывает значения между запятыми в
 * разные колонки. Семантически данная стратегия просто записывает данные "как есть"
 */
public enum EscapeStrategy {
    QUOTE_ESCAPING,
    COLUMN_ESCAPING
}
