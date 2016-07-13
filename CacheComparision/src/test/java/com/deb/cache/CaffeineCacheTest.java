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

import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



/**
 * @author debmalyajash
 *
 */
public class CaffeineCacheTest {

	private static CaffeineCache<String,JsonObject> myCaffeineCache;
	
	@BeforeClass
	public static void init(){
		myCaffeineCache = new CaffeineCache<>(TestUtil.ONE_MILLION);
	}
	
	@AfterClass
	public static void cleanUp(){
		myCaffeineCache.cleanUp();
	}
	/**
	 * Test method for {@link com.deb.cache.CaffeineCache#CaffeineCache(int)}.
	 */
	@Test
	public void testCaffeineCache() {
		Assert.assertNotNull(myCaffeineCache);
	}

	/**
	 * Test method for {@link com.deb.cache.CaffeineCache#get(java.lang.Object)}.
	 */
	@Test
	public void testGet() {
		String msisdn = TestUtil.generateRandomMSISDN();
		
		JsonParser parser = new JsonParser();
		JsonObject jsonObj = (JsonObject)parser.parse(TestUtil.SUBSCRIBER_INDICATOR);
		myCaffeineCache.put(msisdn, jsonObj);
		JsonObject retrievedObj = myCaffeineCache.get(msisdn);
		Assert.assertNotNull(retrievedObj);
		
		Assert.assertEquals(jsonObj.toString(), retrievedObj.toString());
	}

	/**
	 * Test method for {@link com.deb.cache.CaffeineCache#put(java.lang.Object, java.lang.Object)}.
	 */
	@Test
	public void testPut() {
		JsonParser parser = new JsonParser();
		JsonObject jsonObj = (JsonObject)parser.parse(TestUtil.SUBSCRIBER_INDICATOR);
		myCaffeineCache.put(TestUtil.generateRandomMSISDN(), jsonObj);
	}

	/**
	 * Test method for {@link com.deb.cache.CaffeineCache#clear()}.
	 */
	@Test
	public void testClear() {
		JsonParser parser = new JsonParser();
		JsonObject jsonObj = (JsonObject)parser.parse(TestUtil.SUBSCRIBER_INDICATOR);
		String myMSISDN = TestUtil.generateRandomMSISDN();
		myCaffeineCache.put(myMSISDN, jsonObj);
		Assert.assertNotNull(myCaffeineCache.get(myMSISDN));
		
		myCaffeineCache.remove(myMSISDN);
		Assert.assertNull(myCaffeineCache.get(myMSISDN));
	}

	/**
	 * Test method for {@link com.deb.cache.CaffeineCache#cleanUp()}.
	 */
	@Test
	public void testCleanUp() {
		myCaffeineCache.cleanUp();
		
	}

}
