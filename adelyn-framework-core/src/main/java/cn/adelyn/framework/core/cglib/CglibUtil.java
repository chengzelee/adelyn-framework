package cn.adelyn.framework.core.cglib;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cglib.core.Converter;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author chengze
 * @date 2023/1/30
 * @desc Cglib 工具类，从 hutool-extra copy的
 *       但我用了 springframework 里的 cglib 包，api 与原生 cglib 一样
 *       jdk17 非法反射访问报错的问题，目前没有好办法，加jvm参数 --add-opens=java.base/java.lang=ALL-UNNAMED
 */
public class CglibUtil {

	/**
	 * 拷贝Bean对象属性到目标类型<br>
	 * 此方法通过指定目标类型自动创建之，然后拷贝属性
	 *
	 * @param <T>         目标对象类型
	 * @param source      源bean对象
	 * @param targetClass 目标bean类，自动实例化此对象
	 * @return 目标对象
	 */
	public static <T> T copy(Object source, Class<T> targetClass) {
		return copy(source, targetClass, null);
	}

	/**
	 * 拷贝Bean对象属性<br>
	 * 此方法通过指定目标类型自动创建之，然后拷贝属性
	 *
	 * @param <T>         目标对象类型
	 * @param source      源bean对象
	 * @param targetClass 目标bean类，自动实例化此对象
	 * @param converter   转换器，无需可传{@code null}
	 * @return 目标对象
	 */
	public static <T> T copy(Object source, Class<T> targetClass, Converter converter) {
		final Object target = ReflectUtils.newInstance(targetClass);
		copy(source, target, converter);
		return (T) target;
	}

	/**
	 * 拷贝Bean对象属性
	 *
	 * @param source 源bean对象
	 * @param target 目标bean对象
	 */
	public static void copy(Object source, Object target) {
		copy(source, target, null);
	}

	/**
	 * 拷贝Bean对象属性
	 *
	 * @param source    源bean对象
	 * @param target    目标bean对象
	 * @param converter 转换器，无需可传{@code null}
	 */
	public static void copy(Object source, Object target, Converter converter) {
		Assert.notNull(source, "Source bean must be not null.");
		Assert.notNull(target, "Target bean must be not null.");

		final Class<?> sourceClass = source.getClass();
		final Class<?> targetClass = target.getClass();
		final BeanCopier beanCopier = BeanCopierCache.INSTANCE.get(sourceClass, targetClass, converter);

		beanCopier.copy(source, target, converter);
	}

	/**
	 * 拷贝List Bean对象属性
	 *
	 * @param <S>    源bean类型
	 * @param <T>    目标bean类型
	 * @param source 源bean对象list
	 * @param target 目标bean对象
	 * @return 目标bean对象list
	 */
	public static <S, T> List<T> copyList(Collection<S> source, Supplier<T> target) {
		return copyList(source, target, null, null);
	}

	/**
	 * 拷贝List Bean对象属性
	 *
	 * @param source    源bean对象list
	 * @param target    目标bean对象
	 * @param converter 转换器，无需可传{@code null}
	 * @param <S>       源bean类型
	 * @param <T>       目标bean类型
	 * @return 目标bean对象list
	 * @since 5.4.1
	 */
	public static <S, T> List<T> copyList(Collection<S> source, Supplier<T> target, Converter converter) {
		return copyList(source, target, converter, null);
	}

	/**
	 * 拷贝List Bean对象属性
	 *
	 * @param source   源bean对象list
	 * @param target   目标bean对象
	 * @param callback 回调对象
	 * @param <S>      源bean类型
	 * @param <T>      目标bean类型
	 * @return 目标bean对象list
	 * @since 5.4.1
	 */
	public static <S, T> List<T> copyList(Collection<S> source, Supplier<T> target, BiConsumer<S, T> callback) {
		return copyList(source, target, null, callback);
	}

	/**
	 * 拷贝List Bean对象属性
	 *
	 * @param source    源bean对象list
	 * @param target    目标bean对象
	 * @param converter 转换器，无需可传{@code null}
	 * @param callback  回调对象
	 * @param <S>       源bean类型
	 * @param <T>       目标bean类型
	 * @return 目标bean对象list
	 */
	public static <S, T> List<T> copyList(Collection<S> source, Supplier<T> target, Converter converter, BiConsumer<S, T> callback) {
		return source.stream().map(s -> {
			T t = target.get();
			copy(s, t, converter);
			if (callback != null) {
				callback.accept(s, t);
			}
			return t;
		}).collect(Collectors.toList());
	}

	/**
	 * 将Bean转换为Map
	 *
	 * @param bean Bean对象
	 * @return {@link BeanMap}
	 * @since 5.4.1
	 */
	public static BeanMap toMap(Object bean) {
		return BeanMap.create(bean);
	}

	/**
	 * 将Map中的内容填充至Bean中
	 * @param map Map
	 * @param bean Bean
	 * @param <T> Bean类型
	 * @return bean
	 * @since 5.6.3
	 */
	@SuppressWarnings("rawtypes")
	public static <T> T fillBean(Map map, T bean){
		BeanMap.create(bean).putAll(map);
		return bean;
	}

	/**
	 * 将Map转换为Bean
	 * @param map Map
	 * @param beanClass Bean类
	 * @param <T> Bean类型
	 * @return bean
	 * @since 5.6.3
	 */
	@SuppressWarnings("rawtypes")
	public static <T> T toBean(Map map, Class<T> beanClass){
		return (T) fillBean(map, ReflectUtils.newInstance(beanClass));
	}
}
