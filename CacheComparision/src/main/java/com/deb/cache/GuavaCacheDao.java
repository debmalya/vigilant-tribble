/**
 * Copyright 2015-2016 Knowesis Pte Ltd.
 *
 */
package com.deb.cache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * @author debmalyajash
 *
 */
public class GuavaCacheDao<K, V> {
	private Cache<K, V> jsonCache = null;

	

	private static final Object lock = new Object();

	public GuavaCacheDao(long size, final int expirationInMinute) {

		// In real life this would come from a command-line flag or config file
		if (jsonCache == null) {
			synchronized (lock) {
				jsonCache = CacheBuilder.newBuilder().maximumSize(size).expireAfterWrite(expirationInMinute, TimeUnit.MINUTES).build();
			}
		}
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void put(final K key, final V value) {
		jsonCache.put(key, value);
	}

	/**
	 * Discards any value for the cached key.
	 * 
	 * @param key
	 */
	public void remove(final K key) {
		jsonCache.invalidate(key);
	}

	/**
	 * Returns the value associated with the key. Multiple threads can
	 * concurrently load the value of the key.
	 * 
	 * Internally it will call CacheLoader.load to load new values into the
	 * cache. Newly loaded values are added to the cache using
	 * Cache.asMap().putIfAbsent after loading has completed; if another value
	 * was associated with key while the new value was loading then a removal
	 * notification will be sent for the new value.
	 * 
	 * @param key
	 * @return retrieved value
	 * @throws ExecutionException
	 * 
	 *             If the cache loader associated with this cache is known not
	 *             to throw checked exceptions, then prefer getUnchecked over
	 *             this method.
	 */
	public V get(final K key) {
		try {
			return jsonCache.get(key, new GuavaCacheMissCaller<V>());
		} catch (ExecutionException exc) {
			return null;
		}
	}
}
