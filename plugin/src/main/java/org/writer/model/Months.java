package org.writer.model;

import org.writer.annotation.CsvField;
import org.writer.annotation.WritableToCsv;

@WritableToCsv
public enum Months {
    JANUARY("January"),
    FEBRUARY("February"),
    MARCH("March"),
    APRIL("April"),
    MAY("May"),
    JUNE("June"),
    JULY("July"),
    AUGUST("August"),
    SEPTEMBER("September"),
    OCTOBER("October"),
    NOVEMBER("November"),
    DECEMBER("December");

    @CsvField
    private final String value;

    Months(String value) {
        this.value = value;
    }

}