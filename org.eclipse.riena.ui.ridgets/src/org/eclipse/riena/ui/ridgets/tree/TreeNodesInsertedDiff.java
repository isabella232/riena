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
public class TreeNodesInsertedDiff extends TreeDiff {

	private final int[] childIndices;
	private final Serializable[] children;

	/**
	 * 
	 */
	public TreeNodesInsertedDiff(ITreeNode parent, int[] childIndices, Serializable[] children) {

		super(parent);
		this.childIndices = childIndices.clone();
		this.children = children.clone();
	}

	public int[] getChildIndices() {
		return childIndices.clone();
	}

	public Object[] getChildren() {
		return children.clone();
	}

	/*
	 * @see
	 * org.eclipse.riena.ui.ridgets.tree.TreeDiff#dispatch(org.eclipse.riena
	 * .ui.ridgets.tree.TreeModelEvent,
	 * org.eclipse.core.databinding.observable.IObservablesListener)
	 */
	@Override
	protected void dispatch(TreeModelEvent event, IObservablesListener listener) {
		((ITreeModelListener) listener).treeNodesInserted(event);
	}
}
