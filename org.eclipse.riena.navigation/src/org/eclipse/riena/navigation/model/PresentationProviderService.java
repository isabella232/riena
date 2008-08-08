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
import org.eclipse.riena.navigation.ApplicationModelFailure;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNodeBuilder;
import org.eclipse.riena.navigation.INavigationNodeId;
import org.eclipse.riena.navigation.INavigationNodeTypeDefiniton;
import org.eclipse.riena.navigation.IPresentationProviderService;
import org.eclipse.riena.navigation.ISubModuleTypeDefinition;
import org.eclipse.riena.navigation.ITypeDefinition;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.ui.ridgets.viewcontroller.IViewController;
import org.osgi.service.log.LogService;

/**
 * This class provides service methods to get information provided by
 * WorkAreaPresentationDefinitions and NavigationNodePresentationDefitinios
 * identified by a given presentationID.
 */
public class PresentationProviderService implements IPresentationProviderService {

	private final static Logger LOGGER = Activator.getDefault().getLogger(PresentationProviderService.class.getName());

	// TODO: split off ... problem: navigation is gui-less ...

	private static final String EP_WORKAREA = "org.eclipse.riena.navigation.SubModuleType"; //$NON-NLS-1$
	private static final String EP_NAVNODE = "org.eclipse.riena.navigation.NavigationNodeType"; //$NON-NLS-1$
	private PresentationExtensionInjectionHelper<ISubModuleTypeDefinition> targetWA;
	private PresentationExtensionInjectionHelper<INavigationNodeTypeDefiniton> targetNN;

	/**
	 * Injects the extension point into a given target. The target returns a set
	 * of presentation definitions
	 */
	private void inject(Class<? extends ITypeDefinition> interfaceType, String wpID,
			PresentationExtensionInjectionHelper<? extends ITypeDefinition> target) {
		Inject.extension(wpID).useType(interfaceType).into(target).andStart(Activator.getDefault().getContext());
	}

	/**
	 * 
	 */
	public PresentationProviderService() {
		targetWA = new PresentationExtensionInjectionHelper<ISubModuleTypeDefinition>();
		inject(ISubModuleTypeDefinition.class, EP_WORKAREA, targetWA);
		targetNN = new PresentationExtensionInjectionHelper<INavigationNodeTypeDefiniton>();
		inject(INavigationNodeTypeDefiniton.class, EP_NAVNODE, targetNN);
	}

	/**
	 * @see org.eclipse.riena.navigation.IPresentationProviderService#createNode(org.eclipse.riena.navigation.INavigationNode,
	 *      org.eclipse.riena.navigation.INavigationNodeId,
	 *      org.eclipse.riena.navigation.NavigationArgument)
	 */
	@SuppressWarnings("unchecked")
	public INavigationNode<?> provideNode(INavigationNode<?> sourceNode, INavigationNodeId targetId,
			NavigationArgument argument) {
		INavigationNode<?> targetNode = findNode(getRootNode(sourceNode), targetId);
		if (targetNode == null) {
			if (LOGGER.isLoggable(LogService.LOG_DEBUG))
				LOGGER.log(LogService.LOG_DEBUG, "createNode: " + targetId); //$NON-NLS-1$
			INavigationNodeTypeDefiniton presentationDefinition = getPresentationDefinitionNN(targetId);
			if (presentationDefinition != null) {
				INavigationNodeBuilder builder = presentationDefinition.createNodeBuilder();
				prepareNavigationNodeBuilder(targetId, builder);
				targetNode = builder.buildNode(targetId);
				INavigationNode parentNode = null;
				if (argument != null && argument.getParentNodeId() != null) {
					parentNode = provideNode(sourceNode, new NavigationNodeId(argument.getParentNodeId()), null);
				} else {
					parentNode = provideNode(sourceNode,
							new NavigationNodeId(presentationDefinition.getParentTypeId()), null);
				}
				parentNode.addChild(targetNode);
			} else {
				// TODO throw some new type of failure
			}
		}
		return targetNode;
	}

	/**
	 * Used to prepare the NavigationNodeBuilder in a application specific way.
	 * 
	 * @param targetId
	 * @param builder
	 */
	protected void prepareNavigationNodeBuilder(INavigationNodeId targetId, INavigationNodeBuilder builder) {
		// can be overwritten by subclass
	}

	/**
	 * @param targetId
	 * @return
	 */
	protected ISubModuleTypeDefinition getPresentationDefinitionWA(String targetId) {
		if (targetWA == null || targetWA.getData().length == 0) {
			return null;
		} else {
			ISubModuleTypeDefinition[] data = targetWA.getData();
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
	protected INavigationNodeTypeDefiniton getPresentationDefinitionNN(INavigationNodeId targetId) {
		if (targetNN == null || targetNN.getData().length == 0 || targetId == null) {
			return null;
		} else {
			INavigationNodeTypeDefiniton[] data = targetNN.getData();
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
	protected INavigationNode<?> findNode(INavigationNode<?> node, INavigationNodeId targetId) {
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
	 *      (org.eclipse.riena.navigation.INavigationNodeId)
	 */
	public Object provideView(INavigationNodeId nodeId) {
		ISubModuleTypeDefinition presentationDefinition = getPresentationDefinitionWA(nodeId.getTypeId());
		if (presentationDefinition != null) {
			return presentationDefinition.getView();
		} else {
			throw new ApplicationModelFailure("No presentation definition found for node '" + nodeId.getTypeId() + "'."); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * @see org.eclipse.riena.navigation.IPresentationProviderService#provideController(org.eclipse.riena.navigation.INavigationNode)
	 */
	public IViewController provideController(INavigationNode<?> node) {
		ISubModuleTypeDefinition presentationDefinition = getPresentationDefinitionWA(node.getNodeId().getTypeId());
		IViewController viewController = null;

		if (presentationDefinition != null) {
			viewController = presentationDefinition.createController();
		}

		return viewController;
	}

	/**
	 * @see org.eclipse.riena.navigation.IPresentationProviderService#isViewShared(org.eclipse.riena.navigation.INavigationNodeId)
	 */
	public boolean isViewShared(INavigationNodeId targetId) {
		ISubModuleTypeDefinition presentationDefinition = getPresentationDefinitionWA(targetId.getTypeId());

		if (presentationDefinition != null) {
			return presentationDefinition.isShared();
		}
		return false;
	}

	/**
	 * @see org.eclipse.riena.navigation.IPresentationProviderService#cleanUp()
	 */
	public void cleanUp() {
		// TODO: implement, does nothing special yet
	}

}
