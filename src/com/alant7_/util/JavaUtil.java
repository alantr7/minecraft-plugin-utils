package com.alant7_.util;

import com.alant7_.util.reflections.Pair;

import java.util.*;

public class JavaUtil {

    public static <K, V> Map<K, V> createMap(Pair<K, V>... pairs) {
        Map<K, V> map = new HashMap<>();
        for (Pair<K, V> pair : pairs)
            map.put(pair.getKey(), pair.getValue());

        return map;
    }

}
