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
package org.eclipse.riena.core.cache;

import java.util.HashMap;

/**
 * The GenericObjectCache is a multipurpose object for storing data without
 * locking all your memory. It works like a regular HashMap with some
 * differences. Item can be removed from the GenericObjectCache under certain
 * conditions:
 * <ul>
 * <li>timeout: xxxx milliseconds after the timeout, an item is no longer valid
 * if found in the cache and automatically removed.</li>
 * <li>GC: if garbage collection is started because of low memory in the JVM,
 * it can remove any entry in the GenericObjectCache.</li>
 * <li>minSize: minSize specifies a number of minimum entries that are held in
 * the cache even if garbage collection occurs. However these minSize entries
 * will still be checked against the timeout.</li>
 * </ul>
 * 
 * @author Christian Campo
 */
public interface IGenericObjectCache {

	/**
	 * returns true if this is a global cache
	 * 
	 * @return true if cache is global
	 */
	boolean isGlobalCache();

	/**
	 * Get an object by key. Cannot be called for global caches. There you have
	 * to use get(Object,Class), see below.
	 * 
	 * @param key
	 *            the key to look up object.
	 * @return the object looked up.
	 * @pre (isGlobalCache()==false)
	 */
	Object get(Object key);

	/**
	 * Get an object by key in the context of the calling class
	 * 
	 * @param key
	 * @param callingClass
	 * @return
	 */
	Object get(Object key, Class callingClass);

	/**
	 * Put some object <code>value</code> with <code>key</code> into the
	 * cache.
	 * 
	 * @param key
	 *            the key for looking up the object.
	 * @param value
	 *            the value. If this is a globalCache the value must implement
	 *            Serializable
	 * @pre !isGlobalCache() || value instanceof Serializable
	 */
	void put(Object key, Object value);

	/**
	 * Remove object with <code>key</code> from the cache.
	 * 
	 * @param key
	 *            the key.
	 */
	void remove(Object key);

	/**
	 * Clear the cache.
	 */
	void clear();

	/**
	 * Set the timeout for cached objects.
	 * 
	 * @param milliseconds
	 *            the timeout in milliseconds.
	 */
	void setTimeout(int milliseconds);

	/**
	 * Return mumber of cached objects.
	 * 
	 * @return the mumber of cached objects.
	 */
	int size();

	/**
	 * Set the minimum number of entries that are held in the cache.
	 * 
	 * @param minSize
	 *            the minimum number of entries.
	 */
	void setMinimumSize(int minSize);

	/**
	 * Returns the currently set timeout value
	 * 
	 * @return
	 */
	int getTimeout();

	/**
	 * Returns the currently set minimum size
	 * 
	 * @return
	 */
	int getMinimumSize();

	/**
	 * Sets a global HashMap resource that is used for this GenericObjectCache.
	 * A type is required for Caches with global HashMap
	 * 
	 * @param map
	 */
	void setHashMap(HashMap<Object, Object> map);

	/**
	 * Descriptive name for this cache (shown in logs)
	 * 
	 * @param name
	 */
	void setName(String name);

	/**
	 * Returns the current size of the GenericObjectCache
	 * 
	 * @return
	 */
	int getSize();

	/**
	 * @return
	 */
	String getStatistic();
}