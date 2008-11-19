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

package org.eclipse.riena.ui.ridgets.tree2;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.ridgets.util.beans.AbstractBean;

/**
 * A TreeNode wraps an arbitary object value to make it usable in the tree
 * maintained by an {@link ITreeRidget}.
 * 
 * @see ITreeNode
 */
public class TreeNode extends AbstractBean implements ITreeNode {

	private static void addToParent(ITreeNode parent, ITreeNode child) {
		Assert.isNotNull(child);
		List<ITreeNode> pChildren = parent.getChildren();
		pChildren.add(child);
		parent.setChildren(pChildren);
	}

	private final ITreeNode parent;

	private Object value;

	private List<ITreeNode> children;

	/**
	 * Creates a tree node instance.
	 * 
	 * @param parent
	 *            the parent node of this tree node. The parent cannot be
	 *            changed. The parent be null if this tree node is a 'root'
	 *            element.
	 * @param value
	 *            the value to wrap in this tree node (may be null)
	 * @see #setValue(Object)
	 */
	public TreeNode(ITreeNode parent, Object value) {
		this.parent = parent;
		this.value = value;
		if (parent != null) {
			addToParent(parent, this);
		}
	}

	/**
	 * Creates a 'root' tree node instance. 'Root' nodes do not have a parent
	 * node.
	 * 
	 * @param value
	 *            the value to wrap in this tree node (may be null)
	 * @see #setValue(Object)
	 */
	public TreeNode(Object value) {
		this(null, value);
	}

	public List<ITreeNode> getChildren() {
		return children != null ? new ArrayList<ITreeNode>(children) : new ArrayList<ITreeNode>();
	}

	public ITreeNode getParent() {
		return parent;
	}

	public Object getValue() {
		return value;
	}

	public void setChildren(List<ITreeNode> children) {
		if (this.children != children) {
			List<ITreeNode> newValue = (children != null) ? new ArrayList<ITreeNode>(children) : null;
			firePropertyChanged(PROPERTY_CHILDREN, this.children, this.children = newValue);
		}
	}

	public void setValue(Object value) {
		if (this.value != value) {
			Object oldValue = this.value;
			this.value = value;
			firePropertyChanged(PROPERTY_VALUE, oldValue, this.value);
		}
	}

	@Override
	public String toString() {
		return String.valueOf(getValue());
	}

}
