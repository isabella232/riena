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

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedList;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.cache.internal.GlobalSoftCacheEntry;
import org.eclipse.riena.core.cache.internal.ICacheEntry;
import org.eclipse.riena.core.cache.internal.SoftCacheEntry;
import org.eclipse.riena.internal.core.Activator;
import org.osgi.service.log.LogService;

/**
 * Class implements a generic object cache.
 * 
 * @author Christian Campo
 */
public class GenericObjectCache implements IGenericObjectCache {

	private static final String SPIRIT_CORE_BASE_INTERNAL_REFERENCE_QUEUE = "spirit.core.base.internal.ReferenceQueue";
	private static final String SPIRIT_CORE_BASE_INTERNAL_HARD_LINKS = "spirit.core.base.internal.HardLinks";
	private final static Logger LOGGER = Activator.getDefault().getLogger(GenericObjectCache.class.getName());
	private HashMap cacheEntries;
	/** timeout in milliseconds * */
	private long timeout;
	/** minimum count of entries to keep * */
	private int minimumSize;
	private LinkedList hardLinks;
	/** Reference queue for cleared SoftReference objects. */
	private ReferenceQueue queue;
	private int statHit;
	private int statNotFound;
	private int statMiss;
	private int statTimeout;
	private static int statDisplayCount;
	private boolean globalCache; // default = false
	private String name = "Cache : ";

	/**
	 * Constructor.
	 */
	public GenericObjectCache() {
		super();
		LOGGER.log(LogService.LOG_DEBUG, "creating new GenericObjectCache instance");
		cacheEntries = new HashMap();
		queue = new ReferenceQueue();
		hardLinks = new LinkedList();
	}

	/**
	 * @see org.eclipse.riena.core.cache.IGenericObjectCache#setHashMap(java.util.HashMap)
	 */
	public void setHashMap(HashMap map) {
		cacheEntries = map;
		globalCache = true;
		synchronized (map) {
			// if we get a global non empty map, get other global instances that
			// are stored their
			if (map.size() > 0) {
				hardLinks = (LinkedList) map.get(SPIRIT_CORE_BASE_INTERNAL_HARD_LINKS);
				queue = (ReferenceQueue) map.get(SPIRIT_CORE_BASE_INTERNAL_REFERENCE_QUEUE);
			} else {
				// else store your local vars in there so the next instance can
				// find them
				map.put(SPIRIT_CORE_BASE_INTERNAL_HARD_LINKS, hardLinks);
				map.put(SPIRIT_CORE_BASE_INTERNAL_REFERENCE_QUEUE, queue);
			}
		}
	}

	/**
	 * @see org.eclipse.riena.core.cache.IGenericObjectCache#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name + " : ";
	}

	/**
	 * @see org.eclipse.riena.core.cache.IGenericObjectCache#isGlobalCache()
	 */
	public boolean isGlobalCache() {
		return globalCache;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.core.cache.IGenericObjectCache#get(java.lang.Object)
	 */
	public Object get(Object key) {
		// Assert.isTrue(!isGlobalCache(),"get with single parameter cannot be
		// used for
		// global caches. use two parameter
		// version ");
		return get(key, GenericObjectCache.class);
	}

	/**
	 * @see org.eclipse.riena.core.cache.IGenericObjectCache#get(Object,Class)
	 */
	public Object get(Object key, Class callingClass) {
		LOGGER.log(LogService.LOG_DEBUG, "get = " + key);
		long timestamp = 0;
		Object value = null;
		synchronized (cacheEntries) {
			Object tempEntry = cacheEntries.get(key);
			/** do we find the entry * */
			if (tempEntry == null) {
				statNotFound++;
				printStat();
				return null;
			}
			// now proxy the entry if we have a global cache because the
			// instance could be from a different classloader
			// if tempEntry is from my webclasspath cast it directly otherwise
			// create a proxy
			if (!(tempEntry instanceof ICacheEntry)) {
				try {
					// timestamp = ((Long)
					// ReflectionUtils.invokeHidden(tempEntry, "getTimestamp",
					// new
					// Object[0])).longValue();
				} catch (Throwable e) {
					// this should not happen
				}
			} else {
				ICacheEntry entry = (ICacheEntry) tempEntry;
				timestamp = entry.getTimestamp();
			}

			long timePassed = System.currentTimeMillis() - timestamp;
			/** is the entry expired * */
			if (timePassed > timeout) {
				remove(key);
				statTimeout++;
				printStat();
				return null;
			}
			// only get the value if its not timed out (see previous IF)
			if (!(tempEntry instanceof ICacheEntry)) {
				try {
					// value = ReflectionUtils.invokeHidden(tempEntry,
					// "getValue", new Object[] {
					// callingClass.getClassLoader() });
				} catch (Throwable e) {
					// this should not happen
				}
			} else {
				if (globalCache) {
					GlobalSoftCacheEntry entry = (GlobalSoftCacheEntry) tempEntry;
					value = entry.getValue(callingClass.getClassLoader());
				} else {
					ICacheEntry entry = (ICacheEntry) tempEntry;
					value = entry.getValue();
				}
			}
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
			LOGGER.log(LogService.LOG_INFO, name + "Hit / NotFound / Miss / Timeout " + statHit + " / " + statNotFound
					+ " / " + statMiss + " / " + statTimeout);
			statDisplayCount = 0;
		}
	}

	public String getStatistic() {
		return name + "Hit / NotFound / Miss / Timeout " + statHit + " / " + statNotFound + " / " + statMiss + " / "
				+ statTimeout;
	}

	private void touchValue(Object value) {
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
	public void put(Object key, Object value) {
		// Assert.isTrue((!globalCache) || value instanceof
		// Serializable,"objects in global caches must be
		// serializable" );
		LOGGER.log(LogService.LOG_DEBUG, "put = " + key + " , " + value + "");
		processQueue();
		ICacheEntry entry;
		if (globalCache) {
			entry = new GlobalSoftCacheEntry(value, key, queue);
		} else {
			entry = new SoftCacheEntry(value, key, queue);
		}
		synchronized (cacheEntries) {
			cacheEntries.put(key, entry);
		}
		touchValue(value);
	}

	/**
	 * @see org.eclipse.riena.core.cache.IGenericObjectCache#clear()
	 */
	public void clear() {
		LOGGER.log(LogService.LOG_DEBUG, "clear");
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
	public void remove(Object key) {
		LOGGER.log(LogService.LOG_DEBUG, "remove = " + key);
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
			LOGGER.log(LogService.LOG_DEBUG, "size returned = " + cacheEntries.size());
			return cacheEntries.size();
		}
	}

	/**
	 * @see org.eclipse.riena.core.cache.IGenericObjectCache#setTimeout(int)
	 */
	public void setTimeout(int milliseconds) {
		LOGGER.log(LogService.LOG_DEBUG, "setTimeout = " + milliseconds);
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
		LOGGER.log(LogService.LOG_DEBUG, "setMinSize = " + minSize);
		minimumSize = minSize;
	}

	private void processQueue() {
		LOGGER.log(LogService.LOG_DEBUG, "processQueue");
		SoftReference ref;
		Object tempEntry;
		Object key = null;
		int count = 0;
		synchronized (cacheEntries) {
			while ((ref = (SoftReference) queue.poll()) != null) {
				tempEntry = ref.get();
				if (tempEntry instanceof ICacheEntry) {
					key = ((ICacheEntry) tempEntry).getKey();
				} else {
					try {
						// key = ReflectionUtils.invokeHidden(tempEntry,
						// "getKey", new Object[0]);
					} catch (Throwable e) {
						// should not happen
					}
				}
				cacheEntries.remove(key);
				count++;
			}
		}
		if (count > 0) {
			LOGGER.log(LogService.LOG_INFO, "processQueue removed " + count + " entries");
		}
	}

}