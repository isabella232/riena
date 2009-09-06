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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.core.databinding.observable.value.WritableValue;

import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.listener.NavigationTreeObserver;
import org.eclipse.riena.navigation.listener.SubModuleNodeListener;
import org.eclipse.riena.navigation.model.NavigationNode;
import org.eclipse.riena.navigation.ui.controllers.ModuleController;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.ridgets.tree2.ITreeNode;

/**
 * Controller of a module.
 */
public class SWTModuleController extends ModuleController {

	private ITreeRidget tree;
	private final static String PROPERTY_ENABLED = "enabled"; //$NON-NLS-1$
	private final static String PROPERTY_VISIBLE = "visible"; //$NON-NLS-1$
	private final static String PROPERTY_IMAGE = "icon"; //$NON-NLS-1$

	/**
	 * @param navigationNode
	 */
	public SWTModuleController(IModuleNode navigationNode) {
		super(navigationNode);
		addListeners();
	}

	/**
	 * @param tree
	 *            the tree to set
	 */
	public void setTree(ITreeRidget tree) {
		this.tree = tree;
	}

	/**
	 * @return the tree
	 */
	public ITreeRidget getTree() {
		return tree;
	}

	@Override
	public void afterBind() {
		super.afterBind();
		updateNavigationNodeMarkers();
		bindTree();
	}

	// helping methods
	//////////////////

	/**
	 * Adds listeners for sub-module and module nodes.
	 */
	private void addListeners() {
		NavigationTreeObserver navigationTreeObserver = new NavigationTreeObserver();
		navigationTreeObserver.addListener(new SubModuleListener());
		navigationTreeObserver.addListenerTo(getNavigationNode());
	}

	/**
	 * Binds the tree to a selection model and tree model.
	 */
	private void bindTree() {
		tree.setRootsVisible(false);
		INavigationNode<?>[] roots = createTreeRootNodes();
		tree.bindToModel(roots, NavigationNode.class, ITreeNode.PROPERTY_CHILDREN, ITreeNode.PROPERTY_PARENT,
				"label", PROPERTY_ENABLED, PROPERTY_VISIBLE, PROPERTY_IMAGE); //$NON-NLS-1$
		tree.setSelectionType(ISelectableRidget.SelectionType.SINGLE);
		tree.bindSingleSelectionToModel(new DelegatingValue());
		selectActiveNode();
	}

	/**
	 * Creates the list of the root nodes of the tree.<br>
	 * This method returns only one root, the module node. So dynamically
	 * sub-module can be added directly below the module node and they are
	 * displayed in the tree (without generating and binding a new model with
	 * new root nodes.)
	 * 
	 * @return root nodes
	 */
	private IModuleNode[] createTreeRootNodes() {
		IModuleNode moduleNode = getNavigationNode();
		return new IModuleNode[] { moduleNode };
	}

	/**
	 * Selects the active sub-module in the tree.
	 * 
	 * @param node
	 */
	private void setSelectedNode(INavigationNode<?> node) {
		if (node.isActivated() && (node != getNavigationNode())) {
			tree.setSelection(node);
		}
		for (INavigationNode<?> child : node.getChildren()) {
			setSelectedNode(child);
		}
	}

	/**
	 * Selects the active sub-module of this module in the tree.
	 */
	private void selectActiveNode() {
		setSelectedNode(getNavigationNode());
	}

	// helping classes
	//////////////////

	/**
	 * Activates the selected sub-module node.
	 */
	private static class DelegatingValue extends WritableValue {
		@Override
		public void doSetValue(Object value) {
			// TODO [ev] handle submodule activation / isSelectable here
			ISubModuleNode node = (ISubModuleNode) value;
			node.activate();
		}
	}

	/**
	 * Updates the tree if a sub-module node is added or remove form parent
	 * sub-module node.
	 */
	private class SubModuleListener extends SubModuleNodeListener {
		@Override
		public void activated(ISubModuleNode source) {
			super.activated(source);
			selectActiveNode();
		}
	}

}
