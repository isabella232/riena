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
package org.eclipse.riena.internal.navigation.ui.marker;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.ui.core.marker.UIProcessFinishedMarker;
import org.eclipse.riena.ui.core.uiprocess.IProgressVisualizer;
import org.eclipse.riena.ui.core.uiprocess.IProgressVisualizerObserver;
import org.eclipse.riena.ui.core.uiprocess.UIProcess;

/**
 * Observer for of {@link IProgressVisualizer}
 * {@link #finalUpdateUI(IProgressVisualizer)}. When notified the observer will
 * delegate the markup of nodes showing the path to the node where the
 * {@link UIProcess }was started
 */
public class UIProcessFinishedObserver implements IProgressVisualizerObserver {

	private INavigationNode<?> baseNode;

	private IMarker uiProcessFinishedMarker;

	private IUIProcessMarkupStrategy markupStrategy;

	/**
	 * @param baseNode
	 *            node where the {@link UIProcess} was started
	 * @param markupStrategy
	 *            the strategy that handles the markup of the nodes showing the
	 *            path to baseNode
	 */
	public UIProcessFinishedObserver(INavigationNode<?> baseNode, IUIProcessMarkupStrategy markupStrategy) {
		this.baseNode = baseNode;
		uiProcessFinishedMarker = new UIProcessFinishedMarker();
		this.markupStrategy = markupStrategy;
	}

	public UIProcessFinishedObserver(INavigationNode<?> currentNode) {
		this(currentNode, new TypeHierarchyMarkerStrategy());
	}

	private INavigationNode<?> getBaseNode() {
		return baseNode;
	}

	public void finalUpdateUI(IProgressVisualizer visualizer) {
		getMarkupStrategy().applyUIProcessMarker(getBaseNode(), uiProcessFinishedMarker);
	}

	private IUIProcessMarkupStrategy getMarkupStrategy() {
		return markupStrategy;
	}

	////////////////////////// empty callbacks

	public void addProgressVisualizer(IProgressVisualizer visualizer) {
	}

	public void initialUpdateUI(IProgressVisualizer visualizer, int totalWork) {
	}

	public void removeProgressVisualizer(IProgressVisualizer visualizer) {
	}

	public void updateProgress(IProgressVisualizer visualizer, int progress) {
	}

}
