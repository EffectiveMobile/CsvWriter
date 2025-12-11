package org.writer.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация позволяющая указать стратегию маппинга перечислений. Стратегия по-умолчанию
 * {@link  EnumMappingStrategy#NAME} - маппинг по имени.
 * </br></br>
 * Более подробную информацию о стратегиях маппинга смотри в {@link EnumMappingStrategy}
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumMapping {
    EnumMappingStrategy strategy() default EnumMappingStrategy.NAME;
}
