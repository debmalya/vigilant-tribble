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

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import com.couchbase.client.java.document.JsonDocument;

/**
 * @author debmalyajash
 *
 */
public enum SimpleCacheType {
	MapDBDirectHTree {
		private DB db = DBMaker.memoryDirectDB().make();

		private HTreeMap<String, JsonDocument> map = (HTreeMap<String, JsonDocument>) db
				.hashMap("MAPDB_MEMORY_HTREE_DOC").expireMaxSize(1_000_000).keySerializer(Serializer.STRING)
				.valueSerializer(Serializer.JAVA).expireAfterCreate(10, TimeUnit.MINUTES).createOrOpen();

		public JsonDocument put(String key, JsonDocument value) {
			return map.put(key, value);
		}

		public JsonDocument get(String key) {
			return map.get(key);
		}

		public JsonDocument remove(String key) {
			return map.remove(key);
		}

	},
	
//	Caffeine {
//		@Override
//		public <K, V> SimpleCache<K, V> create(int maximumSize) {
//			return new CaffeineCache<>(maximumSize);
//		}
//	},

}
