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
 * Interface for cache entries in the various cache implementations
 * 
 * @author christian campo
 */
public interface ICacheEntry {

	/**
	 * Returns the value that is hold in the cache, null if no value was found
	 * 
	 * @return value
	 */
	Object getValue();

	/**
	 * Returns the key of the cache entry
	 * 
	 * @return key
	 */
	Object getKey();

	/**
	 * Returns the timestamp of the cache entry, when it was last put or
	 * touched.
	 * 
	 * @return timestamp
	 */
	long getTimestamp();
}
