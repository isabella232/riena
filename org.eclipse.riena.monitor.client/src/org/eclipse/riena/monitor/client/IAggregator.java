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

import org.eclipse.riena.monitor.common.Collectible;

/**
 * The {@code IAggregator} is responsible for collecting collectibles, make them
 * persistent, initiating transfers, ..
 * <p>
 * The default implementation {@code Aggregator} is very unlikely to be
 * re-implemented.
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
	 * Collect the given collectible
	 * 
	 * @param collectible
	 * @return true on success
	 */
	boolean collect(final Collectible<?> collectible);

}
