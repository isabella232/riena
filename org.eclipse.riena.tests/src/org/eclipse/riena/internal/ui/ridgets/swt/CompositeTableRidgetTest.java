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

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.tests.UITestHelper;
import org.eclipse.riena.ui.common.IComplexComponent;
import org.eclipse.riena.ui.ridgets.AbstractCompositeRidget;
import org.eclipse.riena.ui.ridgets.ICompositeTableRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRowRidget;
import org.eclipse.riena.ui.ridgets.ISelectableIndexedRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.databinding.UnboundPropertyWritableList;
import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtControlRidgetMapper;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.nebula.widgets.compositetable.CompositeTable;
import org.eclipse.swt.nebula.widgets.compositetable.ResizableGridRowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

/**
 * Tests of the class {@link CompositeTableRidget}.
 */
public class CompositeTableRidgetTest extends AbstractTableRidgetTest {

	@Override
	protected Widget createWidget(Composite parent) {
		CompositeTable control = new CompositeTable(parent, SWT.NONE);
		control.setLayoutData(new RowData(100, 100));
		new Row(control, SWT.NONE);
		control.setRunTime(true);
		return control;
	}

	@Override
	protected IRidget createRidget() {
		return new CompositeTableRidget();
	}

	@Override
	protected CompositeTable getWidget() {
		return (CompositeTable) super.getWidget();
	}

	@Override
	protected ICompositeTableRidget getRidget() {
		return (ICompositeTableRidget) super.getRidget();
	}

	@Override
	protected void bindRidgetToModel() {
		IObservableList rowObservables = new UnboundPropertyWritableList(manager, "persons");
		getRidget().bindToModel(rowObservables, Person.class, RowRidget.class);
	}

	// test methods
	///////////////

	public void testRidgetMapping() {
		DefaultSwtControlRidgetMapper mapper = new DefaultSwtControlRidgetMapper();
		assertSame(CompositeTableRidget.class, mapper.getRidgetClass(getWidget()));
	}

	@Override
	public void testUpdateSingleSelectionFromRidgetOnRebind() {
		ISelectableIndexedRidget ridget = getRidget();
		CompositeTable control = getWidget();

		setUIControlRowSelectionInterval(2, 2);

		assertEquals(2, getUIControlSelectedRow());

		ridget.setUIControl(null); // unbind
		control.setSelection(0, 1); // select row 1 without binding
		UITestHelper.readAndDispatch(control);

		assertEquals(1, getUIControlSelectedRow());
		assertEquals(getRowValue(2), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(2), multiSelectionBean.getSelectionList().get(0));

		System.out.println("bef: " + singleSelectionBean.getSelection());
		ridget.setUIControl(control); // rebind; row 2 should be selected
		System.out.println("aft: " + singleSelectionBean.getSelection());

		assertEquals(2, getUIControlSelectedRow());
		assertEquals(getRowValue(2), singleSelectionBean.getSelection());
		assertEquals(1, multiSelectionBean.getSelectionList().size());
		assertEquals(getRowValue(2), multiSelectionBean.getSelectionList().get(0));
	}

	// helping methods
	//////////////////

	/**
	 * Not supported.
	 */
	@Override
	protected void clearUIControlRowSelection() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Object getRowValue(int i) {
		// return getRidget().getRowObservables().get(i);
		IObservableList rowObservables = ReflectionUtils.invokeHidden(getRidget(), "getRowObservables");
		return rowObservables.get(i);
	}

	@Override
	protected int[] getSelectedRows() {
		// IObservableList rowObservables = getRidget().getRowObservables();
		IObservableList rowObservables = ReflectionUtils.invokeHidden(getRidget(), "getRowObservables");
		Object[] elements = getRidget().getMultiSelectionObservable().toArray();
		int[] result = new int[elements.length];
		for (int i = 0; i < elements.length; i++) {
			Object element = elements[i];
			result[i] = rowObservables.indexOf(element);
		}
		return result;
	}

	@Override
	protected int getUIControlSelectedRow() {
		CompositeTable control = getWidget();
		Point selection = control.getSelection();
		return selection == null || selection.y == -1 ? -1 : selection.y + control.getTopRow();
	}

	@Override
	protected int getUIControlSelectedRowCount() {
		CompositeTable control = getWidget();
		Point selection = null;
		// workaround: check if we have selection, because 
		// CompositeTable cannot unselect
		if (getRidget().getSelectionIndex() != -1) {
			selection = control.getSelection();
		}
		return selection == null || selection.y == -1 ? 0 : 1;
	}

	@Override
	protected int[] getUIControlSelectedRows() {
		int index = getUIControlSelectedRow();
		return index == -1 ? new int[0] : new int[] { index };
	}

	@Override
	protected void setUIControlRowSelection(int[] indices) {
		throw new UnsupportedOperationException("not supported");
	}

	@Override
	protected void setUIControlRowSelectionInterval(int start, int end) {
		assertTrue("multiple selection is not supported", start == end);
		CompositeTable control = getWidget();
		int row = start - control.getTopRow();
		control.setSelection(0, row);
		UITestHelper.readAndDispatch(control);
	}

	@Override
	protected boolean supportsMulti() {
		return false;
	}

	// helping classes
	//////////////////

	/**
	 * A row control for the CompositeTable.
	 * 
	 * @see RowRidget
	 */
	public static class Row extends Composite implements IComplexComponent {
		private Text txtFirst;
		private Text txtLast;

		public Row(Composite parent, int style) {
			super(parent, style);
			setLayout(new ResizableGridRowLayout());
			txtFirst = UIControlsFactory.createText(this);
			txtFirst.setText("first");
			txtLast = UIControlsFactory.createText(this);
			txtLast.setText("last");
			SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
			locator.setBindingProperty(txtFirst, "first");
			locator.setBindingProperty(txtLast, "last");
		}

		public List<Object> getUIControls() {
			return Arrays.asList(new Object[] { txtFirst, txtLast });
		}
	}

	/**
	 * A row ridget for the CompositeTableRidget.
	 * 
	 * @see Row
	 */
	public static class RowRidget extends AbstractCompositeRidget implements IRowRidget {

		private Person rowData;

		public void setData(Object rowData) {
			this.rowData = (Person) rowData;
		}

		@Override
		public void configureRidgets() {
			ITextRidget txtFirst = (ITextRidget) getRidget("first"); //$NON-NLS-1$
			txtFirst.bindToModel(rowData, Person.PROPERTY_FIRSTNAME);
			txtFirst.updateFromModel();

			ITextRidget txtLast = (ITextRidget) getRidget("last"); //$NON-NLS-1$
			txtLast.bindToModel(rowData, Person.PROPERTY_LASTNAME);
			txtLast.updateFromModel();
		}

	}

}
