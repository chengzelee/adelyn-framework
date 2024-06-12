package cn.adelyn.framework.core.util;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {

    public static String getRandomIntString() {
        return String.valueOf(getRandomInt());
    }

    public static String getRandomLongString() {
        return String.valueOf(getRandomLong());
    }

    public static String getRandomDoubleString() {
        return String.valueOf(getRandomDouble());
    }

    public static String getRandomFloatString() {
        return String.valueOf(getRandomFloat());
    }

    public static int getRandomInt() {
        return ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
    }

    public static long getRandomLong() {
        return ThreadLocalRandom.current().nextLong(0L, Long.MAX_VALUE);
    }

    public static double getRandomDouble() {
        return ThreadLocalRandom.current().nextDouble(0D, Double.MAX_EXPONENT);
    }

    public static float getRandomFloat() {
        return ThreadLocalRandom.current().nextFloat(0F, Float.MAX_EXPONENT);
    }
}
