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
import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ApplicationNode;
import org.eclipse.riena.ui.core.resource.IIconManager;
import org.eclipse.riena.ui.core.uiprocess.ProgressProviderBridge;

/**
 * Abstract application defining the basic structure of a Riena application
 */
public abstract class AbstractApplication implements IApplication {
	public static final String DEFAULT_APPLICATION_TYPEID = "application"; //$NON-NLS-1$

	public Object start(IApplicationContext context) throws Exception {
		IApplicationNode node = createModel();
		ApplicationNodeManager.registerApplicationNode(node);
		initializeNode(node);
		setProgressProviderBridge(node);
		return createView(context, node);
	}

	private void setProgressProviderBridge(IApplicationNode model) {
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
	protected IApplicationNode createModel() {
		IApplicationNode applicationModel = new ApplicationNode(new NavigationNodeId(DEFAULT_APPLICATION_TYPEID));
		return applicationModel;
	}

	protected void initializeNode(IApplicationNode model) {
		initializeModelDefaults(model);
	}

	protected void initializeModelDefaults(IApplicationNode model) {
		initializeNodeDefaults(model);
	}

	protected void initializeNodeDefaults(IApplicationNode node) {

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

	abstract protected Object createView(IApplicationContext context, IApplicationNode pNode) throws Exception;
}
