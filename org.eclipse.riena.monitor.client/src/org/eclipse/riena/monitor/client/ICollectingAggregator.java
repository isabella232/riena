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
 * The consumer of the IAggregator service shall not see the possibility to
 * collect collectibles. Only {@code ICollectors} shall collect stuff and pass
 * it to the aggregator.
 */
public interface ICollectingAggregator extends IAggregator {
	/**
	 * Collect the given collectible
	 * 
	 * @param collectible
	 */
	void collect(final Collectible<?> collectible);

}
