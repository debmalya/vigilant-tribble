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

import java.io.PrintStream;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.github.jamm.MemoryMeter;
import org.github.jamm.MemoryMeter.Guess;

import com.deb.cache.TestUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.cache.CacheBuilder;
import com.google.common.math.LongMath;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jakewharton.fliptables.FlipTable;

/**
 * @author debmalyajash
 *
 */
public class MemoryBenchMark {

	// The number of entries added to minimize skew due to non-entry factors
	static final int FUZZY_SIZE = 25_000;
	// The maximum size, which is larger than the fuzzy factor due to Guava's
	// early eviction
	static final int MAXIMUM_SIZE = 2 * FUZZY_SIZE;

	private static final JsonObject ourObject = (JsonObject) new JsonParser().parse(TestUtil.SUBSCRIBER_INDICATOR);

	final MemoryMeter meter = new MemoryMeter().withGuessing(Guess.FALLBACK_BEST).ignoreKnownSingletons().enableDebug();
	final PrintStream out = System.out;

	// The pre-computed entries to store into the cache when computing the
	// per-entry overhead
//	Stream<String> strStream = Arrays.as;
//	static final Map<String, JsonObject> workingSet = IntStream.range(0, FUZZY_SIZE).boxed()
//			.collect(Collectors.toMap(identity(), i -> ourObject));
	static final Map<String, JsonObject> workingSet = createWorkingSet();

	public static void main(String[] args) throws Exception {
		new MemoryBenchMark().run();
	}

	public void run() throws Exception {
		if (!MemoryMeter.hasInstrumentation()) {
			out.println("WARNING: Java agent not installed - guessing instead");
		}
		out.println();
		unbounded();
		// maximumSize();
		// maximumSize_expireAfterAccess();
		// maximumSize_expireAfterWrite();
		// maximumSize_refreshAfterWrite();
		// maximumWeight();
		// expireAfterAccess();
		// expireAfterWrite();
		// expireAfterAccess_expireAfterWrite();
		// weakKeys();
		// weakValues();
		// weakKeys_weakValues();
		// weakKeys_softValues();
		// softValues();
	}

	private Caffeine<Object, Object> builder() {
		// Avoid counting ForkJoinPool in estimates
		return Caffeine.newBuilder().executor(Runnable::run);
	}

	private String[] evaluate(String label, Map<String, JsonObject> map) {
		long base = meter.measureDeep(map);
		Map<String,JsonObject> workingSet = new HashMap<String,JsonObject>();
		map.putAll(workingSet );

		long populated = meter.measureDeep(map);
		long entryOverhead = 2 * FUZZY_SIZE * meter.measureDeep(workingSet.keySet().iterator().next());
		long perEntry = LongMath.divide(populated - entryOverhead - base, FUZZY_SIZE, RoundingMode.HALF_EVEN);
		perEntry += ((perEntry & 1) == 0) ? 0 : 1;
		long aligned = ((perEntry % 8) == 0) ? perEntry : ((1 + perEntry / 8) * 8);
		return new String[] { label, String.format("%,d bytes", base),
				String.format("%,d bytes (%,d aligned)", perEntry, aligned) };
	}

	private void unbounded() {
		Cache<String, JsonObject> caffeine = builder().build();
		com.google.common.cache.Cache<String, JsonObject> guava = CacheBuilder.newBuilder().build();
		compare("Unbounded", caffeine, guava);
	}

	private void compare(String label, Cache<String, JsonObject> caffeine,
			com.google.common.cache.Cache<String, JsonObject> guava) {
		caffeine.cleanUp();
		guava.cleanUp();

		int leftPadded = Math.max((36 - label.length()) / 2 - 1, 1);
		out.printf(" %2$-" + leftPadded + "s %s%n", label, " ");
		String result = FlipTable.of(new String[] { "Cache", "Baseline", "Per Entry" },
				new String[][] { evaluate("Caffeine", caffeine.asMap()), evaluate("Guava", guava.asMap()) });
		out.println(result);
	}
	
	private static Map<String, JsonObject> createWorkingSet() {
		List<String> msisdnList = new ArrayList<>();
		for (int i = 0; i < FUZZY_SIZE; i++) {
			msisdnList.add( ""+i);
		}
		Map<String, JsonObject> workingSet = msisdnList.stream().collect(Collectors.toMap(Function.identity(), i-> ourObject));
		return workingSet;
	}
	
	

	
}
