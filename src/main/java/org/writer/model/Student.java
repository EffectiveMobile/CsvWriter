package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotations.CollectionMapping;
import org.writer.annotations.CollectionMappingStrategy;
import org.writer.annotations.CsvInclude;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Student {

    @CsvInclude
    private String name;

    @CsvInclude(collectionMapping = @CollectionMapping(strategy = CollectionMappingStrategy.ITEM_COLUMN))
    private List<String> score;
}