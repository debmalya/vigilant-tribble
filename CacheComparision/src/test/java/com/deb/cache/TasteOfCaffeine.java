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

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import com.couchbase.client.java.document.JsonDocument;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.testing.FakeTicker;

/**
 * @author debmalyajash
 *
 */
public class TasteOfCaffeine {

	@Test
	public void test() {
		FakeTicker ticker = new FakeTicker(); // Guava's testlib
		Cache<String, JsonDocument> cache = Caffeine.newBuilder()
		    .expireAfterWrite(10, TimeUnit.MINUTES)
		    .executor(Runnable::run)
		    .ticker(ticker::read)
		    .maximumSize(10)
		    .build();

		String msisdn = TestUtil.generateRandomMSISDN();
		cache.put(msisdn, TestUtil.subscriberDocument);
		ticker.advance(30, TimeUnit.MINUTES);
		Assert.assertNull(cache.getIfPresent(msisdn));
	}

}
