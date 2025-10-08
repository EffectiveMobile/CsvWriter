package org.writer.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация, которая определяет какие поля класса будут включены в CSV-файл.
 * С помощью данной аннотации можно установить стратегии экранирования, маппинга перечислений, и маппинга коллекций
 * </br></br>
 * {@link CsvInclude#escape} - устанавливает стратегию экранирования запятых, подробнее {@link Escape}
 * </br></br>
 * {@link CsvInclude#enumMapping} - устанавливает стратегию маппинга перечислений, подробнее {@link EnumMapping}
 * </br></br>
 * {@link CsvInclude#collectionMapping} - устанавливает стратегию маппинга коллекций, подробнее {@link CollectionMapping}
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvInclude {
    Escape escape() default @Escape(strategy = EscapeStrategy.COLUMN_ESCAPING);
    EnumMapping enumMapping() default @EnumMapping(strategy = EnumMappingStrategy.NAME);
    CollectionMapping collectionMapping() default @CollectionMapping(strategy = CollectionMappingStrategy.ITEM_COLUMN);
}
