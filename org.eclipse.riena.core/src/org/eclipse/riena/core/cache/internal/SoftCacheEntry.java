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

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

/**
 * CacheEntry with SoftReference (=values can be garbage collected)
 * 
 * @author Christian Campo
 */
public class SoftCacheEntry implements ICacheEntry {
	private Object key;
	private long timestamp;
	private SoftReference<? extends Object> value;

	/**
	 * Create a new Cache Entry without value (for subclasses which have their
	 * own implementation for that)
	 * 
	 * @param key
	 */
	public SoftCacheEntry(Object key) {
		super();
		this.key = key;
		timestamp = System.currentTimeMillis();
	}

	/**
	 * Creates a new Cache Entry
	 * 
	 * @param value
	 * @param key
	 * @param queue
	 */
	public <T> SoftCacheEntry(T value, Object key, ReferenceQueue<T> queue) {
		this(key);
		if (value != null) {
			this.value = new SoftReference<T>(value, queue);
		}
	}

	/**
	 * @see org.eclipse.riena.core.cache.internal.ICacheEntry#getValue()
	 */
	public Object getValue() {
		return value.get();
	}

	/**
	 * @see org.eclipse.riena.core.cache.internal.ICacheEntry#getKey()
	 */
	public Object getKey() {
		return key;
	}

	/**
	 * @see org.eclipse.riena.core.cache.internal.ICacheEntry#getTimestamp()
	 */
	public long getTimestamp() {
		return timestamp;
	}
}