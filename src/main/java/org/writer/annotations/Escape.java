package org.writer.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация позволяющая указать стратегию экранирования для полей, строковое представление которых содержит запятые.
 * Примитивные типы не экранируются. Стратегия по-умолчанию {@link  EscapeStrategy#QUOTE_ESCAPING} - экранирование с помощью кавычек.
 * </br></br>
 * Более подробную информацию о стратегиях экранирования смотри в {@link EscapeStrategy}
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Escape {
    EscapeStrategy strategy() default EscapeStrategy.QUOTE_ESCAPING;
}
