package org.writer.utils;

import lombok.Getter;

/**
 * Enumeration of supported file formats for data export.
 * Each format contains file extension information.
 */
@Getter
public enum FileType {

    /**
     * Comma-Separated Values format.
     * Text format for representing tabular data.
     */
    CSV(".csv");

    /**
     * -- GETTER --
     *  Returns the file extension for this format.
     *
     * @return file extension string including the dot.
     */
    private final String fileFormat;

    /**
     * Creates a file type with the specified extension.
     *
     * @param fileFormat file extension.
     */
    FileType(String fileFormat) {
        this.fileFormat = fileFormat;
    }

}
