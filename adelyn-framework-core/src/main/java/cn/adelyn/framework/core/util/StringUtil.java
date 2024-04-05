package cn.adelyn.framework.core.util;

import org.springframework.util.StringUtils;

public class StringUtil extends StringUtils {

    public static boolean hasText(String str) {
        return str != null && !str.isBlank();
    }

    public static boolean hasLength(String str) {
        return str != null && !str.isEmpty();
    }
}
