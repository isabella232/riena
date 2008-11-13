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
package org.eclipse.riena.navigation.model;

import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.util.InvocationTargetFailure;
import org.eclipse.riena.internal.navigation.Activator;
import org.eclipse.riena.navigation.INavigationNodeExtension;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ISubModuleNodeExtension;
import org.eclipse.riena.navigation.ISubModuleViewBuilder;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.ui.ridgets.controller.IController;
import org.osgi.service.log.LogService;

/**
 *
 */
public class SubModuleViewBuilder implements ISubModuleViewBuilder {

	private final static Logger LOGGER = Activator.getDefault().getLogger(NavigationNodeProvider.class);

	/**
	 * 
	 */
	public SubModuleViewBuilder() {
	}

	protected INavigationNodeExtension getNavigationNodeDefinition(NavigationNodeId targetId) {

		NavigationNodeProvider p = (NavigationNodeProvider) NavigationNodeProviderAccessor.current()
				.getNavigationNodeProvider();
		return p.getNavigationNodeTypeDefinition(targetId);
	}

	protected INavigationNodeExtension getNavigationNodeDefinition(String targetId) {

		return getNavigationNodeDefinition(new NavigationNodeId(targetId));
	}

	protected ISubModuleNodeExtension getSubModuleNodeDefinition(NavigationNodeId targetId) {

		NavigationNodeProvider p = (NavigationNodeProvider) NavigationNodeProviderAccessor.current()
				.getNavigationNodeProvider();
		return p.getSubModuleNodeDefinition(targetId);
	}

	protected ISubModuleNodeExtension getSubModuleNodeDefinition(String targetId) {

		return getSubModuleNodeDefinition(new NavigationNodeId(targetId));
	}

	/**
	 * This is the basic SWT implementation from Riena. It returns the matching
	 * view id for the given navigationNodeId
	 * 
	 * @see org.eclipse.riena.navigation.INavigationNodeProvider#createView
	 *      (org.eclipse.riena.navigation.NavigationNodeId)
	 */
	public Object provideView(ISubModuleNode node) {

		Assert.isNotNull(node, "navigation node must not be null"); //$NON-NLS-1$
		Assert.isNotNull(node.getNodeId(), "navigation node id must not be null"); //$NON-NLS-1$

		// first consult node itself - maybe view id was explicitly set
		if (node.getViewId() != null) {
			return node.getViewId();
		}

		// not explicitly set - search extensions
		ISubModuleNodeExtension subModuleNodeDefinition = getSubModuleNodeDefinition(node.getNodeId().getTypeId());
		if (subModuleNodeDefinition != null && subModuleNodeDefinition.getView() != null) {
			return subModuleNodeDefinition.getView();
		}

		throw new ExtensionPointFailure(String.format(
				"view definition not found [nodeId=%s].", node.getNodeId().getTypeId())); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeProvider#provideController(org.eclipse.riena.navigation.INavigationNode)
	 */
	public IController provideController(ISubModuleNode node) {

		IController controller = null;

		Assert.isNotNull(node, "navigation node must not be null"); //$NON-NLS-1$
		Assert.isNotNull(node.getNodeId(), "navigation node id must not be null"); //$NON-NLS-1$

		// first consult node itself - maybe controller class was explicitly set
		if (node.getControllerClassForView() != null) {
			try {
				return (IController) node.getControllerClassForView().newInstance();
			} catch (IllegalAccessException ex) {
				String message = String.format(
						"cannnot create controller for class %s", node.getControllerClassForView()); //$NON-NLS-1$ 
				LOGGER.log(LogService.LOG_ERROR, message, ex);
				throw new InvocationTargetFailure(message, ex);
			} catch (InstantiationException ex) {
				String message = String.format(
						"cannnot create controller for class %s", node.getControllerClassForView()); //$NON-NLS-1$
				LOGGER.log(LogService.LOG_ERROR, message, ex);
				throw new InvocationTargetFailure(message, ex);
			}
		}

		// not explicitly set - search extensions
		ISubModuleNodeExtension subModuleNodeDefinition = getSubModuleNodeDefinition(node.getNodeId());
		if (subModuleNodeDefinition != null && subModuleNodeDefinition.getView() != null) {
			return subModuleNodeDefinition.createController();
		}

		return controller;
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeProvider#isViewShared(org.eclipse.riena.navigation.NavigationNodeId)
	 */
	public boolean isViewShared(ISubModuleNode node) {

		INavigationNodeExtension nodeDefinition = getNavigationNodeDefinition(node.getNodeId().getTypeId());
		if (nodeDefinition != null) {
			return nodeDefinition.isShared();
		}

		// not explicitly set - search extensions
		ISubModuleNodeExtension subModuleNodeDefinition = getSubModuleNodeDefinition(node.getNodeId());
		if (subModuleNodeDefinition != null && subModuleNodeDefinition.getView() != null) {
			return subModuleNodeDefinition.isShared();
		}

		return false;
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeProvider#cleanUp()
	 */
	public void cleanUp() {
		// TODO: implement, does nothing special yet
	}
}
