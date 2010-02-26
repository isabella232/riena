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
package org.eclipse.riena.navigation.ui.application;

import java.util.List;

import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.wire.InjectExtension;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.navigation.ui.Activator;
import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNodeProvider;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.StartupNodeInfo;
import org.eclipse.riena.navigation.model.ApplicationNode;
import org.eclipse.riena.navigation.model.NavigationNodeProvider;
import org.eclipse.riena.navigation.ui.login.ILoginDialogViewExtension;
import org.eclipse.riena.ui.core.uiprocess.ProgressProviderBridge;

/**
 * Abstract application defining the basic structure of a Riena application
 */
public abstract class AbstractApplication implements IApplication {

	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), AbstractApplication.class);
	protected ILoginDialogViewExtension loginDialogViewExtension;

	public Object start(IApplicationContext context) throws Exception {

		Object result = initializePerformLogin(context);
		if (!EXIT_OK.equals(result)) {
			return result;
		}

		IApplicationNode applicationNode = createModel();
		if (applicationNode == null) {
			throw new RuntimeException(
					"Application did not return an ApplicationModel in method 'createModel' but returned NULL. Cannot continue"); //$NON-NLS-1$
		}
		ApplicationNodeManager.registerApplicationNode(applicationNode);
		createStartupNodes(applicationNode);
		initializeNode(applicationNode);
		setProgressProviderBridge();
		return createView(context, applicationNode);
	}

	private void setProgressProviderBridge() {
		ProgressProviderBridge bridge = ProgressProviderBridge.instance();
		Job.getJobManager().setProgressProvider(bridge);
		bridge.setVisualizerFactory(new ProgressVisualizerLocator());
	}

	/**
	 * Overwrite to create own application model
	 * 
	 * @return IApplicationModelProvider - root of the configured application
	 *         model
	 */
	protected IApplicationNode createModel() {
		IApplicationNode applicationModel = new ApplicationNode(new NavigationNodeId(
				ApplicationNode.DEFAULT_APPLICATION_TYPEID));
		return applicationModel;
	}

	/**
	 * @since 1.2
	 */
	protected void createStartupNodes(IApplicationNode applicationNode) {
		INavigationNodeProvider navigationNodeProvider = NavigationNodeProvider.getInstance();
		List<StartupNodeInfo> startups = navigationNodeProvider.getSortedStartupNodeInfos();
		for (StartupNodeInfo startup : startups) {
			LOGGER.log(LogService.LOG_INFO, "creating " + startup.toString()); //$NON-NLS-1$
			applicationNode.create(new NavigationNodeId(startup.getId()));
		}
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
		//		for (ISubModuleNode child : node.getChildren()) {
		//			initializeNodeDefaults(child);
		//		}
	}

	//
	//	protected void initializeNodeDefaults(ISubModuleNode node) {
	//
	//		initializeNodeDefaultIcon(node);
	//		for (ISubModuleNode child : node.getChildren()) {
	//			initializeNodeDefaults(child);
	//		}
	//	}

	protected void initializeNodeDefaultIcon(INavigationNode<?> node) {
		//		if (node.getIcon() == null) {
		//			node.setIcon("defaultNode"); //$NON-NLS-1$
		//		}
	}

	abstract protected Object createView(IApplicationContext context, IApplicationNode pNode) throws Exception;

	protected Object initializePerformLogin(IApplicationContext context) throws Exception {

		initializeLoginViewDefinition();

		if (isDialogLogin(context)) {
			return performLogin(context);
		} else {
			return EXIT_OK;
		}
	}

	protected boolean isDialogLogin(IApplicationContext context) {
		return loginDialogViewExtension != null;
	}

	protected boolean isSplashLogin(IApplicationContext context) {
		return false;
	}

	protected Object doPerformLogin(IApplicationContext context) {

		return EXIT_OK;
	}

	protected Object doPerformSplashLogin(IApplicationContext context) {

		return EXIT_OK;
	}

	protected Object performLogin(IApplicationContext context) throws Exception {

		if (isSplashLogin(context)) {
			return doPerformSplashLogin(context);
		} else {
			return doPerformLogin(context);
		}
	}

	@InjectExtension
	public void update(ILoginDialogViewExtension[] data) {

		if (data.length > 0) {
			loginDialogViewExtension = data[0];
		}
	}

	protected void initializeLoginViewDefinition() {
		Wire.instance(this).andStart(Activator.getDefault().getContext());
	}
}
