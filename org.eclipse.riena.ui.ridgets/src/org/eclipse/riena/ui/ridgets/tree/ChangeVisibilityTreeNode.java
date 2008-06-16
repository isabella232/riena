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
package org.eclipse.riena.ui.ridgets.tree;

import java.util.Enumeration;
import java.util.Vector;

import org.eclipse.core.runtime.Assert;

/**
 * Nodes of class <code>ChangeVisibilityTreeNode</code> can change their
 * visibility (visible/invisible).
 */
public class ChangeVisibilityTreeNode extends ToolTipTreeNode implements IVisibleTreeNode {

	private boolean visible;

	/**
	 * Constructor. Creates a tree node with no parent, no children and
	 * initializes it with the specified user object.
	 * 
	 * @param userObject -
	 *            an Object provided by the user that constitutes the node's
	 *            data.
	 */
	public ChangeVisibilityTreeNode(Object userObject) {

		super(userObject);

		setVisible(true);
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.IVisibleTreeNode#getVisibleChildAt(int)
	 */
	public IVisibleTreeNode getVisibleChildAt(int childIndex) {

		Assert.isNotNull(children, "node has no children");

		return (IVisibleTreeNode) getVisibleChildren().elementAt(childIndex);
	}

	private Vector getVisibleChildren() {

		Vector<Object> visibleChildren = new Vector<Object>();
		Enumeration enumeration = children.elements();

		while (enumeration.hasMoreElements()) {
			Object element = enumeration.nextElement();
			if (element instanceof IVisibleTreeNode) {
				if (((IVisibleTreeNode) element).isVisible()) {
					visibleChildren.add(element);
				}
			} else {
				visibleChildren.add(element);
			}
		}

		return visibleChildren;
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.IVisibleTreeNode#getVisibleChildCount()
	 */
	public int getVisibleChildCount() {

		if (children == null) {
			return 0;
		} else {
			return getVisibleChildren().size();
		}
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.IVisibleTreeNode#
	 *      getVisibleIndex(de.compeople.spirit.core.client.uibinding.adapter.tree.IVisibleTreeNode)
	 */
	public int getVisibleIndex(IVisibleTreeNode node) {

		Assert.isNotNull(node, "missing node argument");

		return getVisibleChildren().indexOf(node);
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.IVisibleTreeNode#isVisibleLeaf()
	 */
	public boolean isVisibleLeaf() {

		return getVisibleChildCount() == 0;
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.IVisibleTreeNode#isVisible()
	 */
	public boolean isVisible() {

		return visible;
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.IVisibleTreeNode#setVisible(boolean)
	 */
	public void setVisible(boolean visible) {

		this.visible = visible;
	}
}