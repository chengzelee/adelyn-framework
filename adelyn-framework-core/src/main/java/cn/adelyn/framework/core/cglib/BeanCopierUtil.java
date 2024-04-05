package cn.adelyn.framework.core.cglib;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cglib.core.Converter;
import org.springframework.cglib.core.ReflectUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * cglib包中的bean拷贝(性能优于Spring当中的BeanUtils)
 * 深拷贝
 */
public class BeanCopierUtil {

    private static final Map<String, BeanCopier> BEAN_COPIER_MAP = new ConcurrentHashMap<>();
    private static final Map<String, BeanCopier> BEAN_COPIER_WITH_CONVERTER_MAP = new ConcurrentHashMap<>();

    private static void copy(Object source, Object target) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);

        String key = getKey(source, target);
        // 判断键是否存在,不存在就将BeanCopier插入到map里,存在就直接获取
        BeanCopier beanCopier = BEAN_COPIER_MAP.computeIfAbsent(key, copier -> BeanCopier.create(source.getClass(), target.getClass(), false));
        beanCopier.copy(source, target, null);
    }

    public static <T> T copy(Object source, Class<T> targetClass) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(targetClass);

        T result;
        try {
            result = targetClass.getDeclaredConstructor().newInstance();
            copy(source, result);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("create instance error", e);
        }
        return result;
    }

    public static <S, T> List<T> copyList(List<S> sourceClasses, Class<T> targetClass) {
        Objects.requireNonNull(sourceClasses);
        Objects.requireNonNull(targetClass);

        return sourceClasses.stream().map(src -> copy(src, targetClass)).collect(Collectors.toList());
    }

    private static void copy(Object source, Object target, Converter converter) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
        Objects.requireNonNull(converter);

        String key = getKey(source, target);
        BeanCopier beanCopier = BEAN_COPIER_WITH_CONVERTER_MAP.computeIfAbsent(key, copier -> BeanCopier.create(source.getClass(), target.getClass(), true));
        beanCopier.copy(source, target, converter);
    }

    public static <T> T copy(Object source, Class<T> targetClass, Converter converter) {
        Objects.requireNonNull(source);
        Objects.requireNonNull(targetClass);
        Objects.requireNonNull(converter);

        T result;
        try {
            result = targetClass.getDeclaredConstructor().newInstance();
            copy(source, result, converter);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("create instance error", e);
        }
        return result;
    }

    public static <S, T> List<T> copyList(List<S> sourceClasses, Class<T> targetClass, Converter converter) {
        Objects.requireNonNull(sourceClasses);
        Objects.requireNonNull(targetClass);
        Objects.requireNonNull(converter);

        return sourceClasses.stream().map(src -> copy(src, targetClass, converter)).collect(Collectors.toList());
    }

    public static BeanMap toMap(Object bean) {
        return BeanMap.create(bean);
    }

    @SuppressWarnings("rawtypes")
    public static <T> T fillBean(Map map, T target){
        BeanMap.create(target).putAll(map);
        return target;
    }

    @SuppressWarnings("rawtypes")
    public static <T> T toBean(Map map, Class<T> targetClass){
        return (T) fillBean(map, ReflectUtils.newInstance(targetClass));
    }

    private static String getKey(Object source, Object target) {
        return source.getClass().getName() + "_" + target.getClass().getName();
    }

}
