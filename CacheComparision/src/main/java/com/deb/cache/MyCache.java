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

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import com.couchbase.client.java.document.JsonDocument;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

/**
 * @author debmalyajash
 *
 */
public class MyCache {

	private static DB db = null;

	private static BTreeMap<String, JsonDocument> jsonDocumentBTreeMap;
	private static BTreeMap<String, String> jsonStringTBreeMap;
	private static HTreeMap<String, JsonDocument> jsonDocumentHTreeMap;
	private static LRUPandaCache<String, JsonDocument> lruPandaCache;
	private static HTreeMap<String, JsonDocument> jsonDocumentDirectHTreeMap;
	private static GuavaCacheDao<String, JsonDocument> jsonDocumentGuava;

	/**
	 * This will hold ID/MSISDN as key and JsonObject as value.
	 */
	private static Cache<String, JsonDocument> ehcache;

	/**
	 * Cache Manager.
	 */
	private CacheManager cacheManager = null;

	private CacheType cacheType;

	@SuppressWarnings("unchecked")
	public MyCache(CacheType passedCacheType, String fileName, long starterSize) {
		cacheType = passedCacheType;

		switch (passedCacheType) {
		case MAPDB_FILE_TREEMAP_DOC:
			File file = new File(fileName);
			file.delete();
			db = DBMaker.fileDB(fileName).fileMmapEnable().fileMmapEnableIfSupported().deleteFilesAfterClose().make();
			jsonDocumentBTreeMap = (BTreeMap<String, JsonDocument>) db.treeMap("MAPDB_FILE_TREEMAP_DOC")
					.keySerializer(Serializer.STRING).valueSerializer(Serializer.JAVA).createOrOpen();
			break;
		case MAPDB_MEMORY_TREEMAP_STR:
			db = DBMaker.memoryDB().allocateStartSize(starterSize).make();
			break;
		case LRULINKED_HASH_MAP:
			lruPandaCache = new LRUPandaCache<String, JsonDocument>(100000, 0.75f);
			break;
		case MAPDB_MEMORY_HTREE_DOC:
			db = DBMaker.memoryDB().make();
			jsonDocumentHTreeMap = (HTreeMap<String, JsonDocument>) db.hashMap("MAPDB_MEMORY_HTREE_DOC")
					.expireMaxSize(starterSize).keySerializer(Serializer.STRING).valueSerializer(Serializer.JAVA)
					.expireAfterCreate().createOrOpen();

			break;

		case MAPDB_MEMORY_DIRECT_HTREE_DOC:
			db = DBMaker.memoryDirectDB().make();
			jsonDocumentDirectHTreeMap = (HTreeMap<String, JsonDocument>) db.hashMap("MAPDB_MEMORY_HTREE_DOC")
					.expireMaxSize(starterSize).keySerializer(Serializer.STRING).valueSerializer(Serializer.JAVA)
					.expireAfterCreate(10, TimeUnit.MINUTES).createOrOpen();

			break;

		case EHCACHE:
			cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
					.withCache("ehcache", CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class,
							JsonDocument.class, ResourcePoolsBuilder.heap(starterSize)).build())
					.build(true);
			ehcache = cacheManager.getCache("ehcache", String.class, JsonDocument.class);
			break;

		case GUAVA_CACHE:
			jsonDocumentGuava = new GuavaCacheDao<String, JsonDocument>(starterSize, 10);
			break;

		default:
			typeNotSupported();
			break;
		}
	}

	public JsonDocument put(String key, JsonDocument value) {
		JsonDocument document = null;
		switch (cacheType) {
		case MAPDB_FILE_TREEMAP_DOC:
			document = jsonDocumentBTreeMap.put(key, value);
			break;
		case LRULINKED_HASH_MAP:
			document = lruPandaCache.put(key, value);
			break;
		case MAPDB_MEMORY_HTREE_DOC:
			document = jsonDocumentHTreeMap.put(key, value);
			break;

		case MAPDB_MEMORY_DIRECT_HTREE_DOC:
			document = jsonDocumentDirectHTreeMap.put(key, value);
			break;
		case EHCACHE:
			ehcache.put(key, value);
			break;

		case GUAVA_CACHE:
			jsonDocumentGuava.put(key, value);
			break;
		default:
			typeNotSupported();
			break;
		}
		return document;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public JsonDocument get(String key) {
		JsonDocument document = null;
		switch (cacheType) {
		case MAPDB_FILE_TREEMAP_DOC:
			document = jsonDocumentBTreeMap.get(key);
			break;
		case LRULINKED_HASH_MAP:
			document = lruPandaCache.get(key);
			break;
		case MAPDB_MEMORY_HTREE_DOC:
			document = jsonDocumentHTreeMap.get(key);
			break;
		case MAPDB_MEMORY_DIRECT_HTREE_DOC:
			document = jsonDocumentDirectHTreeMap.get(key);
			break;
		case EHCACHE:
			document = ehcache.get(key);
			break;
		case GUAVA_CACHE:
			document = jsonDocumentGuava.get(key);
			break;
		default:
			typeNotSupported();
			break;
		}
		return document;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public JsonDocument remove(String key) {
		JsonDocument document = null;
		switch (cacheType) {
		case MAPDB_FILE_TREEMAP_DOC:
			document = jsonDocumentBTreeMap.remove(key);
			break;
		case LRULINKED_HASH_MAP:
			document = lruPandaCache.remove(key);
			break;
		case MAPDB_MEMORY_HTREE_DOC:
			document = jsonDocumentHTreeMap.remove(key);
			break;
		case MAPDB_MEMORY_DIRECT_HTREE_DOC:
			document = jsonDocumentDirectHTreeMap.remove(key);
			break;
		case EHCACHE:
			ehcache.remove(key);
			break;
		case GUAVA_CACHE:
			jsonDocumentGuava.remove(key);
			break;
		default:
			typeNotSupported();
			break;
		}
		return document;
	}

	/**
	 * 
	 */
	private void typeNotSupported() {
		System.err.println(cacheType + " not Supported");
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String put(String key, String value) {
		String document = null;
		switch (cacheType) {
		case MAPDB_MEMORY_TREEMAP_STR:
			document = jsonStringTBreeMap.put(key, value);
			break;
		default:
			typeNotSupported();
			break;
		}
		return document;
	}

	public double cacheHitRatio() {
		return 0.00d;
	}
}
