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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.beans.common.AbstractBean;
import org.eclipse.riena.tests.UITestHelper;
import org.eclipse.riena.ui.ridgets.IMasterDetailsDelegate;
import org.eclipse.riena.ui.ridgets.IMasterDetailsRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.swt.MasterDetailsRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.uibinding.DefaultBindingManager;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingManager;
import org.eclipse.riena.ui.swt.MasterDetailsComposite;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Tests for the class {@link MasterDetailsRidget}
 */
public class MasterDetailsRidgetTest extends AbstractSWTRidgetTest {

	private static final IBindingManager BINDING_MAN = new DefaultBindingManager(SWTBindingPropertyLocator
			.getInstance(), SwtControlRidgetMapper.getInstance());
	private final String[] columnProperties = { MDBean.PROPERTY_COLUMN_1, MDBean.PROPERTY_COLUMN_2 };
	private final String[] columnHeaders = { "TestColumn1Header", "TestColumn2Header" };

	private List<MDBean> input;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		input = createInput();
		MasterDetailsRidget ridget = (MasterDetailsRidget) getRidget();
		List<Object> uiControls = getWidget().getUIControls();
		BINDING_MAN.injectRidgets(ridget, uiControls);
		BINDING_MAN.bind(ridget, uiControls);
		ridget.setDelegate(new MDDelegate());
		getShell().setSize(300, 300);
	}

	@Override
	protected Widget createWidget(Composite parent) {
		return new MDWidget(parent, SWT.NONE);
	}

	@Override
	protected IRidget createRidget() {
		return new MasterDetailsRidget();
	}

	@Override
	protected MDWidget getWidget() {
		return (MDWidget) super.getWidget();
	}

	@Override
	protected MasterDetailsRidget getRidget() {
		return (MasterDetailsRidget) super.getRidget();
	}

	// test methods
	///////////////

	public void testRidgetMapping() {
		SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertSame(MasterDetailsRidget.class, mapper.getRidgetClass(getWidget()));
	}

	public void testBindToModel() {
		IMasterDetailsRidget<?> ridget = getRidget();
		MasterDetailsComposite composite = getWidget();
		Table table = composite.getTable();

		assertEquals(0, table.getItemCount());

		bindToModel();

		// TODO [ev] 264325 - enable this after adding seperation into TableRidget
		// assertEquals(0, table.getItemCount());

		ridget.updateFromModel();

		assertEquals(2, table.getColumnCount());
		assertEquals(3, table.getItemCount());
		assertEquals("TestColumn1Header", table.getColumn(0).getText());
		assertEquals("TestColumn2Header", table.getColumn(1).getText());
		assertContent(table, 3);
	}

	public void testSetUIControl() {
		IMasterDetailsRidget<?> ridget = getRidget();
		Table table = getWidget().getTable();
		MasterDetailsComposite mdComposite2 = (MasterDetailsComposite) createWidget(getShell());
		Table table2 = mdComposite2.getTable();

		assertEquals(0, table.getItemCount());

		bindToModel();
		ridget.updateFromModel();

		assertEquals(3, table.getItemCount());

		ridget.setUIControl(mdComposite2);
		input.remove(0);
		ridget.updateFromModel();

		assertEquals(3, table.getItemCount());
		assertEquals(2, table2.getItemCount());

		ridget.setUIControl(null);
		input.remove(0);
		ridget.updateFromModel();

		assertEquals(3, table.getItemCount());
		assertEquals(2, table2.getItemCount());

		try {
			ridget.setUIControl(new Table(getShell(), SWT.MULTI));
			fail();
		} catch (RuntimeException rex) {
			ok("does not allow SWT.MULTI");
		}
	}

	public void testAddBean() {
		IMasterDetailsRidget<?> ridget = getRidget();
		MDWidget widget = getWidget();
		Table table = widget.getTable();

		bindToModel();
		ridget.updateFromModel();

		assertContent(table, 3);
		assertEquals(3, input.size());

		ridget.clearDetails();
		widget.txtColumn1.setFocus();
		UITestHelper.sendString(widget.getDisplay(), "A\r");
		widget.txtColumn2.setFocus();
		UITestHelper.sendString(widget.getDisplay(), "B\r");
		ridget.copyFromDetailsToMaster();

		assertEquals(4, input.size());
		assertContent(table, 3);
		assertEquals("A", input.get(3).getColumn1());
		assertEquals("B", input.get(3).getColumn2());
	}

	public void testDeleteBean() {
		MasterDetailsRidget ridget = getRidget();
		MDWidget widget = getWidget();
		Table table = widget.getTable();

		bindToModel();
		ridget.updateFromModel();

		assertContent(table, 3);
		assertEquals(3, input.size());

		MDBean toDelete = input.get(1);
		ridget.setSelection(toDelete, true);
		ridget.removeSelection();

		assertEquals(2, input.size());
		assertFalse(input.contains(toDelete));
	}

	public void testModifyBean() {
		IMasterDetailsRidget<Object> ridget = getRidget();
		MDWidget widget = getWidget();
		Table table = widget.getTable();

		bindToModel();
		ridget.updateFromModel();

		assertContent(table, 3);
		assertEquals(3, input.size());

		ridget.setSelection(input.get(1), false);
		widget.txtColumn1.setFocus();
		UITestHelper.sendString(widget.getDisplay(), "A\r");
		widget.txtColumn2.setFocus();
		UITestHelper.sendString(widget.getDisplay(), "B\r");
		ridget.copyFromDetailsToMaster();

		assertEquals(3, input.size());
		assertEquals("A", input.get(1).getColumn1());
		assertEquals("B", input.get(1).getColumn2());
	}

	public void testAddToMaster() {

	}

	public void testClearDetails() {

	}

	public void testCopyFromDetailsToMaster() {

	}

	public void testCopyFromMasterToDetails() {

	}

	public void testGetMasterSelection() {

	}

	public void testGetWorkingCopy() {

	}

	public void testIsDetailsChanged() {

	}

	public void isInputValid() {

	}

	public void testSetSelection() {

	}

	public void testSetSelectionFiresEvents() {

	}

	public void testSetSelectionRevealsSelection() {

	}

	public void testCreateWorkingCopyObject() {

	}

	public void testCopyBean() {

	}

	public void testUpdateDetails() {

	}

	public void testUpdateShowsDialogWhenRulesFail() {
		// warning case ??
		// error case
	}

	public void testModifyiedDetailsShowWarningOnChangeSelection() {

	}

	// helping methods
	//////////////////

	private void assertContent(Table table, int items) {
		for (int i = 0; i < items; i++) {
			String label0 = String.format("TestR%dC1", i);
			String label1 = String.format("TestR%dC2", i);
			assertEquals(label0, table.getItem(i).getText(0));
			assertEquals(label1, table.getItem(i).getText(1));
		}
	}

	private void bindToModel() {
		WritableList list = new WritableList(input, MDBean.class);
		getRidget().bindToModel(list, MDBean.class, columnProperties, columnHeaders);
	}

	private List<MDBean> createInput() {
		List<MDBean> result = new ArrayList<MDBean>();
		result.add(new MDBean("TestR0C1", "TestR0C2"));
		result.add(new MDBean("TestR1C1", "TestR1C2"));
		result.add(new MDBean("TestR2C1", "TestR2C2"));
		return result;
	}

	// helping classes
	//////////////////

	/**
	 * A bean with two String values; {@code column1} and {@code column2}.
	 */
	private static final class MDBean extends AbstractBean {

		private static final String PROPERTY_COLUMN_1 = "column1";
		private static final String PROPERTY_COLUMN_2 = "column2";

		private String column1;
		private String column2;

		MDBean(String column1, String column2) {
			this.column1 = column1;
			this.column2 = column2;
		}

		public String getColumn1() {
			return column1;
		}

		public String getColumn2() {
			return column2;
		}

		public void setColumn1(String column1) {
			firePropertyChanged("column1", this.column1, this.column1 = column1);
		}

		public void setColumn2(String column2) {
			firePropertyChanged("column2", this.column2, this.column2 = column2);
		}

		@Override
		public String toString() {
			return String.format("[%s, %s]", column1, column2);
		}
	}

	/**
	 * A MasterDetailsComposite with a details area containing two text fields.
	 */
	private static final class MDWidget extends MasterDetailsComposite {

		private Text txtColumn1;
		private Text txtColumn2;

		public MDWidget(Composite parent, int style) {
			super(parent, style, SWT.BOTTOM);
		}

		@Override
		protected void createDetails(Composite parent) {
			GridLayoutFactory.fillDefaults().numColumns(1).applyTo(parent);
			GridDataFactory hFill = GridDataFactory.fillDefaults().grab(true, false);

			txtColumn1 = UIControlsFactory.createText(parent);
			hFill.applyTo(txtColumn1);
			addUIControl(txtColumn1, "txtColumn1");

			txtColumn2 = UIControlsFactory.createText(parent);
			hFill.applyTo(txtColumn2);
			addUIControl(txtColumn2, "txtColumn2");
		}
	}

	/**
	 * Implements a delegate with two text ridgets. This class is a companion
	 * class to {@link MDBean} and {@link MDWidget}.
	 */
	private static final class MDDelegate implements IMasterDetailsDelegate {

		private final MDBean workingCopy = createWorkingCopyObject();

		public void configureRidgets(IRidgetContainer container) {
			ITextRidget txtColumn1 = (ITextRidget) container.getRidget("txtColumn1");
			txtColumn1.bindToModel(workingCopy, MDBean.PROPERTY_COLUMN_1);
			txtColumn1.updateFromModel();

			ITextRidget txtColumn2 = (ITextRidget) container.getRidget("txtColumn2");
			txtColumn2.bindToModel(workingCopy, MDBean.PROPERTY_COLUMN_2);
			txtColumn2.updateFromModel();
		}

		public Object copyBean(Object source, Object target) {
			MDBean from = source == null ? createWorkingCopyObject() : (MDBean) source;
			MDBean to = target == null ? createWorkingCopyObject() : (MDBean) target;
			to.setColumn1(from.getColumn1());
			to.setColumn2(from.getColumn2());
			return to;
		}

		public MDBean createWorkingCopyObject() {
			return new MDBean("", "");
		}

		public MDBean getWorkingCopy() {
			return workingCopy;
		}

		public void updateDetails(IRidgetContainer container) {
			for (IRidget ridget : container.getRidgets()) {
				ridget.updateFromModel();
			}
		}

	}
}
