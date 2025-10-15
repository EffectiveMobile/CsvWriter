package org.writer;

import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        var docWriter = new Report(new CsvDocument());
        docWriter.writeObjectAsDoc(Collections.emptyList(), "People");
    }
}