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
package org.eclipse.riena.monitor.client;

import java.util.Map;

/**
 * The {@code ISender} is responsible for retrieving collectibles from the
 * store, transferring them to the receiver.
 */
public interface ISender {

	/**
	 * Set the store for the sender.
	 * 
	 * @param store
	 */
	void setStore(IStore store);

	/**
	 * Set the categories this sender must handle.
	 * 
	 * @param category
	 */
	void setCategories(Map<String, Category> categories);

	/**
	 * Start the sender, i.e. from now on the sender is responsible for sending
	 * collectibles and maintain the sore appropriate.
	 * <p>
	 * Sender is also responsible for retrying failed send attempts.
	 */
	void start();

	/**
	 * Stop the sender.
	 */
	void stop();

	/**
	 * Trigger the transfer of the collectibles registered with the given
	 * category.
	 * 
	 * @param category
	 */
	void triggerTransfer(String category);

}
