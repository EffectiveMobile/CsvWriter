package org.writer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Метка для класса, который должен быть сериализован в CSV формат.
 * <p>CsvWriter будет обрабатывать только классы, помеченные этой аннотацией.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CsvSerializable {
}
