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
package org.eclipse.riena.ui.ridgets.swt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.databinding.viewers.IViewerObservableList;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.AbstractListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionListener;

import org.eclipse.riena.core.util.ListenerList;
import org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTWidgetRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.AbstractSelectableIndexedRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.MarkerSupport;
import org.eclipse.riena.internal.ui.ridgets.swt.OutputAwareValidator;
import org.eclipse.riena.ui.common.ISortableByColumn;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IColumnFormatter;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;

/**
 * An abstract Ridget for lists that does not depend on the class
 * org.eclipse.swt.widgets.List. May be used for Ridgets for custom lists.
 */
public abstract class AbstractListRidget extends AbstractSelectableIndexedRidget implements ITableRidget {
	protected SelectionListener selectionTypeEnforcer;
	protected final MouseListener doubleClickForwarder;
	private ListenerList<IActionListener> doubleClickListeners;

	private DataBindingContext dbc;
	private Binding viewerSSB;
	/*
	 * Binds the viewer's multiple selection to the multiple selection
	 * observable. This binding hsa to be disposed when the ridget is set to
	 * output-only, to avoid updating the model. It has to be recreated when the
	 * ridget is set to not-output-only.
	 */
	private Binding viewerMSB;
	private Class<?> rowBeanClass;
	/*
	 * Data we received in bindToModel(...). May change without our doing.
	 */
	private IObservableList modelObservables;
	/*
	 * Data the viewer is bound to. It is updated from modelObservables on
	 * updateFromModel().
	 */
	private IObservableList viewerObservables;
	private String renderingMethod;

	private boolean isSortedAscending;
	private int sortedColumn;
	private ViewerComparator comparator;

	public AbstractListRidget() {
		doubleClickForwarder = new DoubleClickForwarder();
		isSortedAscending = true;
		sortedColumn = -1;
		getSingleSelectionObservable().addValueChangeListener(new IValueChangeListener() {
			public void handleValueChange(ValueChangeEvent event) {
				disableMandatoryMarkers(hasInput());
			}
		});
		getMultiSelectionObservable().addListChangeListener(new IListChangeListener() {
			public void handleListChange(ListChangeEvent event) {
				disableMandatoryMarkers(hasInput());
			}
		});
		addPropertyChangeListener(IRidget.PROPERTY_ENABLED, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				boolean isEnabled = ((Boolean) evt.getNewValue()).booleanValue();
				updateEnabled(isEnabled);
			}
		});
		addPropertyChangeListener(IMarkableRidget.PROPERTY_OUTPUT_ONLY, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (isOutputOnly()) {
					disposeMultipleSelectionBinding();
				} else {
					createMultipleSelectionBinding();
				}
			}
		});
	}

	protected abstract void updateEnabled(boolean isEnabled);

	/**
	 * @see org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTWidgetRidget#unbindUIControl()
	 */
	@Override
	protected void unbindUIControl() {
		if (dbc != null) {
			disposeSelectionBindings();
			dbc.dispose();
			dbc = null;
		}
	}

	@Override
	protected java.util.List<?> getRowObservables() {
		return viewerObservables;
	}

	public void addDoubleClickListener(IActionListener listener) {
		Assert.isNotNull(listener, "listener is null"); //$NON-NLS-1$
		if (doubleClickListeners == null) {
			doubleClickListeners = new ListenerList<IActionListener>(IActionListener.class);
		}
		doubleClickListeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Implementation note: the ListRidget ignoeres columnHeaders.
	 */
	public void bindToModel(IObservableList rowValues, Class<? extends Object> rowClass, String[] columnPropertyNames,
			String[] columnHeaders) {

		unbindUIControl();

		rowBeanClass = rowClass;
		modelObservables = rowValues;
		viewerObservables = null;
		renderingMethod = columnPropertyNames[0];

		bindUIControl();
	}

	public void bindToModel(Object listHolder, String listPropertyName, Class<? extends Object> rowClass,
			String[] columnPropertyNames, String[] columnHeaders) {
		IObservableList rowValues;
		if (AbstractSWTWidgetRidget.isBean(rowClass)) {
			rowValues = BeansObservables.observeList(listHolder, listPropertyName);
		} else {
			rowValues = PojoObservables.observeList(listHolder, listPropertyName);
		}
		bindToModel(rowValues, rowClass, columnPropertyNames, columnHeaders);
	}

	@Override
	public void updateFromModel() {
		super.updateFromModel();
		if (modelObservables != null) {
			List<Object> copy = new ArrayList<Object>(modelObservables);
			viewerObservables = new WritableList(copy, rowBeanClass);
		}
		if (hasViewer() && viewerObservables != null) {
			AbstractListViewer viewer = getViewer();
			viewer.getControl().setRedraw(false); // prevent flicker during
			// update
			StructuredSelection currentSelection = new StructuredSelection(getSelection());
			try {
				configureViewer(viewer);
			} finally {
				viewer.setSelection(currentSelection);
				viewer.getControl().setRedraw(true);
			}
		}
	}

	protected void configureViewer(AbstractListViewer viewer) {
		ObservableListContentProvider viewerCP = new ObservableListContentProvider();
		String[] propertyNames = new String[] { renderingMethod };
		IObservableMap[] attributeMap;
		if (AbstractSWTWidgetRidget.isBean(rowBeanClass)) {
			attributeMap = BeansObservables.observeMaps(viewerCP.getKnownElements(), rowBeanClass, propertyNames);
		} else {
			attributeMap = PojoObservables.observeMaps(viewerCP.getKnownElements(), rowBeanClass, propertyNames);
		}
		viewer.setLabelProvider(new ListLabelProvider(attributeMap));
		viewer.setContentProvider(viewerCP);
		viewer.setInput(viewerObservables);
	}

	public IObservableList getObservableList() {
		return viewerObservables;
	}

	public void removeDoubleClickListener(IActionListener listener) {
		if (doubleClickListeners != null) {
			doubleClickListeners.remove(listener);
		}
	}

	public void setComparator(int columnIndex, Comparator<Object> comparator) {
		Assert.isLegal(columnIndex == 0, "columnIndex out of bounds (must be 0)"); //$NON-NLS-1$
		if (comparator != null) {
			SortableComparator sortableComparator = new SortableComparator(this, comparator);
			this.comparator = new ViewerComparator(sortableComparator);
		} else {
			this.comparator = null;
		}
		updateComparator();
	}

	public int getSortedColumn() {
		return comparator != null && sortedColumn == 0 ? 0 : -1;
	}

	public boolean isColumnSortable(int columnIndex) {
		Assert.isLegal(columnIndex == 0, "columnIndex out of bounds (must be 0)"); //$NON-NLS-1$
		return comparator != null;
	}

	@Override
	public boolean isDisableMandatoryMarker() {
		return hasInput();
	}

	public boolean isSortedAscending() {
		return isSortedAscending;
	}

	/**
	 * This method is not supported by this ridget.
	 * 
	 * @throws UnsupportedOperationException
	 *             this is not supported by this ridget
	 */
	public final void setColumnSortable(int columnIndex, boolean sortable) {
		throw new UnsupportedOperationException();
	}

	public void setSortedAscending(boolean ascending) {
		if (ascending != isSortedAscending) {
			boolean oldSortedAscending = isSortedAscending;
			isSortedAscending = ascending;
			if (hasViewer()) {
				refreshViewer();
			}
			firePropertyChange(ISortableByColumn.PROPERTY_SORT_ASCENDING, oldSortedAscending, isSortedAscending);
		}
	}

	protected boolean hasViewer() {
		return getViewer() != null;
	}

	protected boolean hasViewerModel() {
		return viewerObservables != null;
	}

	protected void refreshViewer() {
		getViewer().refresh();
	}

	public final void setSortedColumn(int columnIndex) {
		String msg = "columnIndex out of range (-1 - 0): " + columnIndex; //$NON-NLS-1$
		Assert.isLegal(columnIndex >= -1 && columnIndex <= 0, msg);
		if (sortedColumn != columnIndex) {
			int oldSortedColumn = sortedColumn;
			sortedColumn = columnIndex;
			updateComparator();
			firePropertyChange(ISortableByColumn.PROPERTY_SORTED_COLUMN, oldSortedColumn, sortedColumn);
		}
	}

	@Override
	public int getSelectionIndex() {
		return getUIControl() == null ? -1 : getUIControlSelectionIndex();
	}

	protected abstract int getUIControlSelectionIndex();

	@Override
	public int[] getSelectionIndices() {
		return getUIControl() == null ? new int[0] : getUIControlSelectionIndices();
	}

	protected abstract int[] getUIControlSelectionIndices();

	@Override
	public int indexOfOption(Object option) {
		if (getUIControl() != null) {
			int optionCount = getUIControlItemCount();
			for (int i = 0; i < optionCount; i++) {
				if (getViewer().getElementAt(i).equals(option)) {
					return i;
				}
			}
		}
		return -1;
	}

	protected abstract int getUIControlItemCount();

	public final boolean hasMoveableColumns() {
		return false;
	}

	/**
	 * This method is not supported by this ridget.
	 * 
	 * @throws UnsupportedOperationException
	 *             this is not supported by this ridget
	 */
	public final void setMoveableColumns(boolean moveableColumns) {
		throw new UnsupportedOperationException("not implemented"); //$NON-NLS-1$
	}

	/**
	 * This method is not supported by this ridget.
	 * 
	 * @throws UnsupportedOperationException
	 *             this is not supported by this ridget
	 */
	public void setColumnFormatter(int columnIndex, IColumnFormatter formatter) {
		throw new UnsupportedOperationException();
	}

	// helping methods
	// ////////////////

	private void createMultipleSelectionBinding() {
		if (viewerMSB == null && dbc != null && hasViewer()) {
			StructuredSelection currentSelection = new StructuredSelection(getSelection());
			IViewerObservableList viewerSelections = ViewersObservables.observeMultiSelection(getViewer());
			viewerMSB = dbc.bindList(viewerSelections, getMultiSelectionObservable(), new UpdateListStrategy(
					UpdateListStrategy.POLICY_UPDATE), new UpdateListStrategy(UpdateListStrategy.POLICY_UPDATE));
			getViewer().setSelection(currentSelection);
		}
	}

	protected abstract AbstractListViewer getViewer();

	private void disposeMultipleSelectionBinding() {
		if (viewerMSB != null) { // implies dbc != null
			viewerMSB.dispose();
			dbc.removeBinding(viewerMSB);
			viewerMSB = null;
		}
	}

	protected void createSelectionBindings() {
		dbc = new DataBindingContext();
		// viewer to single selection binding
		IObservableValue viewerSelection = ViewersObservables.observeSingleSelection(getViewer());
		viewerSSB = dbc.bindValue(viewerSelection, getSingleSelectionObservable(), new UpdateValueStrategy(
				UpdateValueStrategy.POLICY_UPDATE).setAfterGetValidator(new OutputAwareValidator(this)),
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
		// viewer to multiple selection binding
		viewerMSB = null;
		if (!isOutputOnly()) {
			createMultipleSelectionBinding();
		}
	}

	protected void disposeSelectionBindings() {
		if (viewerSSB != null && !viewerSSB.isDisposed()) {
			viewerSSB.dispose();
		}
		disposeMultipleSelectionBinding();
	}

	private boolean hasInput() {
		return !getSelection().isEmpty();
	}

	protected void updateComparator() {
		if (hasViewer()) {
			if (sortedColumn == 0) {
				getViewer().setComparator(this.comparator);
			} else {
				getViewer().setComparator(null);
			}
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
				for (IActionListener listener : doubleClickListeners.getListeners()) {
					listener.callback();
				}
			}
		}
	}

	private final class ListLabelProvider extends ObservableMapLabelProvider {

		public ListLabelProvider(IObservableMap[] attributeMap) {
			super(attributeMap);
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if (MarkerSupport.HIDE_DISABLED_RIDGET_CONTENT && !isEnabled()) {
				return ""; //$NON-NLS-1$
			} else {
				return super.getColumnText(element, columnIndex);
			}
		}
	}

}
