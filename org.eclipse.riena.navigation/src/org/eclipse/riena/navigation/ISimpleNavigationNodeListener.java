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
package org.eclipse.riena.navigation;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.navigation.INavigationNode.State;
import org.eclipse.riena.navigation.model.SimpleNavigationNodeAdapter;
import org.eclipse.riena.ui.filter.IUIFilter;

/**
 * Listener for various events related to a navigation node and it's lifecycle.
 * <p>
 * Because this interface has many methods, but implementors are typically only
 * interested in a few of them, it is is recommended to extend the
 * {@link SimpleNavigationNodeAdapter}.
 * 
 * @see SimpleNavigationNodeAdapter
 */
public interface ISimpleNavigationNodeListener {

	void labelChanged(INavigationNode<?> source);

	void iconChanged(INavigationNode<?> source);

	void selectedChanged(INavigationNode<?> source);

	void childAdded(INavigationNode<?> source, INavigationNode<?> childAdded);

	void childRemoved(INavigationNode<?> source, INavigationNode<?> childRemoved);

	void presentationChanged(INavigationNode<?> source);

	void parentChanged(INavigationNode<?> source);

	void expandedChanged(INavigationNode<?> source);

	/**
	 * This method is called if a marker was added to or removed from a node.
	 * 
	 * @param source
	 *            node
	 * @param marker
	 *            marker that was removed or added
	 */
	void markerChanged(INavigationNode<?> source, IMarker marker);

	void activated(INavigationNode<?> source);

	void beforeActivated(INavigationNode<?> source);

	void afterActivated(INavigationNode<?> source);

	void deactivated(INavigationNode<?> source);

	void beforeDeactivated(INavigationNode<?> source);

	void afterDeactivated(INavigationNode<?> source);

	void disposed(INavigationNode<?> source);

	void beforeDisposed(INavigationNode<?> source);

	void afterDisposed(INavigationNode<?> source);

	void stateChanged(INavigationNode<?> source, State oldState, State newState);

	void block(INavigationNode<?> source, boolean block);

	void filterRemoved(INavigationNode<?> source, IUIFilter filter);

	void filterAdded(INavigationNode<?> source, IUIFilter filter);

}
