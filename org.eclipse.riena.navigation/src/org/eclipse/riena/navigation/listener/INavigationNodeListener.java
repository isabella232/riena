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
package org.eclipse.riena.navigation.listener;

import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNode.State;

/**
 * Each call back includes as the first parameter always the source of the
 * change, even if the source is sometimes not needed
 */
public interface INavigationNodeListener<S extends INavigationNode<C>, C extends INavigationNode<?>> {

	void labelChanged(S source);

	void iconChanged(S source);

	void selectedChanged(S source);

	void childAdded(S source, C childAdded);

	void childRemoved(S source, C childRemoved);

	void presentationChanged(S source);

	void parentChanged(S source);

	void expandedChanged(S source);

	void markersChanged(S source);

	void activated(S source);

	void beforeActivated(S source);

	void afterActivated(S source);

	void deactivated(S source);

	void beforeDeactivated(S source);

	void afterDeactivated(S source);

	void disposed(S source);

	void beforeDisposed(S source);

	void afterDisposed(S source);

	void stateChanged(S source, State oldState, State newState);

	void block(S source, boolean block);
}
