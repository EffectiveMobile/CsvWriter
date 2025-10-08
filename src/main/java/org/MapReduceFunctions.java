package org;

import java.util.ArrayList;
import java.util.List;

public class MapReduceFunctions {


    public static List<KeyValue> map(String fileName, String content) {
        List<KeyValue> result = new ArrayList<>();
        String[] words = content.split("\\W+");
        for (String word : words) {
            if (!word.isEmpty()) {
                result.add(new KeyValue(word.toLowerCase(), "1"));
            }
        }
        return result;
    }


    public static String reduce(String key, List<String> values) {
        int sum = values.stream().mapToInt(Integer::parseInt).sum();
        return String.valueOf(sum);
    }
}
