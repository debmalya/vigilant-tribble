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

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.couchbase.client.java.document.JsonDocument;

/**
 * @author debmalyajash
 *
 */
public class CacheTest {

	/**
	 * 
	 */

	private static MyCache myCache;

	private static MyCache lruPandaCache;

	private static MyCache ehCache;
	
	private static MyCache guavaCache;
	
	private static MyCache caffeineCache;

	@BeforeClass
	public static void init() {
		myCache = new MyCache(CacheType.MAPDB_FILE_TREEMAP_DOC, "temp.db", TestUtil.ONE_MILLION);
		lruPandaCache = new MyCache(CacheType.LRULINKED_HASH_MAP, "temp.db", TestUtil.ONE_MILLION);
		ehCache = new MyCache(CacheType.EHCACHE, null, TestUtil.ONE_MILLION);
		guavaCache = new MyCache(CacheType.GUAVA_CACHE, "temp.db", TestUtil.ONE_MILLION);
		caffeineCache = new MyCache(CacheType.CAFFEINE,"temp.db",TestUtil.ONE_MILLION);
	}

	/**
	 * Test method for
	 * {@link com.deb.cache.MyCache#Cache(com.deb.cache.CacheType, java.lang.String, long)}
	 * .
	 */
	@Test
	public void testCache() {
		Assert.assertNotNull(myCache);
		Assert.assertNotNull(lruPandaCache);
		Assert.assertNotNull(ehCache);
		Assert.assertNotNull(guavaCache);
		Assert.assertNotNull(caffeineCache);
	}

	/**
	 * Test method for
	 * {@link com.deb.cache.MyCache#put(java.lang.String, com.couchbase.client.java.document.JsonDocument)}
	 * .
	 */
	@Test
	public void testPutStringJsonDocument() {
		myCache.put(TestUtil.generateRandomMSISDN(), TestUtil.createJsonDocument());
		lruPandaCache.put(TestUtil.generateRandomMSISDN(), TestUtil.createJsonDocument());
		ehCache.put(TestUtil.generateRandomMSISDN(), TestUtil.createJsonDocument());
		guavaCache.put(TestUtil.generateRandomMSISDN(), TestUtil.createJsonDocument());
		caffeineCache.put(TestUtil.generateRandomMSISDN(), TestUtil.createJsonDocument());
	}

	/**
	 * Test method for
	 * {@link com.deb.cache.MyCache#put(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testPutStringString() {

	}

	/**
	 * Test method for
	 * {@link com.deb.cache.MyCache#put(java.lang.String, com.couchbase.client.java.document.JsonDocument)}
	 * .
	 */
	@Test
	public void testGetStringJsonDocument() {
		String msisdn = "ABCDEFGH";
		JsonDocument stored = myCache.put(msisdn, TestUtil.createJsonDocument());
		Assert.assertNull(stored);
		Assert.assertEquals(msisdn + " content not matching ", TestUtil.createJsonDocument().content(),
				myCache.get(msisdn).content());
		myCache.remove(msisdn);
		
		stored = ehCache.put(msisdn, TestUtil.createJsonDocument());
		Assert.assertNull(stored);
		Assert.assertEquals(msisdn + " content not matching ", TestUtil.createJsonDocument().content(),
				ehCache.get(msisdn).content());
		ehCache.remove(msisdn);

		stored = lruPandaCache.put(msisdn, TestUtil.createJsonDocument());
		Assert.assertNull(stored);
		Assert.assertEquals(msisdn + " content not matching ", TestUtil.createJsonDocument().content(),
				lruPandaCache.get(msisdn).content());
		lruPandaCache.remove(msisdn);
		
		stored = guavaCache.put(msisdn, TestUtil.createJsonDocument());
		Assert.assertNull(stored);
		Assert.assertEquals(msisdn + " content not matching ", TestUtil.createJsonDocument().content(),
				guavaCache.get(msisdn).content());
		guavaCache.remove(msisdn);
		
		stored = caffeineCache.put(msisdn, TestUtil.createJsonDocument());
		Assert.assertNull(stored);
		Assert.assertEquals(msisdn + " content not matching ", TestUtil.createJsonDocument().content(),
				caffeineCache.get(msisdn).content());
		caffeineCache.remove(msisdn);

		
	}

}
