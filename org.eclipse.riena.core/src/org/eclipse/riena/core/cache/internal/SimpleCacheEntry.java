/*******************************************************************************
 * Copyright (c) 2007, 2008 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.cache.internal;

/**
 * A simple cache entry that is store in GenericObjectCache
 * 
 * @author Christian Campo
 */
public class SimpleCacheEntry<K, V> implements ICacheEntry<K, V> {
	private V value;
	private K key;
	private long timestamp;

	/**
	 * @param value
	 * @param key
	 * @param queue
	 */
	public SimpleCacheEntry(V value, K key) {
		super();
		this.value = value;
		this.key = key;
		timestamp = System.currentTimeMillis();
	}

	public V getValue() {
		return value;
	}

	public K getKey() {
		return key;
	}

	public long getTimestamp() {
		return timestamp;
	}
}
