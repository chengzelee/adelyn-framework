package cn.adelyn.framework.cache.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import org.checkerframework.checker.index.qual.NonNegative;
import org.springframework.lang.NonNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class CaffeineCacheUtil {

    private static final Cache<Object, Object> cache = Caffeine.newBuilder()
        .expireAfter(new CaffeineExpiry<>())
        .build();

    public static <K, V> void put(K key, V value, long expireAfterWriteDuration, TimeUnit expireAfterWriteTimeUnit) {
        cache.policy().expireVariably().ifPresent(e-> {
            e.put(key, value, expireAfterWriteDuration, expireAfterWriteTimeUnit);
        });
    }

    public static Object get(Object key) {
        return cache.getIfPresent(key);
    }

    public static Object get(Object key, Function<Object, Object> mappingFunction, long expireAfterWriteDuration, TimeUnit expireAfterWriteTimeUnit) {
        Object value = get(key);
        if (Objects.isNull(value)) {
            value = mappingFunction.apply(key);
            put(key, value, expireAfterWriteDuration, expireAfterWriteTimeUnit);
        }
        return value;
    }

    public static boolean contains(Object key) {
        return Objects.isNull(cache.getIfPresent(key));
    }

    public static void remove(Object key) {
        cache.invalidate(key);
    }

    public static void clear() {
        cache.invalidateAll();
    }

    private static class CaffeineExpiry<K, V> implements Expiry<K, V> {
        @Override
        public long expireAfterCreate(@NonNull K key, @NonNull V value, long currentTime) {
            return 0;
        }

        @Override
        public long expireAfterUpdate(@NonNull K key, @NonNull V value, long currentTime, @NonNegative long currentDuration) {
            return currentDuration;
        }

        @Override
        public long expireAfterRead(@NonNull K key, @NonNull V value, long currentTime, @NonNegative long currentDuration) {
            return currentDuration;
        }
    }
}
