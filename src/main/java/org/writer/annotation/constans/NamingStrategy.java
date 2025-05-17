package org.writer.annotation.constans;

public enum NamingStrategy {

    /**
     * Использует имя поля как есть (например, "firstName" -> "firstName").
     */
    AS_IS,
    /**
     * Преобразует camelCase в слова с пробелами и первой заглавной (например, "firstName" -> "First Name").
     * Это будет значение по умолчанию для @CsvRecord.
     */
    AS_IS_TO_SPACE_SEPARATED_CAPITALIZED,
    /**
     * Преобразует camelCase в snake_case (например, "firstName" -> "first_name").
     */
    CAMEL_TO_SNAKE_CASE,
    /**
     * Преобразует camelCase в SCREAMING_SNAKE_CASE (например, "firstName" -> "FIRST_NAME").
     */
    CAMEL_TO_SCREAMING_SNAKE_CASE,
    /**
     * Использует стратегию, указанную в @CsvRecord, или глобальную по умолчанию, если не указана.
     * Специальное значение для @CsvColumn.
     */
    DEFAULT
}
