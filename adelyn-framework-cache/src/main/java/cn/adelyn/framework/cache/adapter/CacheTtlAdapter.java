package cn.adelyn.framework.cache.adapter;


import cn.adelyn.framework.cache.pojo.bo.CacheNameWithTtlBO;

import java.util.List;

/**
 * @author chengze
 * @date 2023/2/1
 * @desc 实现该接口之后，根据缓存的cacheName和ttl将缓存进行过期
 */
public interface CacheTtlAdapter {

	/**
	 * 根据缓存的cacheName和ttl将缓存进行过期
	 * @return 需要独立设置过期时间的缓存列表
	 */
	List<CacheNameWithTtlBO> listCacheNameWithTtl();

}
