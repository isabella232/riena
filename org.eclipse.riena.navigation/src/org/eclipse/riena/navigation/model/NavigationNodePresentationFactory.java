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
import org.eclipse.riena.navigation.INavigationNodePresentationDefiniton;
import org.eclipse.riena.navigation.INavigationNodePresentationFactory;
import org.eclipse.riena.navigation.INodeProvider;

/**
 * 
 */
public class NavigationNodePresentationFactory implements INavigationNodePresentationFactory {

	private static final String ID = "de.compeople.scp.navigation.ui.swing.ModulePresentation";

	private static NavigationNodePresentationFactory factory;

	/**
	 * Constructor (private). no instance allowed.
	 */
	private NavigationNodePresentationFactory() {
		// TODO Auto-generated constructor stub

		NodePresentationData target = new NodePresentationData();
		Inject.extension(ID).useType(INavigationNodePresentationDefiniton.class).into(target).andStart(
				Activator.getDefault().getContext());
	}

	public INavigationNode<?> createNode(INavigationNode<?> sourceNode, String targetId) {

		INavigationNode targetNode = findNode(sourceNode, targetId);

		if (targetNode == null) {
			INavigationNodePresentationDefiniton presentationDefinition = getPresentationDefinition(targetId);
			INodeProvider provider = presentationDefinition.getProvider();
			targetNode = provider.provide();

			INavigationNode parentNode = createNode(sourceNode, presentationDefinition.getParent());
			parentNode.addChild(targetNode);
		}

		return targetNode;
	}

	private INavigationNodePresentationDefiniton getPresentationDefinition(String targetId) {
		if (factory == null) {
			// side effect:
			// instantiation of this class would populate instance variable
			// <code>webBrowserCreator</code>
			factory = new NavigationNodePresentationFactory();
		}

		// TODO EAC: get presentation definition for targetId

		return null;
	}

	private INavigationNode<?> findNode(INavigationNode<?> sourceNode, String targetId) {

		// search the tree that contains the sourceNode for a node with
		// the presentationId targetId...

		return null;
	}

	private class NodePresentationData {

		private INavigationNodePresentationDefiniton[] data;

		public void update(INavigationNodePresentationDefiniton[] data) {
			this.data = data;
		}

		public INavigationNodePresentationDefiniton[] getData() {
			return data;
		}

	}

}
