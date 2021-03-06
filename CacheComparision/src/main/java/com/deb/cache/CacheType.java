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

/**
 * @author debmalyajash
 *
 */
public enum CacheType {
	/**
	 * MapDB 
	 * 
	 */
	MAPDB_MEMORY_HTREE_DOC,
	/**
	 * MapDB in memory tree map containing String.
	 */
	MAPDB_MEMORY_TREEMAP_STR,
	/**
	 * MapDB file with BTreeMap containing JsonDocument.
	 */
	MAPDB_FILE_TREEMAP_DOC,
	/**
	 * MapDB file with BTreemap containing string.
	 */
	MAPDB_FILE_TREEMAP_STR,
	
	/**
	 * LRU linked hash Map.
	 */
	LRULINKED_HASH_MAP, 
	
	/**
	 * To use ehcache
	 */
	EHCACHE,
	
	/**
	 * To use com.github.ben-manes.caffeine
	 */
	CAFFEINE,
	
	/**
	 * Guava cache.
	 */
	GUAVA_CACHE,
	
	/**
	 * MapDB Memory Direct DB
	 */
	MAPDB_MEMORY_DIRECT_HTREE_DOC;
	
}
