package cn.adelyn.framework.core.util;

import java.util.UUID;

public class IdUtil {

    /**
     * 获取随机UUID
     */
    public static String UUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 简化的UUID
     */
    public static String simpleUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
