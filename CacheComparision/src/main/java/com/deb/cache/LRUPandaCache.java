/**
 * Copyright 2015-2016 Knowesis Pte Ltd.
 * 
 */
package com.deb.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author debmalyajash
 *
 */
public class LRUPandaCache<K,T> extends LinkedHashMap<K,T>  {

	private static final long serialVersionUID = 1L;
	private int capacity;

	public LRUPandaCache( int capacity, float d ){
		super( capacity, d, true );
		this.capacity = capacity;
	}

	/**
	 * removeEldestEntry() should be overridden by the user, otherwise it will
	 * not remove the oldest object from the Map.
	 */
	@Override
	protected boolean removeEldestEntry( Map.Entry< K, T > eldest ) {
		return size() > this.capacity;
	}

}
