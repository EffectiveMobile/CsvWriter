package org.writer.annotation;

import org.writer.annotation.constans.NamingStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Указывает, что класс является записью для CSV-файла.
 * Позволяет настроить общие параметры для всех полей класса.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CsvRecord {

    /**
     * Стратегия именования колонок по умолчанию для всех полей этого класса.
     * Может быть переопределена аннотацией @CsvColumn на конкретном поле.
     */
    NamingStrategy defaultNamingStrategy() default NamingStrategy.AS_IS;

    /**
     * Определяет, нужно ли генерировать строку заголовка для этого типа объекта.
     * По умолчанию true. Может быть false, если заголовок уже написан
     * или не требуется.
     */
    boolean includeHeader() default true;
}
