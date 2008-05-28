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

import java.io.Serializable;

import org.eclipse.core.databinding.observable.IObservablesListener;

/**
 * 
 */
public class TreeNodesRemovedDiff extends TreeDiff {

	public final int[] childIndices;
	public final Serializable[] children;

	/**
	 * 
	 */
	public TreeNodesRemovedDiff(ITreeNode parent, int[] childIndices, Serializable[] children) {

		super(parent);
		this.childIndices = childIndices;
		this.children = children;
	}

	public int[] getChildIndices() {
		return childIndices;
	}

	public Object[] getChildren() {
		return children;
	}

	/*
	 * @see org.eclipse.riena.ui.ridgets.tree.TreeDiff#dispatch(org.eclipse.riena.ui.ridgets.tree.TreeModelEvent,
	 *      org.eclipse.core.databinding.observable.IObservablesListener)
	 */
	@Override
	protected void dispatch(TreeModelEvent event, IObservablesListener listener) {
		((ITreeModelListener) listener).treeNodesRemoved(event);
	}
}
