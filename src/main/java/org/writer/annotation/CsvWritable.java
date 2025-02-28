package org.writer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Указывает, что класс может быть сериализован в CSV.
 * Классы, помеченные этой аннотацией, могут быть обработаны {@link org.writer.service.CsvWriter}.
 *
 * @author Мельников Никита
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvWritable {
}
