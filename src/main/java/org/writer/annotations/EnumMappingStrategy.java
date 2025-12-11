package org.writer.annotations;

/**
 * Определяет стратегию маппинга перечислений
 * </br></br>
 * {@link EnumMappingStrategy#NAME} - стратегия маппинга по имени, {@code EnumExample.SOME_ENUM_VALUE} будет маппиться в {@code SOME_ENUM_VALUE}
 * </br></br>
 * {@link EnumMappingStrategy#ORDINAL} - стратегия маппинга по ординалу значения перечисления
 */
public enum EnumMappingStrategy {
    NAME,
    ORDINAL
}
