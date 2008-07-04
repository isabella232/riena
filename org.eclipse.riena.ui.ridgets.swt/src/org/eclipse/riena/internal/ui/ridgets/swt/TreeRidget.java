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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.internal.databinding.viewers.SelectionProviderMultipleSelectionObservableList;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.ridgets.tree.IObservableTreeModel;
import org.eclipse.riena.ui.ridgets.tree.ITreeNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Ridget for SWT {@link Tree} widgets.
 */
public class TreeRidget extends AbstractSelectableRidget implements ITreeRidget {

	private final SelectionListener selectionTypeEnforcer;
	private final DoubleClickForwarder doubleClickForwarder;
	/* a flat list of all "model" values from all nodes */
	private final WritableList observableValues;

	private DataBindingContext dbc;
	private TreeViewer viewer;
	private IObservableTreeModel observableTreeModel;
	private Collection<IActionListener> doubleClickListeners;

	public TreeRidget() {
		selectionTypeEnforcer = new SelectionTypeEnforcer();
		doubleClickForwarder = new DoubleClickForwarder();
		observableValues = new WritableList();
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
		int result = -1;
		Tree control = getUIControl();
		if (control != null) {
			TreeItem[] items = control.getItems();
			for (int i = 0; result == -1 && i < items.length; i++) {
				TreeItem item = items[i];
				// SWT impl detail; option is stored in item.getData();
				Object data = item.getData();
				if (data == option) {
					result = control.indexOf(item);
				}
			}
		}
		return result;
	}

	@Override
	protected void bindUIControl() {
		Tree control = getUIControl();
		if (control != null && observableTreeModel != null) {
			viewer = new TreeViewer(control);
			viewer.setLabelProvider(new TreeNodeLabelProvider(viewer));
			viewer.setContentProvider(new TreeModelContentProvider());
			viewer.setInput(observableTreeModel);
			if (observableTreeModel.getRoot() instanceof ITreeNode) {
				expand((ITreeNode) observableTreeModel.getRoot());
			}

			StructuredSelection currentSelection = new StructuredSelection(getSelection());

			dbc = new DataBindingContext();
			IObservableValue viewerSelection = ViewersObservables.observeSingleSelection(viewer);
			dbc.bindValue(viewerSelection, getSingleSelectionObservable(), new UpdateValueStrategy(
					UpdateValueStrategy.POLICY_UPDATE), new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
			IObservableList viewerSelections = new SelectionProviderMultipleSelectionObservableList(dbc
					.getValidationRealm(), viewer, Object.class);
			dbc.bindList(viewerSelections, getMultiSelectionObservable(), new UpdateListStrategy(
					UpdateListStrategy.POLICY_UPDATE), new UpdateListStrategy(UpdateListStrategy.POLICY_UPDATE));

			viewer.setSelection(currentSelection);

			control.addSelectionListener(selectionTypeEnforcer);
			control.addMouseListener(doubleClickForwarder);
		}
	}

	@Override
	protected void checkUIControl(Object uiControl) {
		AbstractSWTRidget.assertType(uiControl, Tree.class);
	}

	@Override
	protected void unbindUIControl() {
		if (dbc != null) {
			dbc.dispose();
			dbc = null;
		}
		Tree control = getUIControl();
		if (control != null) {
			control.removeSelectionListener(selectionTypeEnforcer);
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
		this.observableTreeModel = observableTreeModel;
		observableValues.clear();
		// TODO [ex] fill OVS?
		setRowObservables(observableValues);
		bindUIControl();
	}

	public void collapse(ITreeNode node) {
		if (viewer != null) {
			Tree control = getUIControl();
			control.setRedraw(false);
			viewer.collapseToLevel(node, 1);
			viewer.update(node, null); // update icon
			control.setRedraw(true);
		}
	}

	public void collapseTree() {
		if (viewer != null) {
			Tree control = getUIControl();
			control.setRedraw(false);
			viewer.collapseAll();
			viewer.refresh(); // update icons
			control.setRedraw(true);
		}
	}

	public void expand(ITreeNode node) {
		if (viewer != null) {
			Tree control = getUIControl();
			control.setRedraw(false);
			viewer.expandToLevel(node, 1);
			viewer.update(node, null); // update icon
			control.setRedraw(true);
		}
	}

	public void expandTree() {
		if (viewer != null) {
			Tree control = getUIControl();
			control.setRedraw(false);
			viewer.expandAll();
			viewer.refresh(); // update icons
			control.setRedraw(true);
		}
	}

	public IObservableTreeModel getRidgetObservable() {
		return observableTreeModel;
	}

	public void removeDoubleClickListener(IActionListener listener) {
		if (doubleClickListeners != null) {
			doubleClickListeners.remove(listener);
		}
	}

	// helping classes
	// ////////////////

	/**
	 * Disallows multiple selection is the selection type of the ridget is
	 * {@link ISelectableRidget.SelectionType#SINGLE}.
	 */
	private final class SelectionTypeEnforcer extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			if (SelectionType.SINGLE.equals(getSelectionType())) {
				Tree control = (Tree) e.widget;
				if (control.getSelectionCount() > 1) {
					// ignore this event
					e.doit = false;
					// set selection to most recent item
					TreeItem firstItem = findFirstItem(control);
					control.setSelection(firstItem);
					// fire event
					Event event = new Event();
					event.type = SWT.Selection;
					event.doit = true;
					control.notifyListeners(SWT.Selection, event);
				}
			}
		}

		private TreeItem findFirstItem(Tree control) {
			TreeItem result = null;
			int minIndex = Integer.MAX_VALUE;

			TreeItem[] selectedItems = control.getSelection();
			Assert.isLegal(selectedItems.length > 0);
			for (TreeItem item : selectedItems) {
				int index = control.indexOf(item);
				if (index < minIndex) {
					minIndex = index;
					result = item;
				}
			}
			return result;
		}
	}

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
