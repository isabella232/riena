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

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.navigation.Activator;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.INavigationNodeId;
import org.eclipse.riena.navigation.INavigationNodePresentationDefiniton;
import org.eclipse.riena.navigation.INavigationNodeProvider;
import org.eclipse.riena.navigation.IPresentationDefinition;
import org.eclipse.riena.navigation.IPresentationProviderService;
import org.eclipse.riena.navigation.IWorkAreaPresentationDefinition;
import org.eclipse.riena.ui.ridgets.viewcontroller.IViewController;

/**
 * This class provides service methods to get information provided by
 * WorkAreaPresentationDefinitions and NavigationNodePresentationDefitinios
 * identified by a given presentationID.
 * 
 * 
 * 
 */
public class PresentationProviderService implements IPresentationProviderService {

	// TODO: split off ... problem: navigation is gui-less ...

	private static final String EP_WORKAREA = "org.eclipse.riena.navigation.WorkAreaPresentation";
	private static final String EP_NAVNODE = "org.eclipse.riena.navigation.NavigationNodePresentation";
	private PresentationExtensionInjectionHelper<IWorkAreaPresentationDefinition> targetWA;
	private PresentationExtensionInjectionHelper<INavigationNodePresentationDefiniton> targetNN;

	/**
	 * Injects the extension point into a given target. The target returns a set
	 * of presentation definitions
	 */
	private void inject(Class<? extends IPresentationDefinition> interfaceType, String wpID,
			PresentationExtensionInjectionHelper<? extends IPresentationDefinition> target) {
		Inject.extension(wpID).useType(interfaceType).into(target).andStart(Activator.getDefault().getContext());
	}

	public PresentationProviderService() {

		targetWA = new PresentationExtensionInjectionHelper<IWorkAreaPresentationDefinition>();
		inject(IWorkAreaPresentationDefinition.class, EP_WORKAREA, targetWA);
		targetNN = new PresentationExtensionInjectionHelper<INavigationNodePresentationDefiniton>();
		inject(INavigationNodePresentationDefiniton.class, EP_NAVNODE, targetNN);

	}

	/**
	 * @see org.eclipse.riena.navigation.IPresentationProviderService#createNode(org.eclipse.riena.navigation.INavigationNode,
	 *      org.eclipse.riena.navigation.INavigationNodeId, java.lang.Object)
	 */
	public INavigationNode<?> createNode(INavigationNode<?> sourceNode, INavigationNodeId targetId, Object argument) {
		INavigationNode<?> targetNode = findNode(getRootNode(sourceNode), targetId);

		if (targetNode == null) {
			INavigationNodePresentationDefiniton presentationDefinition = getPresentationDefinitionNN(targetId);
			if (presentationDefinition != null) {
				INavigationNodeProvider builder = presentationDefinition.createNodeProvider();
				targetNode = builder.buildNode(targetId, argument);

				INavigationNode parentNode = createNode(sourceNode, new NavigationNodeId(presentationDefinition
						.getParentPresentationId()), null);
				parentNode.addChild(targetNode);
			} else {
				// TODO throw some new type of failure
			}
		}

		return targetNode;
	}

	private IWorkAreaPresentationDefinition getPresentationDefinitionWA(String targetId) {

		if (targetWA == null || targetWA.getData().length == 0) {
			return null;
		} else {
			IWorkAreaPresentationDefinition[] data = targetWA.getData();
			for (int i = 0; i < data.length; i++) {
				if (data[i].getPresentationId() != null && data[i].getPresentationId().equals(targetId)) {
					return data[i];
				}

			}
		}
		return null;

	}

	private INavigationNodePresentationDefiniton getPresentationDefinitionNN(INavigationNodeId targetId) {

		if (targetNN == null || targetNN.getData().length == 0 || targetId == null) {
			return null;
		} else {
			INavigationNodePresentationDefiniton[] data = targetNN.getData();
			for (int i = 0; i < data.length; i++) {
				if (data[i].getPresentationId() != null && data[i].getPresentationId().equals(targetId.getTypeId())) {
					return data[i];
				}

			}
		}
		return null;

	}

	protected INavigationNode<?> getRootNode(INavigationNode<?> node) {
		if (node.getParent() == null) {
			return node;
		}
		return getRootNode(node.getParent());
	}

	protected INavigationNode<?> findNode(INavigationNode<?> node, INavigationNodeId targetId) {

		if (targetId == null) {
			return null;
		}
		if (targetId.equals(node.getPresentationId())) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.navigation.IPresentationProviderService#createView
	 * (org.eclipse.riena.navigation.INavigationNodeId)
	 */
	public Object createView(INavigationNodeId targetId) {
		IWorkAreaPresentationDefinition presentationDefinition = getPresentationDefinitionWA(targetId.getTypeId());
		Object view = null;

		if (presentationDefinition != null) {
			view = presentationDefinition.createView();
		}

		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.navigation.IPresentationDefinitionService#
	 * createViewController()
	 */
	public IViewController createViewController(INavigationNodeId targetId) {
		IWorkAreaPresentationDefinition presentationDefinition = getPresentationDefinitionWA(targetId.getTypeId());
		IViewController viewController = null;

		if (presentationDefinition != null) {
			viewController = presentationDefinition.createViewController();
		}

		return viewController;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.navigation.IPresentationProviderService#isViewShared ()
	 */
	public boolean isViewShared(INavigationNodeId targetId) {
		IWorkAreaPresentationDefinition presentationDefinition = getPresentationDefinitionWA(targetId.getTypeId());

		if (presentationDefinition != null) {
			return presentationDefinition.isViewShared();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.navigation.IPresentationProviderService#cleanUp()
	 */
	public void cleanUp() {
		// TODO: implement, does noething special yet

	}

}
