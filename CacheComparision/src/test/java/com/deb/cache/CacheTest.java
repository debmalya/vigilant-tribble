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

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author debmalyajash
 *
 */
public class CacheTest {
	
	/**
	 * 
	 */
	
	private static Cache myCache;
	
	private static Cache lruPandaCache;
	
	@BeforeClass
	public static void init(){
		myCache = new Cache(CacheType.MAPDB_FILE_TREEMAP_DOC, "temp.db", TestUtil.ONE_MILLION);
		lruPandaCache = new Cache(CacheType.LRULINKED_HASH_MAP, "temp.db", TestUtil.ONE_MILLION);
	}

	/**
	 * Test method for {@link com.deb.cache.Cache#Cache(com.deb.cache.CacheType, java.lang.String, long)}.
	 */
	@Test
	public void testCache() {
		Assert.assertNotNull(myCache);
		Assert.assertNotNull(lruPandaCache);
	}

	/**
	 * Test method for {@link com.deb.cache.Cache#put(java.lang.String, com.couchbase.client.java.document.JsonDocument)}.
	 */
	@Test
	public void testPutStringJsonDocument() {
		myCache.put(TestUtil.generateRandomMSISDN(),TestUtil.createJsonDocument());
		lruPandaCache.put(TestUtil.generateRandomMSISDN(), TestUtil.createJsonDocument());
	}

	/**
	 * Test method for {@link com.deb.cache.Cache#put(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testPutStringString() {
		
	}

}
