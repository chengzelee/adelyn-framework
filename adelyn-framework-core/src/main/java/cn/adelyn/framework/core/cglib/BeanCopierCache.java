package cn.adelyn.framework.core.cglib;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chengze
 * @date 2023/1/30
 * @desc BeanCopier属性缓存
 *  	 缓存用于防止多次反射造成的性能问题，优化 CglibUtil 对象 copy 的性能
 *  	 也是从 hutool-extra copy 的
 *       但我用了 springframework 里的 cglib 包，api 与原生 cglib 一样
 *       jdk17 非法反射访问报错的问题，目前没有好办法，加jvm参数 --add-opens=java.base/java.lang=ALL-UNNAMED
 */
public enum BeanCopierCache {
	/**
	 * BeanCopier属性缓存单例
	 */
	INSTANCE;

	private final ConcurrentHashMap<String, BeanCopier> cache = new ConcurrentHashMap<>();

	/**
	 * 获得类与转换器生成的key在{@link BeanCopier}的Map中对应的元素
	 *
	 * @param srcClass    源Bean的类
	 * @param targetClass 目标Bean的类
	 * @param converter   转换器
	 * @return Map中对应的BeanCopier
	 */
	public BeanCopier get(Class<?> srcClass, Class<?> targetClass, Converter converter) {
		return get(srcClass, targetClass, null != converter);
	}

	/**
	 * 获得类与转换器生成的key在{@link BeanCopier}的Map中对应的元素
	 *
	 * @param srcClass     源Bean的类
	 * @param targetClass  目标Bean的类
	 * @param useConverter 是否使用转换器
	 * @return Map中对应的BeanCopier
	 * @since 5.8.0
	 */
	public BeanCopier get(Class<?> srcClass, Class<?> targetClass, boolean useConverter) {
		final String key = genKey(srcClass, targetClass, useConverter);
		return cache.computeIfAbsent(key, (k) -> BeanCopier.create(srcClass, targetClass, useConverter));
	}

	/**
	 * 获得类与转换器生成的key<br>
	 * 结构类似于：srcClassName#targetClassName#1 或者 srcClassName#targetClassName#0
	 *
	 * @param srcClass     源Bean的类
	 * @param targetClass  目标Bean的类
	 * @param useConverter 是否使用转换器
	 * @return 属性名和Map映射的key
	 */
	private String genKey(Class<?> srcClass, Class<?> targetClass, boolean useConverter) {
		final StringBuilder key = new StringBuilder()
				.append(srcClass.getName())
				.append('#').append(targetClass.getName())
				.append('#').append(useConverter ? 1 : 0);
		return key.toString();
	}
}
