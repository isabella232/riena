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
package org.eclipse.riena.ui.ridgets.tree;

import org.eclipse.core.databinding.observable.IObservablesListener;

/**
 * 
 */
public class TreeNodeValueDiff extends TreeDiff {

	private final Object oldValue;
	private final Object newValue;

	/**
	 * 
	 */
	public TreeNodeValueDiff(ITreeNode node, Object oldValue, Object newValue) {

		super(node);
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	@Override
	public int[] getChildIndices() {
		return new int[0];
	}

	@Override
	public Object[] getChildren() {
		return new Object[0];
	}

	/*
	 * @see
	 * org.eclipse.riena.ui.ridgets.tree.TreeDiff#dispatch(org.eclipse.riena
	 * .ui.ridgets.tree.TreeModelEvent,
	 * org.eclipse.core.databinding.observable.IObservablesListener)
	 */
	@Override
	protected void dispatch(TreeModelEvent event, IObservablesListener listener) {
		((ITreeModelListener) listener).treeNodesChanged(event);
	}

	public Object getOldValue() {
		return oldValue;
	}

	public Object getNewValue() {
		return newValue;
	}
}
