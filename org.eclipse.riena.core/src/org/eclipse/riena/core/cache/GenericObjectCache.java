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
package org.eclipse.riena.core.cache;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedList;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.cache.internal.ICacheEntry;
import org.eclipse.riena.core.cache.internal.SoftCacheEntry;
import org.eclipse.riena.internal.core.Activator;
import org.osgi.service.log.LogService;

/**
 * Class implements a generic object cache.
 * 
 * 
 */
public class GenericObjectCache<K, V> implements IGenericObjectCache<K, V> {

	private static final String SPIRIT_CORE_BASE_INTERNAL_REFERENCE_QUEUE = "spirit.core.base.internal.ReferenceQueue"; //$NON-NLS-1$
	private static final String SPIRIT_CORE_BASE_INTERNAL_HARD_LINKS = "spirit.core.base.internal.HardLinks"; //$NON-NLS-1$
	private final static Logger LOGGER = Activator.getDefault().getLogger(GenericObjectCache.class);
	private HashMap<K, ICacheEntry<K, V>> cacheEntries;
	/** timeout in milliseconds * */
	private long timeout;
	/** minimum count of entries to keep * */
	private int minimumSize;
	private LinkedList<V> hardLinks;
	/** Reference queue for cleared SoftReference objects. */
	private ReferenceQueue<V> queue;
	private int statHit;
	private int statNotFound;
	private int statMiss;
	private int statTimeout;
	private static int statDisplayCount;
	private String name = "Cache : "; //$NON-NLS-1$

	/**
	 * Constructor.
	 */
	public GenericObjectCache() {
		super();
		LOGGER.log(LogService.LOG_DEBUG, "creating new GenericObjectCache instance"); //$NON-NLS-1$
		cacheEntries = new HashMap<K, ICacheEntry<K, V>>();
		queue = new ReferenceQueue<V>();
		hardLinks = new LinkedList<V>();
		// default timeout 1 minute
		setTimeout(60000);
	}

	/**
	 * @see org.eclipse.riena.core.cache.IGenericObjectCache#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name + " : "; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.core.cache.IGenericObjectCache#get(java.lang.Object)
	 */
	public V get(K key) {
		LOGGER.log(LogService.LOG_DEBUG, "get = " + key); //$NON-NLS-1$
		long timestamp = 0;
		V value = null;
		synchronized (cacheEntries) {
			ICacheEntry<K, V> entry = cacheEntries.get(key);
			/** do we find the entry * */
			if (entry == null) {
				statNotFound++;
				printStat();
				return null;
			}
			timestamp = entry.getTimestamp();

			long timePassed = System.currentTimeMillis() - timestamp;
			/** is the entry expired * */
			if (timePassed > timeout) {
				remove(key);
				statTimeout++;
				printStat();
				return null;
			}
			value = entry.getValue();
		}

		/** does the soft reference still point anywhere * */
		if (value != null) {
			/** if a minimum size is required * */
			touchValue(value);
			statHit++;
			printStat();
			return value;
		} else {
			/** if not remove the key * */
			remove(key);
			statMiss++;
			printStat();
			return null;
		}
	}

	private void printStat() {
		statDisplayCount++;
		if (statDisplayCount > 100) {
			LOGGER.log(LogService.LOG_INFO, name + "Hit / NotFound / Miss / Timeout " + statHit + " / " + statNotFound //$NON-NLS-1$ //$NON-NLS-2$
					+ " / " + statMiss + " / " + statTimeout); //$NON-NLS-1$ //$NON-NLS-2$
			statDisplayCount = 0;
		}
	}

	public String getStatistic() {
		return name + "Hit / NotFound / Miss / Timeout " + statHit + " / " + statNotFound + " / " + statMiss + " / " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				+ statTimeout;
	}

	private void touchValue(V value) {
		if (minimumSize > 0) {
			synchronized (hardLinks) {
				hardLinks.addFirst(value);
				if (hardLinks.size() > minimumSize) {
					// Remove the last entry if list longer than minimumSize
					hardLinks.removeLast();
				}
			}
		}
	}

	/**
	 * @see org.eclipse.riena.core.cache.IGenericObjectCache#put(Object,
	 *      java.lang.Object)
	 */
	public void put(K key, V value) {
		LOGGER.log(LogService.LOG_DEBUG, "put = " + key + " , " + value + ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		processQueue();
		ICacheEntry<K, V> entry;
		entry = new SoftCacheEntry<K, V>(value, key, queue);
		synchronized (cacheEntries) {
			cacheEntries.put(key, entry);
		}
		touchValue(value);
	}

	/**
	 * @see org.eclipse.riena.core.cache.IGenericObjectCache#clear()
	 */
	public void clear() {
		LOGGER.log(LogService.LOG_DEBUG, "clear"); //$NON-NLS-1$
		synchronized (hardLinks) {
			hardLinks.clear();
		}
		processQueue();
		synchronized (cacheEntries) {
			cacheEntries.clear();
		}
	}

	/**
	 * @see org.eclipse.riena.core.cache.IGenericObjectCache#remove(Object)
	 */
	public void remove(K key) {
		LOGGER.log(LogService.LOG_DEBUG, "remove = " + key); //$NON-NLS-1$
		processQueue();
		synchronized (cacheEntries) {
			cacheEntries.remove(key);
		}
	}

	/**
	 * @see org.eclipse.riena.core.cache.IGenericObjectCache#size()
	 */
	public int size() {
		processQueue();
		synchronized (cacheEntries) {
			LOGGER.log(LogService.LOG_DEBUG, "size returned = " + cacheEntries.size()); //$NON-NLS-1$
			return cacheEntries.size();
		}
	}

	/**
	 * @see org.eclipse.riena.core.cache.IGenericObjectCache#setTimeout(int)
	 */
	public void setTimeout(int milliseconds) {
		LOGGER.log(LogService.LOG_DEBUG, "setTimeout = " + milliseconds); //$NON-NLS-1$
		timeout = milliseconds;
	}

	/**
	 * @see org.eclipse.riena.core.cache.IGenericObjectCache#getTimeout()
	 */
	public int getTimeout() {
		return (int) timeout;
	}

	/**
	 * @see org.eclipse.riena.core.cache.IGenericObjectCache#getMinimumSize()
	 */
	public int getMinimumSize() {
		return minimumSize;
	}

	/**
	 * @see org.eclipse.riena.core.cache.IGenericObjectCache#getSize()
	 */
	public int getSize() {
		return size();
	}

	/**
	 * @see org.eclipse.riena.core.cache.IGenericObjectCache#setMinimumSize(int)
	 */
	public void setMinimumSize(int minSize) {
		LOGGER.log(LogService.LOG_DEBUG, "setMinSize = " + minSize); //$NON-NLS-1$
		minimumSize = minSize;
	}

	private void processQueue() {
		LOGGER.log(LogService.LOG_DEBUG, "processQueue"); //$NON-NLS-1$
		SoftReference<V> ref;
		ICacheEntry<K, V> tempEntry;
		K key = null;
		int count = 0;
		synchronized (cacheEntries) {
			while ((ref = (SoftReference<V>) queue.poll()) != null) {
				tempEntry = (ICacheEntry<K, V>) ref.get();
				if (tempEntry instanceof ICacheEntry) {
					key = tempEntry.getKey();
				}
				cacheEntries.remove(key);
				count++;
			}
		}
		if (count > 0) {
			LOGGER.log(LogService.LOG_INFO, "processQueue removed " + count + " entries"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
}
