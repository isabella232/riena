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
package org.eclipse.riena.navigation.ui.application;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.riena.internal.navigation.ui.uiprocess.visualizer.VisualizerFactory;
import org.eclipse.riena.navigation.ApplicationModelManager;
import org.eclipse.riena.navigation.IApplicationModel;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.ApplicationModel;
import org.eclipse.riena.navigation.model.NavigationNodeId;
import org.eclipse.riena.ui.core.resource.IIconManager;
import org.eclipse.riena.ui.core.uiprocess.ProgressProviderBridge;

/**
 * Abstract application defining the basic structure of a Riena application
 */
public abstract class AbstractApplication implements IApplication {

	public Object start(IApplicationContext context) throws Exception {
		IApplicationModel model = createModel();
		ApplicationModelManager.registerApplicationModel(model);
		initializeModel(model);
		setProgressProviderBridge(model);
		return createView(context, model);
	}

	private void setProgressProviderBridge(IApplicationModel model) {
		ProgressProviderBridge bridge = ProgressProviderBridge.instance();
		Job.getJobManager().setProgressProvider(bridge);
		bridge.setVisualizerFactory(new VisualizerFactory(model));
	}

	/**
	 * Overwrite to create own application model
	 * 
	 * @return IApplicationModelProvider - root of the configured application
	 *         model
	 */
	protected IApplicationModel createModel() {
		ApplicationModel applicationModel = new ApplicationModel();
		applicationModel.setPresentationId(new NavigationNodeId("application"));
		return applicationModel;
	}

	protected void initializeModel(IApplicationModel model) {
		initializeModelDefaults(model);
	}

	protected void initializeModelDefaults(IApplicationModel model) {
		initializeNodeDefaults(model);
	}

	protected void initializeNodeDefaults(IApplicationModel node) {

		for (ISubApplicationNode child : node.getChildren()) {
			initializeNodeDefaults(child);
		}
	}

	protected void initializeNodeDefaults(ISubApplicationNode node) {

		for (IModuleGroupNode child : node.getChildren()) {
			initializeNodeDefaults(child);
		}
	}

	protected void initializeNodeDefaults(IModuleGroupNode node) {

		for (IModuleNode child : node.getChildren()) {
			initializeNodeDefaults(child);
		}
	}

	protected void initializeNodeDefaults(IModuleNode node) {

		initializeNodeDefaultIcon(node);
		for (ISubModuleNode child : node.getChildren()) {
			initializeNodeDefaults(child);
		}
	}

	protected void initializeNodeDefaults(ISubModuleNode node) {

		initializeNodeDefaultIcon(node);
		for (ISubModuleNode child : node.getChildren()) {
			initializeNodeDefaults(child);
		}
	}

	protected void initializeNodeDefaultIcon(INavigationNode<?> node) {

		if (node.getIcon() == null) {
			node.setIcon(IIconManager.DEFAULT_ICON);
		}
	}

	abstract protected Object createView(IApplicationContext context, IApplicationModel pModel) throws Exception;
}
