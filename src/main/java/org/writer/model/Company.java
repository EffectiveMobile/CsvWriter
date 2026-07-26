package org.writer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.writer.annotation.CsvWritable;

@Data
@Builder
@AllArgsConstructor
@CsvWritable
public class Company {
    private String name;
    private String street;
    private String registrationNumber;
}
