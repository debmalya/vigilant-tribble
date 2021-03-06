/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.deb;

import java.util.concurrent.Callable;
import java.util.function.Function;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;

import com.couchbase.client.java.document.JsonDocument;
import com.deb.cache.CacheType;
import com.deb.cache.MyCache;
import com.deb.cache.TestUtil;

@State(Scope.Benchmark)
public class MyBenchmark {

	private static final JsonDocument myDocument = TestUtil.createJsonDocument();
	
	private static final Callable<JsonDocument> valueLoader = () -> myDocument;
	private static final Function<String, JsonDocument> mappingFunction = any -> myDocument;

	@State(Scope.Benchmark)
	public static class MapDBFileHolder {
		public static final MyCache mapDBCache;
		static {
			mapDBCache = new MyCache(CacheType.MAPDB_FILE_TREEMAP_DOC, "temp.db", 1000000);
		}
	}
	
	@State(Scope.Benchmark)
	public static class CaffenineHolder {
		public static final MyCache caffenineCache;
		static {
			caffenineCache = new MyCache(CacheType.CAFFEINE, "temp.db", 1000000);
		}
	}
	
	@State(Scope.Benchmark)
	public static class MapDBInDirectMemoryHolder {
		public static final MyCache mapDBDirectMemory;
		static {
			mapDBDirectMemory = new MyCache(CacheType.MAPDB_MEMORY_DIRECT_HTREE_DOC, "temp.db", 1000000);
		}
	}

	@State(Scope.Benchmark)
	public static class EhCacheHolder {
		public static final MyCache ehCache;
		static {
			ehCache = new MyCache(CacheType.EHCACHE, "temp.db", 1000000);
		}
	}
	
	
	@State(Scope.Benchmark)
	public static class LRUMapHolder {
		public static final MyCache lruCache;
		static {
			lruCache = new MyCache(CacheType.LRULINKED_HASH_MAP, "temp.db", 1000000);
		}
	}
	
	@State(Scope.Benchmark)
	public static class GuavaCacheHolder {
		public static final MyCache guavaCache;
		static {
			guavaCache = new MyCache(CacheType.GUAVA_CACHE, "temp.db", 1000000);
		}
	}
	

	/**
	 * MyBenchmark.mapDBFile thrpt 200 779.527 ± 18.967 ops/s
	 */
//	@Benchmark
	public void mapDBFilePut() {
		MapDBFileHolder.mapDBCache.put(TestUtil.generateRandomMSISDN(), myDocument);
	}

//	@Benchmark
	public void mapDBFileGet() {
		MapDBFileHolder.mapDBCache.get(TestUtil.generateRandomMSISDN());
	}

//	@Benchmark
	public void mapDBFileRemove() {
		MapDBFileHolder.mapDBCache.remove(TestUtil.generateRandomMSISDN());
	}

//	@Benchmark
	public void ehcachePut() {
		EhCacheHolder.ehCache.put(TestUtil.generateRandomMSISDN(), myDocument);
	}

//	@Benchmark @Threads(16)
	public void ehcacheGet() {
		EhCacheHolder.ehCache.get(TestUtil.generateRandomMSISDN());
	}

//	@Benchmark
	public void ehcacheRemove() {
		EhCacheHolder.ehCache.remove(TestUtil.generateRandomMSISDN());
	}
	
	@Benchmark @Threads(16)
	public void caffeinePut() {
		CaffenineHolder.caffenineCache.put(TestUtil.generateRandomMSISDN(), myDocument);
	}

	@Benchmark @Threads(16)
	public void caffeineGet() {
		CaffenineHolder.caffenineCache.get(TestUtil.generateRandomMSISDN());
	}

	@Benchmark @Threads(16)
	public void caffeineRemove() {
		CaffenineHolder.caffenineCache.remove(TestUtil.generateRandomMSISDN());
	}
	
//	@Benchmark
	public void mapDBDirectMemoryPut() {
		MapDBInDirectMemoryHolder.mapDBDirectMemory.put(TestUtil.generateRandomMSISDN(), myDocument);
	}

//	@Benchmark
	public void mapDBDirectMemoryGet() {
		MapDBInDirectMemoryHolder.mapDBDirectMemory.get(TestUtil.generateRandomMSISDN());
	}

//	@Benchmark
	public void mapDBDirectMemoryRemove() {
		MapDBInDirectMemoryHolder.mapDBDirectMemory.remove(TestUtil.generateRandomMSISDN());
	}
	
	@Benchmark @Threads(16)
	public void lruPut() {
		LRUMapHolder.lruCache.put(TestUtil.generateRandomMSISDN(), myDocument);
	}

	@Benchmark @Threads(16)
	public void lruGet() {
		LRUMapHolder.lruCache.get(TestUtil.generateRandomMSISDN());
	}

	@Benchmark @Threads(16)
	public void lruRemove() {
		LRUMapHolder.lruCache.remove(TestUtil.generateRandomMSISDN());
	}
	
//	@Benchmark
	public void guavaPut() {
		GuavaCacheHolder.guavaCache.put(TestUtil.generateRandomMSISDN(), myDocument);
	}

//	@Benchmark
	public void guavaGet() {
		GuavaCacheHolder.guavaCache.get(TestUtil.generateRandomMSISDN());
	}

//	@Benchmark
	public void guavaRemove() {
		GuavaCacheHolder.guavaCache.remove(TestUtil.generateRandomMSISDN());
	}

//	@Benchmark
	public void mapDBFile() {
		MapDBFileHolder.mapDBCache.put(TestUtil.generateRandomMSISDN(), myDocument);
		MapDBFileHolder.mapDBCache.get(TestUtil.generateRandomMSISDN());
		MapDBFileHolder.mapDBCache.remove(TestUtil.generateRandomMSISDN());
	}

//	@Benchmark
	public void ehcache() {
		EhCacheHolder.ehCache.put(TestUtil.generateRandomMSISDN(), myDocument);
		EhCacheHolder.ehCache.get(TestUtil.generateRandomMSISDN());
		EhCacheHolder.ehCache.remove(TestUtil.generateRandomMSISDN());
	}

}
