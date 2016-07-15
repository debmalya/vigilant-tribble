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

import java.util.concurrent.ConcurrentMap;

import com.couchbase.client.java.document.JsonDocument;
import com.deb.cache.TestUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.jakewharton.fliptables.FlipTable;

/**
 * @author debmalyajash
 *
 */
public class CaffeineStats {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Cache<String, JsonDocument> graphs = Caffeine.newBuilder().maximumSize(10_000).recordStats().build();
		ConcurrentMap<String, JsonDocument> caffeineMap = graphs.asMap();

		String msisdn = TestUtil.generateRandomMSISDN();
		for (int i = 0; i < TestUtil.ONE_MILLION; i++) {
			caffeineMap.put(msisdn, TestUtil.createJsonDocument());
			caffeineMap.remove(msisdn);
			caffeineMap.get(msisdn);
		}
		printCacheStats(graphs);
		
		CacheStats.empty();
		for (int i = 0; i < TestUtil.ONE_MILLION; i++) {
			graphs.put(TestUtil.generateRandomMSISDN(), TestUtil.createJsonDocument());
			caffeineMap.remove(TestUtil.generateRandomMSISDN());
			caffeineMap.get(TestUtil.generateRandomMSISDN());
		}

		printCacheStats(graphs);
	}

	private static void printCacheStats(Cache<String, JsonDocument> graphs) {
		String result = FlipTable
				.of(new String[] { "Name", "Value" },
						new String[][] {
								new String[] { "Average Load Penalty",
										String.valueOf(graphs.stats().averageLoadPenalty()) },
								new String[] { "Eviction Count", String.valueOf(graphs.stats().evictionCount()) },
								new String[] { "Eviction Weight", String.valueOf(graphs.stats().evictionWeight()) },
								new String[] { "Load Count", String.valueOf(graphs.stats().loadCount()) },
								new String[] { "Load Failure Count",
										String.valueOf(graphs.stats().loadFailureCount()) },
								new String[] { "Load Failure Rate", String.valueOf(graphs.stats().loadFailureRate()) },
								new String[] { "Load Success Count",
										String.valueOf(graphs.stats().loadSuccessCount()) },
								new String[] { "Miss Count", String.valueOf(graphs.stats().missCount()) },
								new String[] { "Miss Rate", String.valueOf(graphs.stats().missRate()) },
								new String[] { "Request Count", String.valueOf(graphs.stats().requestCount()) },
								new String[] { "Total Load Time", String.valueOf(graphs.stats().totalLoadTime()) },
								new String[] { "Hit Count", String.valueOf(graphs.stats().hitCount()) },
								new String[] { "Hit Rate", String.valueOf(graphs.stats().hitRate()) },
								});
		System.out.println(result);
	}

}
