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

/**
 * The {@code IAggregator} is responsible for the life cycle of the client side
 * monitoring and its components.
 * <p>
 * The default implementation {@code Aggregator} is very unlikely to be
 * re-implemented by clients.
 */
public interface IAggregator {

	/**
	 * Start the {@code IAggregator}.
	 */
	void start();

	/**
	 * Stop the {@code IAggregator}.
	 */
	void stop();

	/**
	 * Trigger transfer of collectibles for the given category.
	 * 
	 * @param category
	 */
	void triggerTransfer(String category);
}
