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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.riena.core.util.ListenerList;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ISortableByColumn;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.databinding.IUnboundPropertyObservable;
import org.eclipse.riena.ui.ridgets.databinding.UnboundPropertyWritableList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;

/**
 * Ridget for SWT {@link List} widgets.
 */
public class ListRidget extends AbstractSelectableIndexedRidget implements ITableRidget {

	private final SelectionListener selectionTypeEnforcer;
	private final MouseListener doubleClickForwarder;
	private ListenerList<IActionListener> doubleClickListeners;

	private DataBindingContext dbc;
	private Binding viewerSSB;
	private Binding viewerMSB;

	private ListViewer viewer;
	private Class<?> rowBeanClass;
	private IObservableList rowObservables;
	private String renderingMethod;

	private boolean isSortedAscending;
	private int sortedColumn;
	private ViewerComparator comparator;

	public ListRidget() {
		selectionTypeEnforcer = new SelectionTypeEnforcer();
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
		addPropertyChangeListener(IMarkableRidget.PROPERTY_ENABLED, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				boolean isEnabled = ((Boolean) evt.getNewValue()).booleanValue();
				updateEnabled(isEnabled);
			}
		});
	}

	@Override
	protected void checkUIControl(Object uiControl) {
		AbstractSWTRidget.assertType(uiControl, List.class);
	}

	@Override
	protected void bindUIControl() {
		final List control = getUIControl();
		if (control != null && rowObservables != null) {
			viewer = new ListViewer(control);
			final ObservableListContentProvider viewerCP = new ObservableListContentProvider();
			IObservableMap[] attrMap = BeansObservables.observeMaps(viewerCP.getKnownElements(), rowBeanClass,
					new String[] { renderingMethod });
			viewer.setLabelProvider(new ObservableMapLabelProvider(attrMap) {
				@Override
				public String getColumnText(Object element, int columnIndex) {
					return isEnabled() ? super.getColumnText(element, columnIndex) : ""; //$NON-NLS-1$
				}
			});
			viewer.setContentProvider(viewerCP);
			viewer.setInput(rowObservables);

			updateComparator();
			updateEnabled(isEnabled());

			control.addSelectionListener(selectionTypeEnforcer);
			control.addMouseListener(doubleClickForwarder);
		}
	}

	@Override
	protected void unbindUIControl() {
		if (dbc != null) {
			disposeSelectionBindings();
			dbc.dispose();
			dbc = null;
		}
		List control = getUIControl();
		if (control != null) {
			control.removeSelectionListener(selectionTypeEnforcer);
			control.removeMouseListener(doubleClickForwarder);
		}
		viewer = null;
	}

	@Override
	protected java.util.List<?> getRowObservables() {
		return rowObservables;
	}

	@Override
	public List getUIControl() {
		return (List) super.getUIControl();
	}

	public void addDoubleClickListener(IActionListener listener) {
		Assert.isNotNull(listener, "listener is null"); //$NON-NLS-1$
		if (doubleClickListeners == null) {
			doubleClickListeners = new ListenerList<IActionListener>(IActionListener.class);
		}
		doubleClickListeners.add(listener);
	}

	public void bindToModel(IObservableList listObservableValue, Class<? extends Object> rowBeanClass,
			String[] columnPropertyNames, String[] columnHeaders) {

		unbindUIControl();

		this.rowBeanClass = rowBeanClass;
		rowObservables = listObservableValue;
		renderingMethod = columnPropertyNames[0];

		bindUIControl();
	}

	public void bindToModel(Object listBean, String listPropertyName, Class<? extends Object> rowBeanClass,
			String[] columnPropertyNames, String[] columnHeaders) {
		IObservableList listObservableValue = new UnboundPropertyWritableList(listBean, listPropertyName);
		bindToModel(listObservableValue, rowBeanClass, columnPropertyNames, columnHeaders);
	}

	@Override
	public void updateFromModel() {
		super.updateFromModel();
		if (viewer != null) {
			viewer.getControl().setRedraw(false); // prevent flicker during
			// update
			StructuredSelection currentSelection = new StructuredSelection(getSelection());
			try {
				if (rowObservables instanceof IUnboundPropertyObservable) {
					((UnboundPropertyWritableList) rowObservables).updateFromBean();
				}
				viewer.refresh(true);
			} finally {
				viewer.setSelection(currentSelection);
				viewer.getControl().setRedraw(true);
			}
		}
	}

	public IObservableList getObservableList() {
		return rowObservables;
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
			if (viewer != null) {
				viewer.refresh();
			}
			firePropertyChange(ISortableByColumn.PROPERTY_SORT_ASCENDING, oldSortedAscending, isSortedAscending);
		}
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
		List control = getUIControl();
		return control == null ? -1 : control.getSelectionIndex();
	}

	@Override
	public int[] getSelectionIndices() {
		List control = getUIControl();
		return control == null ? new int[0] : control.getSelectionIndices();
	}

	@Override
	public int indexOfOption(Object option) {
		List control = getUIControl();
		if (control != null) {
			int optionCount = control.getItemCount();
			for (int i = 0; i < optionCount; i++) {
				if (viewer.getElementAt(i).equals(option)) {
					return i;
				}
			}
		}
		return -1;
	}

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

	// helping methods
	// ////////////////

	private void createSelectionBindings() {
		StructuredSelection currentSelection = new StructuredSelection(getSelection());

		dbc = new DataBindingContext();
		// viewer to single selection binding
		IObservableValue viewerSelection = ViewersObservables.observeSingleSelection(viewer);
		viewerSSB = dbc.bindValue(viewerSelection, getSingleSelectionObservable(), new UpdateValueStrategy(
				UpdateValueStrategy.POLICY_UPDATE).setAfterGetValidator(new OutputAwareValidator(this)),
				new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
		// viewer to to multi selection binding
		IObservableList viewerSelections = new OutputAwareMultipleSelectionObservableList(dbc.getValidationRealm(),
				viewer, Object.class, this);
		viewerMSB = dbc.bindList(viewerSelections, getMultiSelectionObservable(), new UpdateListStrategy(
				UpdateListStrategy.POLICY_UPDATE), new UpdateListStrategy(UpdateListStrategy.POLICY_UPDATE));

		viewer.setSelection(currentSelection);
	}

	private void disposeSelectionBindings() {
		if (viewerSSB != null && !viewerSSB.isDisposed()) {
			viewerSSB.dispose();
		}
		if (viewerMSB != null && !viewerMSB.isDisposed()) {
			viewerMSB.dispose();
		}
	}

	private boolean hasInput() {
		return !getSelection().isEmpty();
	}

	private void updateComparator() {
		if (viewer != null) {
			if (sortedColumn == 0) {
				viewer.setComparator(this.comparator);
			} else {
				viewer.setComparator(null);
			}
		}
	}

	private void updateEnabled(boolean isEnabled) {
		final String savedBackgroundKey = "oldbg"; //$NON-NLS-1$
		if (isEnabled) {
			if (viewer != null && rowObservables != null) {
				viewer.refresh();
				createSelectionBindings();
				List list = viewer.getList();
				list.setBackground((Color) list.getData(savedBackgroundKey));
				list.setData(savedBackgroundKey, null);
			}
		} else {
			disposeSelectionBindings();
			if (viewer != null) {
				viewer.refresh();
				List list = viewer.getList();
				list.deselectAll();
				list.setData(savedBackgroundKey, list.getBackground());
				list.setBackground(list.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
			}
		}
	}

	// helping classes
	// ////////////////

	/**
	 * Enforces selection in the control:
	 * <ul>
	 * <li>disallows selection changes when the ridget is "output only"</li>
	 * <li>disallows multiple selection is the selection type of the ridget is
	 * {@link ISelectableRidget.SelectionType#SINGLE}</li>
	 * </ul>
	 */
	private final class SelectionTypeEnforcer extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			List control = (List) e.widget;
			if (isOutputOnly()) {
				revertSelection(control);
			} else if (SelectionType.SINGLE.equals(getSelectionType())) {
				if (control.getSelectionCount() > 1) {
					// ignore this event
					e.doit = false;
					selectFirstItem(control);
				}
			}
		}

		private void selectFirstItem(List control) {
			// set selection to most recent item
			control.setSelection(control.getSelectionIndex());
			// fire event
			Event event = new Event();
			event.type = SWT.Selection;
			event.doit = true;
			control.notifyListeners(SWT.Selection, event);
		}

		private void revertSelection(List control) {
			control.setRedraw(false);
			try {
				// undo user selection when "output only"
				viewer.setSelection(new StructuredSelection(getSelection()));
			} finally {
				// redraw control to remove "cheese" that is caused when
				// using the keyboard to select the next row
				control.setRedraw(true);
			}
		}
	}

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
}
