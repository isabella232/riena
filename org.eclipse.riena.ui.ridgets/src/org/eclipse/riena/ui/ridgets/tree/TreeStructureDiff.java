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

import java.io.Serializable;

import org.eclipse.core.databinding.observable.IObservablesListener;

/**
 * 
 */
public class TreeStructureDiff extends TreeDiff {

	private final int[] childIndices;
	private final Serializable[] children;

	/**
	 * 
	 */
	public TreeStructureDiff(ITreeNode parent, int[] childIndices, Serializable[] children) {

		super(parent);
		this.childIndices = clone(childIndices);
		this.children = clone(children);
	}

	@Override
	public int[] getChildIndices() {
		return clone(childIndices);
	}

	@Override
	public Object[] getChildren() {
		return clone(children);
	}

	private int[] clone(int[] intArray) {
		if (intArray != null) {
			return intArray.clone();
		} else {
			return null;
		}
	}

	private Serializable[] clone(Serializable[] array) {
		if (array != null) {
			return array.clone();
		} else {
			return null;
		}
	}

	/*
	 * @see
	 * org.eclipse.riena.ui.ridgets.tree.TreeDiff#dispatch(org.eclipse.riena
	 * .ui.ridgets.tree.TreeModelEvent,
	 * org.eclipse.core.databinding.observable.IObservablesListener)
	 */
	@Override
	protected void dispatch(TreeModelEvent event, IObservablesListener listener) {
		((ITreeModelListener) listener).treeStructureChanged(event);
	}
}
