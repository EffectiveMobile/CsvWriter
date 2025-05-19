package org.writer.annotation;

import org.writer.annotation.constans.MaskingStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Указывает, что значение аннотированного поля должно быть замаскировано
 * или преобразовано перед сериализацией.
 *
 * @author Астонский Шпион
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MaskedField {

    /**
     * Стратегия маскирования.
     */
    MaskingStrategy strategy();

    /**
     * Символ, используемый для маскирования (например, '*').
     * Используется стратегиями ASTERISKS_*.
     */
    char maskCharacter() default '*';

    /**
     * Количество видимых символов в начале строки для ASTERISKS_PARTIAL_PREFIX.
     * Или количество видимых символов в конце строки для ASTERISKS_PARTIAL_SUFFIX.
     * Например, для карт XXXX-XXXX-XXXX-1234, visibleChars = 4, strategy = ASTERISKS_PARTIAL_SUFFIX
     */
    int visibleChars() default 4;
}