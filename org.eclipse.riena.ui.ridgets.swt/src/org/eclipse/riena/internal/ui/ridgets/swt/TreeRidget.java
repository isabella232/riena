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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.ridgets.tree.IObservableTreeModel;
import org.eclipse.riena.ui.ridgets.tree.ITreeNode;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * TODO [ev] docs
 */
public class TreeRidget extends AbstractSelectableRidget implements ITreeRidget {

	private final DoubleClickForwarder doubleClickForwarder;

	private TreeViewer viewer;
	private IObservableTreeModel treeModel;
	private Collection<IActionListener> doubleClickListeners;

	public TreeRidget() {
		doubleClickForwarder = new DoubleClickForwarder();
	}

	@Override
	public int getSelectionIndex() {
		int result = -1;
		if (getUIControl() != null) {
			int[] indexes = getSelectionIndices();
			if (indexes.length > 0) {
				Arrays.sort(indexes);
				result = indexes[0];
			}
		}
		return result;
	}

	@Override
	public int[] getSelectionIndices() {
		int[] result = null;
		Tree control = getUIControl();
		if (control != null) {
			TreeItem[] items = control.getSelection();
			result = new int[items.length];
			for (int i = 0; i < items.length; i++) {
				TreeItem item = items[i];
				result[i] = control.indexOf(item);
			}
		}
		return result != null ? result : new int[0];
	}

	@Override
	public int indexOfOption(Object option) {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	protected void bindUIControl() {
		Tree control = getUIControl();
		if (control != null && treeModel != null) {
			viewer = new TreeViewer(control);
			viewer.setLabelProvider(new LabelProvider());
			viewer.setContentProvider(new TreeModelContentProvider());
			viewer.setInput(treeModel);
			control.addMouseListener(doubleClickForwarder);
		}
	}

	@Override
	protected void checkUIControl(Object uiControl) {
		AbstractSWTRidget.assertType(uiControl, Tree.class);
	}

	@Override
	protected void unbindUIControl() {
		Tree control = getUIControl();
		if (control != null) {
			control.removeMouseListener(doubleClickForwarder);
		}
		viewer = null;
	}

	@Override
	public Tree getUIControl() {
		return (Tree) super.getUIControl();
	}

	public void addDoubleClickListener(IActionListener listener) {
		Assert.isNotNull(listener, "listener is null"); //$NON-NLS-1$
		if (doubleClickListeners == null) {
			doubleClickListeners = new ArrayList<IActionListener>();
		}
		doubleClickListeners.add(listener);
	}

	public void bindToModel(IObservableTreeModel observableTreeModel) {
		unbindUIControl();
		treeModel = observableTreeModel;
		bindUIControl();
	}

	public void collapse(ITreeNode node) {
		if (viewer != null) {
			Tree control = getUIControl();
			control.setRedraw(false);
			viewer.collapseToLevel(node, 1);
			control.setRedraw(true);
		}
	}

	public void collapseTree() {
		if (viewer != null) {
			Tree control = getUIControl();
			control.setRedraw(false);
			viewer.collapseAll();
			control.setRedraw(true);
		}
	}

	public void expand(ITreeNode node) {
		if (viewer != null) {
			Tree control = getUIControl();
			control.setRedraw(false);
			viewer.expandToLevel(node, 1);
			control.setRedraw(true);
		}
	}

	public void expandTree() {
		if (viewer != null) {
			Tree control = getUIControl();
			control.setRedraw(false);
			viewer.expandAll();
			control.setRedraw(true);
		}
	}

	public IObservableTreeModel getRidgetObservable() {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeDoubleClickListener(IActionListener listener) {
		if (doubleClickListeners != null) {
			doubleClickListeners.remove(listener);
		}
	}

	// helping classes
	// ////////////////

	/**
	 * Notifies doubleClickListeners when the bound widget is double clicked.
	 */
	private final class DoubleClickForwarder extends MouseAdapter {
		@Override
		public void mouseDoubleClick(MouseEvent e) {
			if (doubleClickListeners != null) {
				for (IActionListener listener : doubleClickListeners) {
					listener.callback();
				}
			}
		}
	}

}
