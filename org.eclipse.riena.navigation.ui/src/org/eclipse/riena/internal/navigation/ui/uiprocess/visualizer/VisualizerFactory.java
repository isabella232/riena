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
import org.eclipse.riena.navigation.IApplicationModel;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplication;
import org.eclipse.riena.navigation.ui.controllers.SubApplicationViewController;
import org.eclipse.riena.ui.core.uiprocess.IProgressVisualizer;
import org.eclipse.riena.ui.core.uiprocess.IProgressVisualizerObserver;
import org.eclipse.riena.ui.core.uiprocess.IUICallbackDispatcherFactory;
import org.eclipse.riena.ui.core.uiprocess.ProgressVisualizer;

public class VisualizerFactory implements IUICallbackDispatcherFactory {

	private IApplicationModel applicationModel;

	public VisualizerFactory(IApplicationModel applicationModel) {
		this.applicationModel = applicationModel;
	}

	@SuppressWarnings("unchecked")
	public IProgressVisualizer getProgressVisualizer(Object context) {
		IProgressVisualizer aVisualizer = new ProgressVisualizer();
		if (context != null && INavigationNode.class.isAssignableFrom(context.getClass())) {
			INavigationNode node = INavigationNode.class.cast(context);
			ISubApplication subApp = (ISubApplication) node.getParentOfType(ISubApplication.class);
			if (subApp != null) {
				aVisualizer.addObserver(getUIProcessRidget(subApp));
				aVisualizer.addObserver(createObserver(node));
				for (ISubApplication aSubApplicationNode : getSubApplications()) {
					if (aSubApplicationNode.getPresentation() != null
							&& ((SubApplicationViewController) aSubApplicationNode.getPresentation())
									.getStatusbarRidget() != null) {
						aVisualizer.addObserver(((SubApplicationViewController) aSubApplicationNode.getPresentation())
								.getStatusbarRidget().getStatusBarProcessRidget());
					}
				}
			}
		}
		return aVisualizer;
	}

	private UIProcessFinsishedObserver createObserver(INavigationNode<?> node) {
		return new UIProcessFinsishedObserver(node);
	}

	private List<ISubApplication> getSubApplications() {
		return applicationModel.getChildren();
	}

	private IProgressVisualizerObserver getUIProcessRidget(ISubApplication subApp) {
		return ((SubApplicationViewController) subApp.getPresentation()).getProgressBoxRidget();
	}
}
