package com.yuejian.meet.utils;

public class TextUtil {

    public static String notNull(String content) {

        if (null == content) {
            content = "";
        }

        if (content.equalsIgnoreCase("null")) {
            content = "";
        }
        return content;
    }


}
