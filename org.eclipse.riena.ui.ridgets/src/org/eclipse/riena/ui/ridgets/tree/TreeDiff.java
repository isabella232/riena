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

import org.eclipse.core.databinding.observable.IObservablesListener;

/**
 * 
 */
public abstract class TreeDiff {

	public final ITreeNode node;

	/**
	 * 
	 */
	public TreeDiff(ITreeNode node) {
		this.node = node;
	}

	abstract protected void dispatch(TreeModelEvent event, IObservablesListener listener);

	public ITreeNode getNode() {

		return node;
	}

	public abstract int[] getChildIndices();

	public abstract Object[] getChildren();
}
