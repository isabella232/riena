/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
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
public class SimpleCacheEntry implements ICacheEntry {
	private Object value;
	private Object key;
	private long timestamp;

	/**
	 * @param value
	 * @param key
	 * @param queue
	 */
	public SimpleCacheEntry(Object value, Object key) {
		super();
		this.value = value;
		this.key = key;
		timestamp = System.currentTimeMillis();
	}

	public Object getValue() {
		return value;
	}

	public Object getKey() {
		return key;
	}

	public long getTimestamp() {
		return timestamp;
	}
}