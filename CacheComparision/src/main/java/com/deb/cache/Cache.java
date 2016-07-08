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

import java.io.File;

import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import com.couchbase.client.java.document.JsonDocument;

/**
 * @author debmalyajash
 *
 */
public class Cache {

	private static DB db = null;

	private static BTreeMap<String, JsonDocument> jsonDocumentBTreeMap;
	private static BTreeMap<String, String> jsonStringTBreeMap;
	private static HTreeMap<String, String> jsonHTreeMap;

	private static CacheType cacheType;

	@SuppressWarnings("unchecked")
	public Cache(CacheType passedCacheType, String fileName, long starterSize) {
		cacheType = passedCacheType;
		
		switch (passedCacheType) {
		case MAPDB_FILE_TREEMAP_DOC:
			File file = new File(fileName);
			file.delete();
			db = DBMaker.fileDB(fileName) 
					.fileMmapEnable().fileMmapEnableIfSupported().deleteFilesAfterClose().make();
			jsonDocumentBTreeMap = (BTreeMap<String, JsonDocument>) db.treeMap("MAPDB_FILE_TREEMAP_DOC")
					.keySerializer(Serializer.STRING).valueSerializer(Serializer.JAVA).createOrOpen();			
			break;
		case MAPDB_MEMORY_TREEMAP_STR:
			db = DBMaker.memoryDB().allocateStartSize(starterSize).make();
			break;
		default:
			break;
		}
	}
	
	public JsonDocument put(String key,JsonDocument value) {
		JsonDocument document = null;
		switch(cacheType) {
		case MAPDB_FILE_TREEMAP_DOC:
			document = jsonDocumentBTreeMap.put(key, value);
			break;
		default:
			typeNotSupported();
			break;
		}
		return document;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public JsonDocument get(String key,JsonDocument value) {
		JsonDocument document = null;
		switch(cacheType) {
		case MAPDB_FILE_TREEMAP_DOC:
			document = jsonDocumentBTreeMap.get(key);
			break;
		default:
			typeNotSupported();
			break;
		}
		return document;
	}

	/**
	 * 
	 */
	private void typeNotSupported() {
		System.err.println(cacheType + " not Supported");
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String put(String key,String value) {
		String document = null;
		switch(cacheType) {
		case MAPDB_MEMORY_TREEMAP_STR:
			document = jsonStringTBreeMap.put(key, value);
			break;
		default:
			typeNotSupported();
			break;
		}
		return document;
	}
}
