package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Car {

    private String model;

    private String engine;

    private int yearOfManufacture;
}
