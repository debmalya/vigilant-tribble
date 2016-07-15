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
package com.deb;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.function.Function;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;

import com.couchbase.client.java.document.JsonDocument;
import com.deb.cache.LRUPandaCache;
import com.deb.cache.TestUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;

/**
 * @author debmalyajash
 *
 */
@State(Scope.Benchmark)
public class ComputeCacheBenchMark {

	static final int SIZE = (2 << 14);
	static final int MASK = SIZE - 1;
	static final int ITEMS = SIZE / 3;
	static final Integer COMPUTE_KEY = SIZE / 2;
	static final String sameMSISDN = TestUtil.generateRandomMSISDN();

	private static final Callable<JsonDocument> valueLoader = () -> TestUtil.subscriberDocument;
	private static final Function<String, JsonDocument> mappingFunction = any -> TestUtil.subscriberDocument;

	Function<String, JsonDocument> benchmarkFunction;

	String[] msisdns;

	/**
	 * Run the benchmark for following libraries.
	 */
	@Param({ "ConcurrentHashMap", "Caffeine", "Guava", "MapDB", "EhCache" })
	String computeType;

	public ComputeCacheBenchMark() {
		msisdns = new String[SIZE];

		for (int i = 0; i < SIZE; i++) {
			msisdns[i] = TestUtil.generateRandomMSISDN();
		}
	}

	@State(Scope.Thread)
	public static class ThreadState {
		static final Random random = new Random();
		int index = random.nextInt();
	}

	@Setup
	public void setup() {
		if (computeType.equals("ConcurrentHashMap")) {
			setupConcurrentHashMap();
		} else if (computeType.equals("Caffeine")) {
			setupCaffeine();
		} else if (computeType.equals("Guava")) {
			setupGuava();
		} else if (computeType.equals("MapDB")) {
			setupMapDB();
		} else if (computeType.equals("EhCache")) {
			setupEhCacheB();
		} else {
			throw new AssertionError("Unknown computingType: " + computeType);
		}
		Arrays.stream(msisdns).forEach(benchmarkFunction::apply);
	}

	@Benchmark
	@Threads(32)
	public JsonDocument compute_sameKey(ThreadState threadState) {
		return benchmarkFunction.apply(sameMSISDN);
	}


	@Benchmark
	@Threads(32)
	public JsonDocument compute_spread(ThreadState threadState) {
		return benchmarkFunction.apply(msisdns[threadState.index++ & MASK]);
	}

	/**
	 * 
	 */
	private void setupEhCacheB() {
		CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
				.withCache("ehcache", CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class,
						JsonDocument.class, ResourcePoolsBuilder.heap(TestUtil.ONE_MILLION)).build())
				.build(true);
		org.ehcache.Cache<String, JsonDocument> ehcache = cacheManager.getCache("ehcache", String.class,
				JsonDocument.class);
		benchmarkFunction = key -> ehcache.putIfAbsent(key, TestUtil.subscriberDocument);

	}

	/**
	 * 
	 */
	private void setupMapDB() {
		DB db = DBMaker.memoryDB().make();
		HTreeMap<String, JsonDocument> jsonDocumentHTreeMap = (HTreeMap<String, JsonDocument>) db
				.hashMap("MAPDB_MEMORY_HTREE_DOC").expireMaxSize(TestUtil.ONE_MILLION).keySerializer(Serializer.STRING)
				.valueSerializer(Serializer.JAVA).expireAfterCreate().createOrOpen();
		benchmarkFunction = key -> jsonDocumentHTreeMap.computeIfAbsent(key, mappingFunction);

	}

	/**
	 * 
	 */
	private void setupGuava() {
		com.google.common.cache.Cache<String, JsonDocument> cache = CacheBuilder.newBuilder().concurrencyLevel(64)
				.build();
		benchmarkFunction = key -> {
			try {
				return cache.get(key, valueLoader);
			} catch (Exception e) {
				throw Throwables.propagate(e);
			}
		};

	}

	/**
	 * 
	 */
	private void setupCaffeine() {
		Cache<String, JsonDocument> cache = Caffeine.newBuilder().build();
		benchmarkFunction = key -> cache.get(key, mappingFunction);
	}

	/**
	 * 
	 */
	private void setupConcurrentHashMap() {
		LRUPandaCache<String, JsonDocument> lruPandaCache = new LRUPandaCache<>(TestUtil.ONE_MILLION, 0.75f);
		benchmarkFunction = key -> lruPandaCache.computeIfAbsent(key, mappingFunction);
	}
}
