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
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.beans.common.AbstractBean;
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
	private List<MDBean> input;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		input = createInput();
		String[] columnProperties = { MDBean.PROPERTY_COLUMN_1, MDBean.PROPERTY_COLUMN_2 };
		String[] columnHeaders = { "TestColumn1Header", "TestColumn2Header" };
		MasterDetailsRidget ridget = (MasterDetailsRidget) getRidget();
		BINDING_MAN.injectRidgets(ridget, getWidget().getUIControls());
		ridget.setDelegate(new MDDelegate());
		ridget.bindToModel(new WritableList(input, MDBean.class), MDBean.class, columnProperties, columnHeaders);
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
	protected MasterDetailsComposite getWidget() {
		return (MasterDetailsComposite) super.getWidget();
	}

	@Override
	protected IMasterDetailsRidget<?> getRidget() {
		return (IMasterDetailsRidget<?>) super.getRidget();
	}

	// test methods
	///////////////

	public void testRidgetMapping() {
		SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertSame(MasterDetailsRidget.class, mapper.getRidgetClass(getWidget()));
	}

	// helping methods
	//////////////////

	private List<MDBean> createInput() {
		List<MDBean> result = new ArrayList<MDBean>();
		result.add(new MDBean("TestR1C1", "TestR1C2"));
		result.add(new MDBean("TestR2C1", "TestR1C2"));
		result.add(new MDBean("TestR3C1", "TestR1C2"));
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
	}

	/**
	 * A MasterDetailsComposite with a details area containing two text fields.
	 */
	private static final class MDWidget extends MasterDetailsComposite {

		public MDWidget(Composite parent, int style) {
			super(parent, style, SWT.BOTTOM);
		}

		@Override
		protected void createDetails(Composite parent) {
			GridLayoutFactory.fillDefaults().numColumns(1).applyTo(parent);
			GridDataFactory hFill = GridDataFactory.fillDefaults().grab(true, false);

			Text txtColumn1 = UIControlsFactory.createText(parent);
			hFill.applyTo(txtColumn1);
			addUIControl(txtColumn1, "txtColumn1");

			Text txtColumn2 = UIControlsFactory.createText(parent);
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

			ITextRidget txtColumn2 = (ITextRidget) container.getRidget("txtColumn1");
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
