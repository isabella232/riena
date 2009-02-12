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
import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.logging.ConsoleLogger;
import org.eclipse.riena.core.util.ReflectionFailure;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.ui.common.IComplexComponent;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRowRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingPropertyLocator;
import org.eclipse.riena.ui.ridgets.uibinding.IControlRidgetMapper;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.nebula.widgets.compositetable.CompositeTable;
import org.eclipse.swt.nebula.widgets.compositetable.IRowContentProvider;
import org.eclipse.swt.nebula.widgets.compositetable.IRowFocusListener;
import org.eclipse.swt.nebula.widgets.compositetable.RowConstructionListener;
import org.eclipse.swt.widgets.Control;
import org.osgi.service.log.LogService;

/**
 * A ridget for nebula's {@link CompositeTable} - this is a table with an
 * instance of an arbitrary composite in each row.
 */
// TODO [ev] tests
public class CompositeTableRidget extends AbstractSelectableIndexedRidget {

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
		rowToRidgetMapper = new CTRowToRidgetMapper();
		selectionSynchronizer = new SelectionSynchronizer();
	}

	@Override
	protected void checkUIControl(Object uiControl) {
		AbstractSWTRidget.assertType(uiControl, CompositeTable.class);
	}

	@Override
	protected void bindUIControl() {
		CompositeTable control = getUIControl();
		if (control != null) {
			if (rowObservables != null) {
				control.setNumRowsInCollection(rowObservables.size());
			}
			control.addRowConstructionListener(rowToRidgetMapper);
			control.addRowContentProvider(rowToRidgetMapper);
			control.addRowFocusListener(selectionSynchronizer);
		}

	}

	@Override
	protected void unbindUIControl() {
		CompositeTable control = getUIControl();
		if (control != null) {
			control.removeRowFocusListener(selectionSynchronizer);
			control.removeRowContentProvider(rowToRidgetMapper);
			control.removeRowConstructionListener(rowToRidgetMapper);
		}
	}

	@Override
	protected List<?> getRowObservables() {
		return rowObservables;
	}

	/**
	 * Bind the composite table to the given model data and specify which
	 * composite to use for the rows.
	 * 
	 * @param rowBeansObservables
	 *            An observable list of beans (non-null).
	 * @param rowBeanClass
	 *            The class of the beans in the list
	 * @param rowRidgetClass
	 *            A class (extending Composite) which will be instantiated for
	 *            each row. It must provide a public constructor with these
	 *            parameters: {@code Composite parent, int style}.
	 */
	// TODO [ev] this javadoc is SWT specific
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
			Point selection = control.getSelection();
			control.setNumRowsInCollection(rowObservables.size());
			control.refreshAllRows();
			if (selection != null && !selection.equals(control.getSelection())) {
				control.setSelection(selection);
			}
		}
	}

	@Override
	public int getSelectionIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] getSelectionIndices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CompositeTable getUIControl() {
		return (CompositeTable) super.getUIControl();
	}

	@Override
	public int indexOfOption(Object option) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isDisableMandatoryMarker() {
		return true;
	}

	// helping methods
	//////////////////

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
			Object rowBean = rowObservables.get(index);
			Assert.isLegal(rowBeanClass.isAssignableFrom(rowBean.getClass()));
			IRowRidget rowRidget = (IRowRidget) row.getData("rowRidget"); //$NON-NLS-1$
			rowRidget.setData(rowBean);
			rowRidget.configureRidgets();
		}

		private IRidget createRidget(Object control) throws ReflectionFailure {
			Class<? extends IRidget> ridgetClass = mapper.getRidgetClass(control);
			return ReflectionUtils.newInstance(ridgetClass);
		}
	}

	/**
	 * Updates the selection in a CompositeTable control, when the value of the
	 * (single selection) observable changes.
	 */
	private final class SelectionSynchronizer implements IRowFocusListener {

		public void arrive(CompositeTable sender, int currentObjectOffset, Control newRow) {
			//			System.out.println("arrive: " + currentObjectOffset);
			//			System.out.println(getSingleSelectionObservable().getValue());
		}

		public void depart(CompositeTable sender, int currentObjectOffset, Control row) {
			//			System.out.println("depart: " + currentObjectOffset);
		}

		public boolean requestRowChange(CompositeTable sender, int currentObjectOffset, Control row) {
			return true;
		}

	}

}
