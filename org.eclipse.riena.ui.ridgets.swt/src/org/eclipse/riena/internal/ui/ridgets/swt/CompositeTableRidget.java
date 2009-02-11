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
import org.eclipse.riena.core.util.ReflectionFailure;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.ui.common.IComplexComponent;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingPropertyLocator;
import org.eclipse.riena.ui.ridgets.uibinding.IControlRidgetMapper;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.nebula.widgets.compositetable.CompositeTable;
import org.eclipse.swt.nebula.widgets.compositetable.IRowContentProvider;
import org.eclipse.swt.widgets.Control;

/**
 * TODO [ev] docs
 */
public class CompositeTableRidget extends AbstractSelectableIndexedRidget {

	private final IRowContentProvider rowContentProvider;

	private IObservableList rowObservables;
	private Class<? extends Object> rowBeanClass;
	private Class<? extends Object> rowRidgetClass;

	public CompositeTableRidget() {
		rowContentProvider = new CTRowContentProvider();
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
			control.addRowContentProvider(rowContentProvider);
		}

	}

	@Override
	protected void unbindUIControl() {
		CompositeTable control = getUIControl();
		if (control != null) {
			control.removeRowContentProvider(rowContentProvider);
		}
	}

	@Override
	protected List<?> getRowObservables() {
		return rowObservables;
	}

	public void bindToModel(IObservableList rowBeansObservables, Class<? extends Object> rowBeanClass,
			Class<? extends Object> rowRidgetClass) {
		unbindUIControl();

		rowObservables = rowBeansObservables;
		this.rowBeanClass = rowBeanClass;
		this.rowRidgetClass = rowRidgetClass;

		bindUIControl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.AbstractRidget#updateFromModel()
	 */
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

	private final class CTRowContentProvider implements IRowContentProvider {

		private final IControlRidgetMapper<Object> mapper = new DefaultSwtControlRidgetMapper();

		public void refresh(CompositeTable table, int index, Control row) {
			Object bean = rowObservables.get(index);
			Assert.isLegal(rowBeanClass.isAssignableFrom(bean.getClass()));
			IComplexComponent rowControl = (IComplexComponent) row;
			IRidgetContainer rowRidget = (IRidgetContainer) ReflectionUtils
					.newInstance(rowRidgetClass, (Object[]) null);
			ReflectionUtils.invoke(rowRidget, "setBean", bean);
			IBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
			for (Object control : rowControl.getUIControls()) {
				String bindingProperty = locator.locateBindingProperty(control);
				if (bindingProperty != null) {
					IRidget ridget = createRidget(control);
					ridget.setUIControl(control);
					rowRidget.addRidget(bindingProperty, ridget);
				} else {
					// TODO [ev] log this
					System.err.println(String.format("widget without binding property: %s : %s", rowControl.getClass(),
							control));
				}
			}
			Wire.instance(rowRidget).andStart(Activator.getDefault().getContext()); // TODO [ev] why?
			rowRidget.configureRidgets();
		}

		private IRidget createRidget(Object control) throws ReflectionFailure {
			Class<? extends IRidget> ridgetClass = mapper.getRidgetClass(control);
			return ReflectionUtils.newInstance(ridgetClass);
		}
	}

}
