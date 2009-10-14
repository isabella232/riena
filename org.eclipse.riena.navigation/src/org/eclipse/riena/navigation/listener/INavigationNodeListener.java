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
package org.eclipse.riena.navigation.listener;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNode.State;
import org.eclipse.riena.ui.filter.IUIFilter;

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

	/**
	 * This method is called if a marker was added to or removed from a node.
	 * 
	 * @param source
	 *            node
	 * @param marker
	 *            marker that was removed or added
	 */
	void markerChanged(S source, IMarker marker);

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

	/**
	 * This method is called when a filter was added to the given node.
	 * 
	 * @param source
	 *            node
	 * @param filter
	 *            new added filter
	 */
	void filterAdded(S source, IUIFilter filter);

	/**
	 * This method is called when a filter was removed from the given node.
	 * 
	 * @param source
	 *            node
	 * @param filter
	 *            removed filter
	 */
	void filterRemoved(S source, IUIFilter filter);

}
