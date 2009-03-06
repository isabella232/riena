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
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.logging.ConsoleLogger;
import org.eclipse.riena.core.util.ReflectionFailure;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.ui.common.IComplexComponent;
import org.eclipse.riena.ui.common.ISortableByColumn;
import org.eclipse.riena.ui.ridgets.ICompositeTableRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRowRidget;
import org.eclipse.riena.ui.ridgets.databinding.IUnboundPropertyObservable;
import org.eclipse.riena.ui.ridgets.databinding.UnboundPropertyWritableList;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingPropertyLocator;
import org.eclipse.riena.ui.ridgets.uibinding.IControlRidgetMapper;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.nebula.widgets.compositetable.AbstractNativeHeader;
import org.eclipse.swt.nebula.widgets.compositetable.CompositeTable;
import org.eclipse.swt.nebula.widgets.compositetable.IRowContentProvider;
import org.eclipse.swt.nebula.widgets.compositetable.IRowFocusListener;
import org.eclipse.swt.nebula.widgets.compositetable.RowConstructionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.osgi.service.log.LogService;

/**
 * A ridget for nebula's {@link CompositeTable} - this is a table with an
 * instance of an arbitrary composite in each row.
 */
public class CompositeTableRidget extends AbstractSelectableIndexedRidget implements ICompositeTableRidget {

	private final static Logger LOGGER;

	static {
		String loggerName = CompositeTableRidget.class.getName();
		if (Activator.getDefault() != null) {
			LOGGER = Activator.getDefault().getLogger(loggerName);
		} else {
			LOGGER = new ConsoleLogger(loggerName);
		}
	}

	private final CTRowToRidgetMapper rowToRidgetMapper;
	private final SelectionSynchronizer selectionSynchronizer;
	private final Map<Integer, Boolean> sortableColumnsMap;
	private final Map<Integer, Comparator<Object>> comparatorMap;
	private final SelectionListener sortListener;

	/**
	 * An observable list supplied via bindToModel(...). May be null.
	 */
	private IObservableList rowObservables;
	/**
	 * A copy of rowObservables as an array. This is used to feed the rows /
	 * rowRidgets in the table. The array will be re-ordered if sorting is
	 * applied. Never null.
	 */
	private Object[] rowValues;
	/**
	 * A copy of the <b>unsorted</b> rowValues. It provides the ability to
	 * restore the unnsorted state of rowValues. This array is null if rowValues
	 * is unsorted.
	 */
	private Object[] rowValuesUnsorted;
	private Class<? extends Object> rowBeanClass;
	private Class<? extends Object> rowRidgetClass;

	private boolean isSortedAscending;
	private int sortedColumn;

	public CompositeTableRidget() {
		Assert.isLegal(!SelectionType.MULTI.equals(getSelectionType()));
		rowToRidgetMapper = new CTRowToRidgetMapper();
		selectionSynchronizer = new SelectionSynchronizer();
		sortableColumnsMap = new HashMap<Integer, Boolean>();
		comparatorMap = new HashMap<Integer, Comparator<Object>>();
		sortListener = new ColumnSortListener();
		rowValues = new Object[0];
		isSortedAscending = true;
		sortedColumn = -1;
		getSingleSelectionObservable().addValueChangeListener(new IValueChangeListener() {
			public void handleValueChange(ValueChangeEvent event) {
				Object value = event.getObservableValue().getValue();
				getMultiSelectionObservable().clear();
				if (value != null) {
					getMultiSelectionObservable().add(value);
				}
			}
		});
		addPropertyChangeListener(IMarkableRidget.PROPERTY_ENABLED, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				refreshRowStyles();
			}
		});
		addPropertyChangeListener(IMarkableRidget.PROPERTY_OUTPUT_ONLY, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				refreshRowStyles();
			}
		});
	}

	@Override
	protected void checkUIControl(Object uiControl) {
		AbstractSWTRidget.assertType(uiControl, CompositeTable.class);
	}

	@Override
	protected void bindUIControl() {
		CompositeTable control = getUIControl();
		if (control != null) {
			control.addRowConstructionListener(rowToRidgetMapper);
			control.addRowContentProvider(rowToRidgetMapper);
			updateRows(control);
			updateSelection(false);
			control.addRowFocusListener(selectionSynchronizer);
			getSingleSelectionObservable().addValueChangeListener(selectionSynchronizer);
			if (getHeaderTable() != null) {
				for (TableColumn column : getHeaderTable().getColumns()) {
					column.addSelectionListener(sortListener);
				}
			}
			refreshRowStyles();
		}
	}

	@Override
	protected void unbindUIControl() {
		CompositeTable control = getUIControl();
		if (control != null) {
			if (getHeaderTable() != null) {
				for (TableColumn column : getHeaderTable().getColumns()) {
					column.removeSelectionListener(sortListener);
				}
			}
			getSingleSelectionObservable().removeValueChangeListener(selectionSynchronizer);
			control.removeRowFocusListener(selectionSynchronizer);
			control.removeRowContentProvider(rowToRidgetMapper);
			control.removeRowConstructionListener(rowToRidgetMapper);
		}
	}

	@Override
	protected List<?> getRowObservables() {
		return rowObservables;
	}

	public void bindToModel(IObservableList rowBeansObservables, Class<? extends Object> rowBeanClass,
			Class<? extends Object> rowRidgetClass) {
		Assert.isLegal(IRowRidget.class.isAssignableFrom(rowRidgetClass));

		unbindUIControl();

		rowObservables = rowBeansObservables;
		this.rowBeanClass = rowBeanClass;
		this.rowRidgetClass = rowRidgetClass;

		bindUIControl();
	}

	@Override
	public void updateFromModel() {
		super.updateFromModel();
		CompositeTable control = getUIControl();
		if (control != null && rowObservables != null) {
			control.setRedraw(false);
			try {
				if (rowObservables instanceof IUnboundPropertyObservable) {
					((UnboundPropertyWritableList) rowObservables).updateFromBean();
				}
				updateRows(control);
				updateSelection(true);
			} finally {
				control.setRedraw(true);
			}
		}
	}

	@Override
	public int getSelectionIndex() {
		Object selection = getSingleSelectionObservable().getValue();
		return indexOfOption(selection);
	}

	@Override
	public int[] getSelectionIndices() {
		int index = getSelectionIndex();
		return index == -1 ? new int[0] : new int[] { index };
	}

	@Override
	public CompositeTable getUIControl() {
		return (CompositeTable) super.getUIControl();
	}

	@Override
	public int indexOfOption(Object option) {
		int result = -1;
		if (option != null && rowObservables != null) {
			result = rowObservables.indexOf(option);
		}
		return result;
	}

	@Override
	public boolean isDisableMandatoryMarker() {
		return true;
	}

	@Override
	public void setSelectionType(SelectionType selectionType) {
		if (SelectionType.MULTI.equals(selectionType)) {
			throw new IllegalArgumentException("SelectionType.MULTI is not supported by the UI-control"); //$NON-NLS-1$
		}
		super.setSelectionType(selectionType);
	}

	@Override
	public void setSelection(List<?> newSelection) {
		readAndDispatch();
		super.setSelection(newSelection);
		readAndDispatch();
	}

	public void setComparator(int columnIndex, Comparator<Object> comparator) {
		checkColumnRange(columnIndex);
		Integer key = Integer.valueOf(columnIndex);
		if (comparator != null) {
			comparatorMap.put(key, comparator);
		} else {
			comparatorMap.remove(key);
		}
		if (columnIndex == sortedColumn) {
			applyComparator();
		}
	}

	// methods of ISortableByColumn
	///////////////////////////////

	/**
	 * {@inheritDoc}
	 * <p>
	 * Implementation note: there is no API to query the sorted column on
	 * CompositeTable. The returned value is computed with information held in
	 * the ridget.
	 */
	public int getSortedColumn() {
		int result = -1;
		Integer key = Integer.valueOf(sortedColumn);
		if (getUIControl() != null && comparatorMap.containsKey(key)) {
			result = sortedColumn;
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Implementation notes: to have columns that are sortable by the user, the
	 * CompositeTable bound to this ridget must have an
	 * {@link AbstractNativeHeader}. In any other case this method will return
	 * false.
	 */
	public boolean isColumnSortable(int columnIndex) {
		checkColumnRange(columnIndex);
		boolean result = false;
		if (getHeaderTable() != null) {
			Integer key = Integer.valueOf(columnIndex);
			Boolean sortable = sortableColumnsMap.get(columnIndex);
			if (sortable == null || Boolean.TRUE.equals(sortable)) {
				result = comparatorMap.get(key) != null;
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Implementation note: there is no API to query the sorted column on
	 * CompositeTable. The returned value is computed with information held in
	 * the ridget.
	 */
	public boolean isSortedAscending() {
		boolean result = false;
		if (getSortedColumn() != -1) {
			result = isSortedAscending;
		}
		return result;
	}

	public void setColumnSortable(int columnIndex, boolean sortable) {
		checkColumnRange(columnIndex);
		Integer key = Integer.valueOf(columnIndex);
		Boolean newValue = Boolean.valueOf(sortable);
		Boolean oldValue = sortableColumnsMap.put(key, newValue);
		if (oldValue == null) {
			oldValue = Boolean.TRUE;
		}
		if (!newValue.equals(oldValue)) {
			firePropertyChange(ISortableByColumn.PROPERTY_COLUMN_SORTABILITY, null, columnIndex);
		}
	}

	public void setSortedAscending(boolean ascending) {
		if (isSortedAscending != ascending) {
			boolean oldSortedAscending = isSortedAscending;
			isSortedAscending = ascending;
			applyComparator();
			firePropertyChange(ISortableByColumn.PROPERTY_SORT_ASCENDING, oldSortedAscending, isSortedAscending);
		}
	}

	public void setSortedColumn(int columnIndex) {
		if (columnIndex != -1) {
			checkColumnRange(columnIndex);
		}
		if (sortedColumn != columnIndex) {
			int oldSortedColumn = sortedColumn;
			sortedColumn = columnIndex;
			applyComparator();
			firePropertyChange(ISortableByColumn.PROPERTY_SORTED_COLUMN, oldSortedColumn, sortedColumn);
		}
	}

	// helping methods
	//////////////////

	private void applyComparator() {
		CompositeTable control = getUIControl();
		if (control != null) {
			Comparator<Object> comparator = null;
			if (sortedColumn != -1) {
				Integer key = Integer.valueOf(sortedColumn);
				comparator = comparatorMap.get(key);
			}
			if (comparator != null) {
				setSortColumnOnHeader(sortedColumn);
				int direction = isSortedAscending ? SWT.UP : SWT.DOWN;
				setSortDirectionOnHeader(direction);
				if (rowValuesUnsorted == null) {
					rowValuesUnsorted = new Object[rowValues.length];
					System.arraycopy(rowValues, 0, rowValuesUnsorted, 0, rowValuesUnsorted.length);
				}
				SortableComparator sortableComparator = new SortableComparator(this, comparator);
				Arrays.sort(rowValues, sortableComparator);
				control.refreshAllRows();
			} else {
				setSortColumnOnHeader(sortedColumn);
				setSortDirectionOnHeader(SWT.NONE);
				if (rowValuesUnsorted != null) {
					Assert.isLegal(rowValuesUnsorted.length == rowValues.length);
					System.arraycopy(rowValuesUnsorted, 0, rowValues, 0, rowValues.length);
					rowValuesUnsorted = null;
					control.refreshAllRows();
				}
			}
		}
	}

	private void checkColumnRange(int columnIndex) {
		Assert.isLegal(-1 < columnIndex, "columnIndex out of range: " + columnIndex); //$NON-NLS-1$
	}

	private Table getHeaderTable() {
		Table result = null;
		CompositeTable control = getUIControl();
		if (control != null) {
			/*
			 * CompositeTable does not have API for gettting the 'header' table.
			 * We rely on implementation details: start with TableComposite ->
			 * child[2] is the InternalComposite (with child 0+1 being the
			 * header and row prototypes) -> child[0] is the content composite
			 * (with child 1+2 being composites for the scroll bars) -> child[0]
			 * is the header composite - hopefully AbstractNativeHeader ->
			 * child[0] is the Table which is used as the 'native header'
			 * widget.
			 */
			Control[] tcChildren = control.getChildren();
			if (tcChildren.length > 2 && tcChildren[2] instanceof Composite) {
				Composite intComposite = (Composite) tcChildren[2];
				Control[] intChildren = intComposite.getChildren();
				if (intChildren.length > 0 && intChildren[0] instanceof Composite) {
					Composite contComposite = (Composite) intChildren[0];
					Control[] contChildren = contComposite.getChildren();
					if (contChildren.length > 0 && contChildren[0] instanceof Composite) {
						Composite tableComp = (Composite) contChildren[0];
						Control[] tableChildren = tableComp.getChildren();
						if (tableChildren.length > 0 && tableChildren[0] instanceof AbstractNativeHeader) {
							AbstractNativeHeader header = (AbstractNativeHeader) tableChildren[0];
							Control[] headerChildren = header.getChildren();
							if (headerChildren.length > 0 && headerChildren[0] instanceof Table) {
								result = (Table) headerChildren[0];
							}
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * CompositeTable.setSelection(x,y) is asynchronous. This means that: - we
	 * have to fully process the event-queue before, to avoid pending events
	 * adding selection changes after our call, via asyncexec(op)). - we have to
	 * fully process the event-queue afterwards, to make sure the selection is
	 * appliied to the widget.
	 * <p>
	 * Typically this method will be called before AND after
	 * ct.setSelection(x,y);
	 */
	private void readAndDispatch() {
		CompositeTable control = getUIControl();
		if (control != null) {
			Display display = control.getDisplay();
			while (display.readAndDispatch()) {
				// keep working
			}
		}
	}

	private void refreshRowStyle(Control rowControl, boolean isEnabled, boolean isOutput, Color bgColor) {
		rowControl.setBackground(bgColor);
		rowControl.setVisible(isEnabled);
		rowControl.setEnabled(isEnabled && !isOutput);
	}

	private void refreshRowStyles() {
		CompositeTable table = getUIControl();
		boolean enabled = isEnabled();
		boolean output = isOutputOnly();
		if (table != null) {
			// update each row
			for (Control rowControl : table.getRowControls()) {
				Color bgColor = table.getBackground();
				refreshRowStyle(rowControl, enabled, output, bgColor);
			}
			// Updates the color of contentPane (=composite that holds the rows). 
			//
			// This is a clever trick: MarkerSupport manipulates
			// the bgColor of the CompositeTable, we manipualte the bgColor
			// of the internal contentPane - this way we don't get in each
			// other's way. By using the color for table when enabled we make
			// sure that we apply the 'right' color from MarkerSupport. 
			Control[] children = table.getChildren();
			Control contentPane = children[children.length - 1];
			Display display = table.getDisplay();
			Color cpBgColor = enabled ? table.getBackground() : display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
			contentPane.setBackground(cpBgColor);
		}
	}

	private int rowIndexOfOption(Object element) {
		int result = -1;
		for (int i = 0; result == -1 && i < rowValues.length; i++) {
			if (rowValues[i] == element) {
				result = i;
			}
		}
		return result;
	}

	private void setSortDirectionOnHeader(int direction) {
		Table headerTable = getHeaderTable();
		if (headerTable != null) {
			headerTable.setSortDirection(direction);
			headerTable.redraw();
		}
	}

	private void setSortColumnOnHeader(int columnIndex) {
		Table headerTable = getHeaderTable();
		if (headerTable != null) {
			if (columnIndex == -1) {
				headerTable.setSortColumn(null);
			} else {
				TableColumn column = headerTable.getColumn(columnIndex);
				headerTable.setSortColumn(column);
				headerTable.redraw();
			}
		}
	}

	private void updateRows(CompositeTable control) {
		if (rowObservables != null) {
			rowValues = rowObservables.toArray();
		} else {
			rowValues = new Object[0];
		}
		rowValuesUnsorted = null;
		control.setNumRowsInCollection(rowValues.length);
		applyComparator();
	}

	/**
	 * Re-applies ridget selection to control (if selection exists), otherwise
	 * clears ridget selection
	 * 
	 * @param canClear
	 *            true, if it's ok to clear the selection
	 */
	private void updateSelection(boolean canClear) {
		CompositeTable control = getUIControl();
		if (control != null) {
			Object selection = getSingleSelectionObservable().getValue();
			int index = rowIndexOfOption(selection);
			if (index > -1) {
				int row = index - control.getTopRow();
				readAndDispatch();
				control.setSelection(0, row);
				readAndDispatch();
			} else {
				if (selection != null && canClear) {
					// if the selection has been deleted, selected another row
					// because otherwise composite table still things the
					// deleted row is selected
					if (rowObservables != null && rowObservables.size() > 0) {
						setSelection(0);
					} else {
						clearSelection();
					}
				}
			}
		}
	}

	// helping classes
	//////////////////

	/**
	 * Binds and configures Ridgets to a Row control.
	 */
	private final class CTRowToRidgetMapper extends RowConstructionListener implements IRowContentProvider {

		private final IControlRidgetMapper<Object> mapper = SwtControlRidgetMapper.getInstance();

		@Override
		public void headerConstructed(Control newHeader) {
			// unused
		}

		@Override
		public void rowConstructed(Control newRow) {
			IComplexComponent rowControl = (IComplexComponent) newRow;
			IRowRidget rowRidget = (IRowRidget) ReflectionUtils.newInstance(rowRidgetClass, (Object[]) null);
			IBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
			for (Object control : rowControl.getUIControls()) {
				String bindingProperty = locator.locateBindingProperty(control);
				if (bindingProperty != null) {
					IRidget ridget = createRidget(control);
					ridget.setUIControl(control);
					rowRidget.addRidget(bindingProperty, ridget);
				} else {
					String message = String.format("widget without binding property: %s : %s", rowControl.getClass(), //$NON-NLS-1$
							control);
					LOGGER.log(LogService.LOG_WARNING, message);
				}
			}
			if (Activator.getDefault() != null) {
				Wire.instance(rowRidget).andStart(Activator.getDefault().getContext());
			}
			newRow.setData("rowRidget", rowRidget); //$NON-NLS-1$
		}

		public void refresh(CompositeTable table, int index, Control row) {
			if (index < rowValues.length) {
				Object rowBean = rowValues[index];
				Assert.isLegal(rowBeanClass.isAssignableFrom(rowBean.getClass()));
				IRowRidget rowRidget = (IRowRidget) row.getData("rowRidget"); //$NON-NLS-1$
				rowRidget.setData(rowBean);
				rowRidget.configureRidgets();
				refreshRowStyle(row, isEnabled(), isOutputOnly(), table.getBackground());
			}
		}

		private IRidget createRidget(Object control) throws ReflectionFailure {
			Class<? extends IRidget> ridgetClass = mapper.getRidgetClass(control);
			return ReflectionUtils.newInstance(ridgetClass);
		}
	}

	/**
	 * Updates the selection in a CompositeTable control, when the value of the
	 * (single selection) observable changes and vice versa.
	 */
	private final class SelectionSynchronizer implements IRowFocusListener, IValueChangeListener {

		private boolean isArriving = false;
		private boolean isSelecting = false;

		public void arrive(CompositeTable sender, int currentObjectOffset, Control newRow) {
			if (isSelecting) {
				return;
			}
			isArriving = true;
			try {
				int selectionIndex = rowIndexOfOption(getSingleSelectionObservable().getValue());
				if (currentObjectOffset != selectionIndex) {
					setSelection(currentObjectOffset);
				}
			} finally {
				isArriving = false;
			}
		}

		public void depart(CompositeTable sender, int currentObjectOffset, Control row) {
			// unused
		}

		public boolean requestRowChange(CompositeTable sender, int currentObjectOffset, Control row) {
			return true;
		}

		public void handleValueChange(ValueChangeEvent event) {
			if (isArriving) {
				return;
			}
			isSelecting = true;
			try {
				updateSelection(false);
			} finally {
				isSelecting = false;
			}
		}
	}

	/**
	 * Selection listener for table headers that changes the sort order of a
	 * column according to the information stored in the ridget.
	 */
	private final class ColumnSortListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			TableColumn column = (TableColumn) e.widget;
			int columnIndex = column.getParent().indexOf(column);
			int direction = column.getParent().getSortDirection();
			if (columnIndex == sortedColumn) {
				if (direction == SWT.UP) {
					setSortedAscending(false);
				} else if (direction == SWT.DOWN) {
					setSortedColumn(-1);
				}
			} else if (isColumnSortable(columnIndex)) {
				setSortedColumn(columnIndex);
				if (direction == SWT.NONE) {
					setSortedAscending(true);
				}
			}
			column.getParent().showSelection();
		}
	}

}
