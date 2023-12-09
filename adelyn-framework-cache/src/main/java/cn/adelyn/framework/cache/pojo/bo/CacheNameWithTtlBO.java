package cn.adelyn.framework.cache.pojo.bo;

/**
 * @author chengze
 * @date 2023/2/1
 * @desc
 */
public class CacheNameWithTtlBO {

	private String cacheName;

	private Integer ttl;

	public CacheNameWithTtlBO(String cacheName, Integer ttl) {
		this.cacheName = cacheName;
		this.ttl = ttl;
	}

	public String getCacheName() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	public Integer getTtl() {
		return ttl;
	}

	public void setTtl(Integer ttl) {
		this.ttl = ttl;
	}

	@Override
	public String toString() {
		return "CacheNameWithTtlBO{" + "cacheName='" + cacheName + '\'' + ", ttl=" + ttl + '}';
	}

}
