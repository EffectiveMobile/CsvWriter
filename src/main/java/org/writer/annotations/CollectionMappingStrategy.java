package org.writer.annotations;

/**
 * Определяет стратегию маппинга коллекций
 * </br></br>
 * {@link CollectionMappingStrategy#STRINGIFY} - стратегия маппинга коллекции путем вызова метода {@code toString()} коллекции
 * </br></br>
 * {@link CollectionMappingStrategy#ITEM_COLUMN} - стратегия маппинга "одно значение - одна колонка", то есть:
 * {@code List.of(1,2,3,4)} будет замаппен следующим образом {@code 1,2,3,4}
 */
public enum CollectionMappingStrategy {
    STRINGIFY,
    ITEM_COLUMN
}
