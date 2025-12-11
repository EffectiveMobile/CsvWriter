package org.writer.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация позволяющая указать стратегию маппинга коллекций
 * Стратегия по-умолчанию {@link  CollectionMappingStrategy#STRINGIFY} - маппинг с помощью вызова {@code toString()}.
 * </br></br>
 * Более подробную информацию о стратегиях маппинга смотри в {@link CollectionMappingStrategy}
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CollectionMapping {
    CollectionMappingStrategy strategy() default CollectionMappingStrategy.STRINGIFY;
}
