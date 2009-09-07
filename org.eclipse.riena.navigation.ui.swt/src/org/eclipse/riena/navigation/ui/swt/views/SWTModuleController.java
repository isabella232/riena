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

import java.util.List;

import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;

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
	 * Creates the list of the root nodes of the tree.
	 * <p>
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

	// TODO [ev] test these:

	/**
	 * Non-API. Public for testing only.
	 * 
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public static ISubModuleNode getSelectableChild(ISubModuleNode node) {
		ISubModuleNode result = null;
		for (ISubModuleNode child : node.getChildren()) {
			if (child.isSelectable()) {
				result = child;
				break;
			}
		}
		return result;
	}

	/**
	 * Non-API. Public for testing only.
	 * 
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public static ISubModuleNode findNodeToActivate(ISubModuleNode oldNode, ISubModuleNode newNode) {
		ISubModuleNode result = newNode;
		if (!result.isSelectable()) {
			ISubModuleNode firstChild = getSelectableChild(result);
			if (firstChild == oldNode) { // move up
				result = findPreviousNode(result);
			} else {
				result = firstChild;
			}
		}
		return result;
	}

	/**
	 * Non-API. Public for testing only.
	 * 
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public static ISubModuleNode findPreviousNode(ISubModuleNode node) {
		ISubModuleNode result = null;
		INavigationNode<?> parent = node.getParent();
		if (parent != null) {
			List<?> children = parent.getChildren();
			for (Object child : children) {
				if (child == node) {
					break;
				}
				result = (ISubModuleNode) child;
			}
		}
		return result;
	}

	private Display getDisplay() {
		return ((Widget) getTree().getUIControl()).getDisplay();
	}

	private void runAsync(Runnable op) {
		getDisplay().asyncExec(op);
	}

	/**
	 * Selects the active sub-module of this module in the tree.
	 */
	private void selectActiveNode() {
		setSelectedNode(getNavigationNode());
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

	// helping classes
	//////////////////

	/**
	 * Activates the selected sub-module node.
	 */
	private final class DelegatingValue extends WritableValue {
		@Override
		public void doSetValue(Object value) {
			ISubModuleNode oldNode = (ISubModuleNode) getValue();
			ISubModuleNode newNode = (ISubModuleNode) value;
			super.doSetValue(value);

			ISubModuleNode toActivate = findNodeToActivate(oldNode, newNode);
			if (!toActivate.isActivated()) {
				toActivate.activate();
			}
		}
	}

	/**
	 * Updates the tree if a sub-module node is added or remove form parent
	 * sub-module node.
	 */
	private class SubModuleListener extends SubModuleNodeListener {
		@Override
		public void afterActivated(final ISubModuleNode source) {
			super.afterActivated(source);
			if (source.isSelectable()) {
				// TODO [ev] explain why
				runAsync(new Runnable() {
					public void run() {
						selectActiveNode();
					}
				});
			}
		}
	}

}
