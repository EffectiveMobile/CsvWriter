package org.writer.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
*Аннотация для пометки полей, которые должны быть обработаны в csv
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CSVField {
    String name() default "";
}
