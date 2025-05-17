package org.writer.annotation;

import org.writer.annotation.constans.NamingStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Задает имя колонки в CSV для аннотированного поля.
 * Также позволяет выбрать стратегию именования, если имя не задано явно.
 *
 * @author Астонский Шпион
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CsvColumn {

    /**
     * Явное имя колонки в CSV. Если указано, оно имеет приоритет
     * над стратегией именования.
     */
    String name() default "";

    /**
     * Стратегия именования для этого поля.
     * Если name() не указан, будет использована эта стратегия.
     * Если и name() и strategy() не указаны (или strategy() = DEFAULT),
     * будет использована defaultNamingStrategy из @CsvRecord,
     * или стратегия по умолчанию библиотеки.
     */
    NamingStrategy strategy() default NamingStrategy.DEFAULT;
}
