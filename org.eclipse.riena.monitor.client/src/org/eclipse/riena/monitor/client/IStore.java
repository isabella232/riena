/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.monitor.client;

import java.util.List;
import java.util.Map;

import org.eclipse.riena.monitor.common.Collectible;

/**
 * The {@code IStore} defines a store for collectibles.
 */
public interface IStore {

	/**
	 * Set the categories this store must handle.
	 * 
	 * @param categories
	 */
	void setCategories(Map<String, Category> categories);

	/**
	 * Open the store.
	 */
	void open();

	/**
	 * Close the store.
	 */
	void close();

	/**
	 * In case that the store implementation buffers entries calling this method
	 * assures persisting them.
	 */
	void flush();

	/**
	 * Make the given collectible persistent.
	 * 
	 * @param collectible
	 * @return true on success
	 */
	boolean collect(final Collectible<?> collectible);

	/**
	 * Prepare (mark) collectibles for the given category as ready to transfer.
	 * 
	 * @param category
	 */
	void prepareTransferables(String category);

	/**
	 * Retrieve all collectibles for the given category
	 * 
	 * @param category
	 *            the category
	 * @return a list of ready to transfer collectibles
	 */
	List<Collectible<?>> retrieveTransferables(String category);

	/**
	 * Commit the given collectibles, i.e. remove them from the persistent
	 * store. These collectibles have been previously retrieved by {@code
	 * getTransferables}. This method should only be called in case of a
	 * successful transfer.
	 * 
	 * @param collectibles
	 */
	void commitTransferred(List<Collectible<?>> collectibles);

}