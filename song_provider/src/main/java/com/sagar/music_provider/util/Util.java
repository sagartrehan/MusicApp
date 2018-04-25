package com.sagar.music_provider.util;

import android.text.TextUtils;

import java.util.List;

public class Util {

    public static String listToCsv(List<String> stringList) {
        if (ValidationUtil.isListEmpty(stringList)) {
            return "";
        }
        return TextUtils.join(",", stringList);
    }

}
