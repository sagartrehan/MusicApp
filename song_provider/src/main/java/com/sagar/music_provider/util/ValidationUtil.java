package com.sagar.music_provider.util;

import java.util.List;

public class ValidationUtil {

    public static boolean isStringEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static boolean isStringNotEmpty(String string) {
        return !isStringEmpty(string);
    }

    public static <T> boolean isListEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    public static <T> boolean isListNotEmpty(List<T> list) {
        return !isListEmpty(list);
    }

}
