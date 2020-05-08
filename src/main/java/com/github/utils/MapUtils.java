package com.github.utils;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {
    public static Map<String, Object> asMap(Object... args) {
        Map<String, Object> result = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            result.put(args[i].toString(), args[i + 1]);
        }
        return result;
    }
}
