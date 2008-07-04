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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.riena.ui.ridgets.tree.IObservableTreeModel;
import org.eclipse.riena.ui.ridgets.tree.ITreeModel;
import org.eclipse.riena.ui.ridgets.tree.ITreeModelListener;
import org.eclipse.riena.ui.ridgets.tree.ITreeNode;
import org.eclipse.riena.ui.ridgets.tree.TreeModelEvent;

/**
 * TODO [ev] docs
 */
final class TreeModelContentProvider implements ITreeContentProvider, ITreeModelListener {

	private StructuredViewer viewer;

	// ITreeContentProvider methods
	// /////////////////////////////

	public Object[] getChildren(Object element) {
		ITreeNode node = (ITreeNode) element;
		int count = node.getChildCount();
		Object[] result = new Object[count];
		for (int i = 0; i < count; i++) {
			result[i] = node.getChildAt(i);
		}
		return result;
	}

	public Object getParent(Object element) {
		ITreeNode node = (ITreeNode) element;
		return node.getParent();
	}

	public boolean hasChildren(Object element) {
		ITreeNode node = (ITreeNode) element;
		return node.getChildCount() > 0;
	}

	public Object[] getElements(Object inputElement) {
		Assert.isLegal(inputElement instanceof ITreeModel);
		ITreeModel model = (ITreeModel) inputElement;
		if (model instanceof IObservableTreeModel) {
			model.removeTreeModelListener(this);
			model.addTreeModelListener(this);
		}
		return new Object[] { model.getRoot() };
	}

	public void dispose() {
		// unused
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		Assert.isLegal(viewer instanceof StructuredViewer);
		this.viewer = (StructuredViewer) viewer;
	}

	// ITreeModelListener methods
	// ///////////////////////////

	public void treeNodesChanged(TreeModelEvent e) {
		ITreeNode element = e.getNode();
		viewer.update(element, null);
	}

	public void treeNodesInserted(TreeModelEvent e) {
		ITreeNode element = e.getNode();
		viewer.refresh(element, true);
	}

	public void treeNodesRemoved(TreeModelEvent e) {
		ITreeNode element = e.getNode();
		viewer.refresh(element, true);
	}

	public void treeStructureChanged(TreeModelEvent e) {
		ITreeNode element = e.getNode();
		viewer.refresh(element, true);
	}

}
