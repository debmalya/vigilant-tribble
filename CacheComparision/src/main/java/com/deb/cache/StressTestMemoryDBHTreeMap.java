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

import com.couchbase.client.java.document.JsonDocument;

/**
 * @author debmalyajash
 *
 */
public class StressTestMemoryDBHTreeMap {

	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		Cache cache = new Cache(CacheType.MAPDB_MEMORY_DIRECT_HTREE_DOC, "temp.db", TestUtil.ONE_BILLION);
		TestUtil.billionaire(cache);
		System.out.println("Thanks: time taken :" + (System.currentTimeMillis() - startTime));

	}

	

}
