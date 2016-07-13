/**
 * Copyright 2015-2016 Debmalya Jash
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.deb.cache;

import java.util.Map;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * @author debmalyajash
 *
 */
public class CaffeineCache<K, V> implements SimpleCache<K, V> {
	private final Cache<K, V> cache;
	private final Map<K, V> map;

	public CaffeineCache(int maximumSize) {
		cache = Caffeine.newBuilder().initialCapacity(maximumSize).maximumSize(maximumSize).build();
		map = cache.asMap();
	}

	@Override
	public V get(K key) {
		return map.get(key);
	}

	@Override
	public void put(K key, V value) {
		map.put(key, value);
	}

	@Override
	public void clear() {
		cache.invalidateAll();
	}

	@Override
	public void cleanUp() {
		cache.cleanUp();
	}
	
	public void remove(K key){
		cache.invalidate(key);
	}
}
