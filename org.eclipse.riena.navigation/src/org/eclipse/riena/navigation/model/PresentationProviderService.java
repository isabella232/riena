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

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.navigation.Activator;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNodeBuilder;
import org.eclipse.riena.navigation.INavigationNodeExtension;
import org.eclipse.riena.navigation.IPresentationProviderService;
import org.eclipse.riena.navigation.ISubModuleExtension;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.ui.ridgets.controller.IController;
import org.osgi.service.log.LogService;

/**
 * This class provides service methods to get information provided by
 * WorkAreaPresentationDefinitions and NavigationNodePresentationDefitinios
 * identified by a given presentationID.
 */
public class PresentationProviderService implements IPresentationProviderService {

	private final static Logger LOGGER = Activator.getDefault().getLogger(PresentationProviderService.class.getName());

	// TODO: split off ... problem: navigation is gui-less ...
	private static final String EP_SUBMODULETYPE = "org.eclipse.riena.navigation.subModule"; //$NON-NLS-1$
	private static final String EP_NAVNODETYPE = "org.eclipse.riena.navigation.navigationNode"; //$NON-NLS-1$
	private SubModuleExtensionInjectionHelper targetSM;
	private NavigationNodeExtensionInjectionHelper targetNN;

	/**
	 * 
	 */
	public PresentationProviderService() {

		targetSM = new SubModuleExtensionInjectionHelper();
		Inject.extension(EP_SUBMODULETYPE).useType(getSubModuleTypeDefinitionIFSafe()).into(targetSM).andStart(
				Activator.getDefault().getContext());

		targetNN = new NavigationNodeExtensionInjectionHelper();
		Inject.extension(EP_NAVNODETYPE).useType(getNavigationNodeTypeDefinitonIFSafe()).into(targetNN).andStart(
				Activator.getDefault().getContext());

	}

	private Class<? extends ISubModuleExtension> getSubModuleTypeDefinitionIFSafe() {

		if (getSubModuleTypeDefinitionIF() != null && getSubModuleTypeDefinitionIF().isInterface()) {
			return getSubModuleTypeDefinitionIF();
		} else {
			return ISubModuleExtension.class;
		}
	}

	private Class<? extends INavigationNodeExtension> getNavigationNodeTypeDefinitonIFSafe() {

		if (getNavigationNodeTypeDefinitonIF() != null && getNavigationNodeTypeDefinitonIF().isInterface()) {
			return getNavigationNodeTypeDefinitonIF();
		} else {
			return INavigationNodeExtension.class;
		}
	}

	public Class<? extends INavigationNodeExtension> getNavigationNodeTypeDefinitonIF() {

		return INavigationNodeExtension.class;
	}

	public Class<? extends ISubModuleExtension> getSubModuleTypeDefinitionIF() {

		return ISubModuleExtension.class;
	}

	/**
	 * @see org.eclipse.riena.navigation.IPresentationProviderService#createNode(org.eclipse.riena.navigation.INavigationNode,
	 *      org.eclipse.riena.navigation.NavigationNodeId,
	 *      org.eclipse.riena.navigation.NavigationArgument)
	 */
	@SuppressWarnings("unchecked")
	protected INavigationNode<?> _provideNode(INavigationNode<?> sourceNode, NavigationNodeId targetId,
			NavigationArgument argument) {
		INavigationNode<?> targetNode = findNode(getRootNode(sourceNode), targetId);
		if (targetNode == null) {
			if (LOGGER.isLoggable(LogService.LOG_DEBUG)) {
				LOGGER.log(LogService.LOG_DEBUG, "createNode: " + targetId); //$NON-NLS-1$
			}
			INavigationNodeExtension navigationNodeTypeDefiniton = getNavigationNodeTypeDefinition(targetId);
			if (navigationNodeTypeDefiniton != null) {
				INavigationNodeBuilder builder = navigationNodeTypeDefiniton.createNodeBuilder();
				prepareNavigationNodeBuilder(targetId, builder);
				targetNode = builder.buildNode(targetId, argument);
				INavigationNode parentNode = null;
				if (argument != null && argument.getParentNodeId() != null) {
					parentNode = _provideNode(sourceNode, argument.getParentNodeId(), null);
				} else {
					parentNode = _provideNode(sourceNode, new NavigationNodeId(navigationNodeTypeDefiniton
							.getParentTypeId()), null);
				}
				parentNode.addChild(targetNode);
			} else {
				throw new ExtensionPointFailure("NavigationNodeType not found. ID=" + targetId.getTypeId()); //$NON-NLS-1$
			}
		}
		return targetNode;
	}

	/**
	 * @see org.eclipse.riena.navigation.IPresentationProviderService#createNode(org.eclipse.riena.navigation.INavigationNode,
	 *      org.eclipse.riena.navigation.NavigationNodeId,
	 *      org.eclipse.riena.navigation.NavigationArgument)
	 */
	public INavigationNode<?> provideNode(INavigationNode<?> sourceNode, NavigationNodeId targetId,
			NavigationArgument argument) {
		final INavigationNode<?> targetNode = _provideNode(sourceNode, targetId, argument);
		return targetNode;
	}

	/**
	 * Used to prepare the NavigationNodeBuilder in a application specific way.
	 * 
	 * @param targetId
	 * @param builder
	 */
	protected void prepareNavigationNodeBuilder(NavigationNodeId targetId, INavigationNodeBuilder builder) {
		// can be overwritten by subclass
	}

	/**
	 * @param targetId
	 * @return
	 */
	protected ISubModuleExtension getSubModuleTypeDefinition(String targetId) {
		if (targetSM == null || targetSM.getData().length == 0) {
			return null;
		} else {
			ISubModuleExtension[] data = targetSM.getData();
			for (int i = 0; i < data.length; i++) {
				if (data[i].getTypeId() != null && data[i].getTypeId().equals(targetId)) {
					return data[i];
				}
			}
		}
		return null;
	}

	/**
	 * @param targetId
	 * @return
	 */
	protected INavigationNodeExtension getNavigationNodeTypeDefinition(NavigationNodeId targetId) {
		if (targetNN == null || targetNN.getData().length == 0 || targetId == null) {
			return null;
		} else {
			INavigationNodeExtension[] data = targetNN.getData();
			for (int i = 0; i < data.length; i++) {
				if (data[i].getTypeId() != null && data[i].getTypeId().equals(targetId.getTypeId())) {
					return data[i];
				}

			}
		}
		return null;
	}

	/**
	 * @param node
	 * @return
	 */
	protected INavigationNode<?> getRootNode(INavigationNode<?> node) {
		if (node.getParent() == null) {
			return node;
		}
		return getRootNode(node.getParent());
	}

	/**
	 * @param node
	 * @param targetId
	 * @return
	 */
	protected INavigationNode<?> findNode(INavigationNode<?> node, NavigationNodeId targetId) {
		if (targetId == null) {
			return null;
		}
		if (targetId.equals(node.getNodeId())) {
			return node;
		}
		for (INavigationNode<?> child : node.getChildren()) {
			INavigationNode<?> foundNode = findNode(child, targetId);
			if (foundNode != null) {
				return foundNode;
			}
		}
		return null;
	}

	/**
	 * This is the basic SWT implementation from Riena. It returns the matching
	 * view id for the given navigationNodeId
	 * 
	 * @see org.eclipse.riena.navigation.IPresentationProviderService#createView
	 *      (org.eclipse.riena.navigation.NavigationNodeId)
	 */
	public Object provideView(NavigationNodeId nodeId) {
		ISubModuleExtension subModuleTypeDefinition = getSubModuleTypeDefinition(nodeId.getTypeId());
		if (subModuleTypeDefinition != null) {
			return subModuleTypeDefinition.getView();
		} else {
			throw new ExtensionPointFailure("SubModuleType not found. ID=" + nodeId.getTypeId()); //$NON-NLS-1$
		}
	}

	/**
	 * @see org.eclipse.riena.navigation.IPresentationProviderService#provideController(org.eclipse.riena.navigation.INavigationNode)
	 */
	public IController provideController(INavigationNode<?> node) {
		ISubModuleExtension subModuleTypeDefinition = getSubModuleTypeDefinition(node.getNodeId().getTypeId());
		IController controller = null;

		if (subModuleTypeDefinition != null) {
			controller = subModuleTypeDefinition.createController();
		}

		return controller;
	}

	/**
	 * @see org.eclipse.riena.navigation.IPresentationProviderService#isViewShared(org.eclipse.riena.navigation.NavigationNodeId)
	 */
	public boolean isViewShared(NavigationNodeId targetId) {
		ISubModuleExtension subModuleTypeDefinition = getSubModuleTypeDefinition(targetId.getTypeId());

		if (subModuleTypeDefinition != null) {
			return subModuleTypeDefinition.isShared();
		}
		return false;
	}

	/**
	 * @see org.eclipse.riena.navigation.IPresentationProviderService#cleanUp()
	 */
	public void cleanUp() {
		// TODO: implement, does nothing special yet
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.navigation.IPresentationProviderService#
	 * getSubModuleTypeDefitinions()
	 */
	public ISubModuleExtension[] getSubModuleTypeDefitinions() {
		if (targetSM != null) {
			return targetSM.getData();
		}
		return null;
	}

	public class SubModuleExtensionInjectionHelper {
		private ISubModuleExtension[] data;

		public void update(ISubModuleExtension[] data) {
			this.data = data.clone();

		}

		public ISubModuleExtension[] getData() {
			return data.clone();
		}
	}

	public class NavigationNodeExtensionInjectionHelper {
		private INavigationNodeExtension[] data;

		public void update(INavigationNodeExtension[] data) {
			this.data = data.clone();

		}

		public INavigationNodeExtension[] getData() {
			return data.clone();
		}
	}
}
