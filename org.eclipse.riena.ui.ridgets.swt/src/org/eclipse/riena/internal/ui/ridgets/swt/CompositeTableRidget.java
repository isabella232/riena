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

import java.util.List;

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
import org.eclipse.riena.ui.ridgets.ICompositeTableRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRowRidget;
import org.eclipse.riena.ui.ridgets.databinding.IUnboundPropertyObservable;
import org.eclipse.riena.ui.ridgets.databinding.UnboundPropertyWritableList;
import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingPropertyLocator;
import org.eclipse.riena.ui.ridgets.uibinding.IControlRidgetMapper;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.swt.nebula.widgets.compositetable.CompositeTable;
import org.eclipse.swt.nebula.widgets.compositetable.IRowContentProvider;
import org.eclipse.swt.nebula.widgets.compositetable.IRowFocusListener;
import org.eclipse.swt.nebula.widgets.compositetable.RowConstructionListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
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

	private IObservableList rowObservables;
	private Class<? extends Object> rowBeanClass;
	private Class<? extends Object> rowRidgetClass;

	public CompositeTableRidget() {
		Assert.isLegal(!SelectionType.MULTI.equals(getSelectionType()));
		rowToRidgetMapper = new CTRowToRidgetMapper();
		selectionSynchronizer = new SelectionSynchronizer();
		getSingleSelectionObservable().addValueChangeListener(new IValueChangeListener() {
			public void handleValueChange(ValueChangeEvent event) {
				Object value = event.getObservableValue().getValue();
				getMultiSelectionObservable().clear();
				if (value != null) {
					getMultiSelectionObservable().add(value);
				}
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
			if (rowObservables != null) {
				control.setNumRowsInCollection(rowObservables.size());
				updateSelection(false);
			}
			control.addRowFocusListener(selectionSynchronizer);
			getSingleSelectionObservable().addValueChangeListener(selectionSynchronizer);
		}
	}

	@Override
	protected void unbindUIControl() {
		CompositeTable control = getUIControl();
		if (control != null) {
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
				control.setNumRowsInCollection(rowObservables.size());
				control.refreshAllRows();
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

	// helping methods
	//////////////////

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
			int index = indexOfOption(selection);
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

		private final IControlRidgetMapper<Object> mapper = new DefaultSwtControlRidgetMapper();

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
			if (rowObservables != null) {
				Object rowBean = rowObservables.get(index);
				Assert.isLegal(rowBeanClass.isAssignableFrom(rowBean.getClass()));
				IRowRidget rowRidget = (IRowRidget) row.getData("rowRidget"); //$NON-NLS-1$
				rowRidget.setData(rowBean);
				rowRidget.configureRidgets();
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
				int selectionIndex = indexOfOption(getSingleSelectionObservable().getValue());
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

}
