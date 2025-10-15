package org.writer;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class Report {
    private final Writable writable;

    public void writeObjectAsDoc(List<?> data, String fileName) {
        writable.writeToFile(data, fileName);
    }
}
