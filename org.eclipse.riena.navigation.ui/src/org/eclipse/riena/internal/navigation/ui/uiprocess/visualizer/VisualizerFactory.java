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
package org.eclipse.riena.internal.navigation.ui.uiprocess.visualizer;

import java.util.List;

import org.eclipse.riena.internal.navigation.ui.marker.UIProcessFinsishedObserver;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ui.controllers.SubApplicationController;
import org.eclipse.riena.ui.core.uiprocess.IProgressVisualizer;
import org.eclipse.riena.ui.core.uiprocess.IProgressVisualizerLocator;
import org.eclipse.riena.ui.core.uiprocess.IProgressVisualizerObserver;
import org.eclipse.riena.ui.core.uiprocess.ProgressVisualizer;

public class VisualizerFactory implements IProgressVisualizerLocator {

	private IApplicationNode applicationNode;

	public VisualizerFactory(IApplicationNode applicationNode) {
		this.applicationNode = applicationNode;
	}

	@SuppressWarnings("unchecked")
	public IProgressVisualizer getProgressVisualizer(Object context) {
		IProgressVisualizer aVisualizer = new ProgressVisualizer();
		if (context != null && INavigationNode.class.isAssignableFrom(context.getClass())) {
			INavigationNode node = INavigationNode.class.cast(context);
			ISubApplicationNode subApp = (ISubApplicationNode) node.getParentOfType(ISubApplicationNode.class);
			if (subApp != null) {
				aVisualizer.addObserver(getUIProcessRidget(subApp));
				aVisualizer.addObserver(createObserver(node));
				for (ISubApplicationNode aSubApplicationNode : getSubApplications()) {
					if (aSubApplicationNode.getNavigationNodeController() != null
							&& ((SubApplicationController) aSubApplicationNode.getNavigationNodeController()).getStatuslineRidget() != null) {
						aVisualizer.addObserver(((SubApplicationController) aSubApplicationNode.getNavigationNodeController())
								.getStatuslineRidget().getStatuslineProcessRidget());
					}
				}
			}
		}
		return aVisualizer;
	}

	private UIProcessFinsishedObserver createObserver(INavigationNode<?> node) {
		return new UIProcessFinsishedObserver(node);
	}

	private List<ISubApplicationNode> getSubApplications() {
		return applicationNode.getChildren();
	}

	private IProgressVisualizerObserver getUIProcessRidget(ISubApplicationNode subApp) {
		return ((SubApplicationController) subApp.getNavigationNodeController()).getUiProcessRidget();
	}
}
