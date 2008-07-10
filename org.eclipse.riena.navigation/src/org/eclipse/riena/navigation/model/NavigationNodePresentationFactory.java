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

/**
 * 
 */
public class NavigationNodePresentationFactory implements INavigationNodePresentationFactory {

	private static final String ID = "de.compeople.scp.navigation.ui.swing.ModulePresentation";

	private static NavigationNodePresentationFactory factory;

	private NodePresentationData target = null;

	public NavigationNodePresentationFactory() {
		// TODO Auto-generated constructor stub

		// instantiation of this class would populate instance variable
		// <code>webBrowserCreator</code>

		target = new NodePresentationData();
		Inject.extension(ID).useType(INavigationNodePresentationDefiniton.class).into(target).andStart(
				Activator.getDefault().getContext());
	}

	public INavigationNode<?> createNode(INavigationNode<?> sourceNode, String targetId) {

		INavigationNode targetNode = findNode(getRootNode(sourceNode), targetId);

		// TODO: einkommentieren wenn Methoden da
		// if (targetNode == null) {
		// INavigationNodePresentationDefiniton presentationDefinition =
		// getPresentationDefinition(targetId);
		// INavigationNodeProvider provider =
		// presentationDefinition.getProvider();
		// targetNode = provider.provide();
		//
		// INavigationNode parentNode = createNode(sourceNode,
		// presentationDefinition.getParent());
		// parentNode.addChild(targetNode);
		// }

		return targetNode;
	}

	public INavigationNodePresentationDefiniton getPresentationDefinition(String targetId) {

		if (target == null || target.getData().length == 0) {
			return null;
		} else {
			INavigationNodePresentationDefiniton[] data = target.getData();
			for (int i = 0; i < data.length; i++) {
				if (data[i].getPresentationId() != null && data[i].getPresentationId().equals(targetId)) {
					return data[i];
				}

			}
		}
		return null;

	}

	private INavigationNode<?> getRootNode(INavigationNode<?> node) {
		if (node.getParent() == null) {
			return node;
		}
		return getRootNode(node.getParent());
	}

	private INavigationNode<?> findNode(INavigationNode<?> node, String targetId) {

		if (targetId.equals(node.getPresentationId())) {
			return node;
		}
		for (INavigationNode<?> child : node.getChildren()) {
			INavigationNode<?> foundNode = findNode(child, targetId);
			if (foundNode != null) {
				return findNode(child, targetId);
			}
		}
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
