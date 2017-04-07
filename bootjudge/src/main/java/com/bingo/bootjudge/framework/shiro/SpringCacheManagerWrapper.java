package com.bingo.bootjudge.framework.shiro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.ehcache.Ehcache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.util.CollectionUtils;

/**
 * 包装Spring cache抽象
 */
public class SpringCacheManagerWrapper implements CacheManager {

	/**
	 * 缓存管理实体类
	 */
	private org.springframework.cache.CacheManager cacheManager;

	/**
	 * 设置spring缓存管理类
	 *
	 * @param cacheManager 缓存管理类
	 */
	public void setCacheManager(
			org.springframework.cache.CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	/**
	 * 获取spring缓存管理
	 */
	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		org.springframework.cache.Cache springCache = cacheManager
				.getCache(name);
		return new SpringCacheWrapper(springCache);
	}

	/**
	 * spring cache 包装类
	 * @author junjie
	 *
	 */
	static class SpringCacheWrapper implements Cache {
		private org.springframework.cache.Cache springCache;

		SpringCacheWrapper(org.springframework.cache.Cache springCache) {
			this.springCache = springCache;
		}

		/**
		 * 获取缓存对象
		 */
		@Override
		public Object get(Object key) throws CacheException {
			Object value = springCache.get(key);
			if (value instanceof SimpleValueWrapper) {
				return ((SimpleValueWrapper) value).get();
			}
			return value;
		}

		/**
		 * 存入一个对象到缓存里
		 */
		@Override
		public Object put(Object key, Object value) throws CacheException {
			springCache.put(key, value);
			return value;
		}

		/**
		 * 清除指定对象缓存
		 */
		@Override
		public Object remove(Object key) throws CacheException {
			springCache.evict(key);
			return null;
		}

		/**
		 * 清除当用用户缓存
		 */
		@Override
		public void clear() throws CacheException {
			springCache.clear();
		}

		/**
		 * 缓存对象数量
		 */
		@Override
		public int size() {
			if(springCache.getNativeCache() instanceof Ehcache) {
                Ehcache ehcache = (Ehcache) springCache.getNativeCache();
                return ehcache.getSize();
            }
			throw new UnsupportedOperationException(
					"invoke spring cache abstract size method not supported");
		}

		/**
		 * 获取所有keys
		 */
		@Override
		public Set keys() {
			 if(springCache.getNativeCache() instanceof Ehcache) {
	                Ehcache ehcache = (Ehcache) springCache.getNativeCache();
	                return new HashSet(ehcache.getKeys());
	            }
			throw new UnsupportedOperationException(
					"invoke spring cache abstract keys method not supported");
		}

		/**
		 * 获取所有对象
		 */
		@Override
		public Collection values() {
			if(springCache.getNativeCache() instanceof Ehcache) {   
		           Ehcache ehcache = (Ehcache) springCache.getNativeCache();   
		           List keys = ehcache.getKeys();   
		           if (!CollectionUtils.isEmpty(keys)) {   
		               List values = new ArrayList(keys.size());   
		               for (Object key : keys) {   
		                   Object value = get(key);   
		                   if (value != null) {   
		                       values.add(value);   
		                   }   
		               }   
		               return Collections.unmodifiableList(values);   
		           } else {   
		               return Collections.emptyList();   
		           }   
		       }
			throw new UnsupportedOperationException(
					"invoke spring cache abstract values method not supported");
		}
	}
}
