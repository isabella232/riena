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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;

import org.eclipse.riena.ui.ridgets.IDateTimeRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;

/**
 * Tests for the class {@link DateTextRidget}.
 */
public class DateTimeRidgetTest extends AbstractSWTRidgetTest {

	@Override
	protected IRidget createRidget() {
		return new DateTimeRidget();
	}

	@Override
	protected IDateTimeRidget getRidget() {
		return (IDateTimeRidget) super.getRidget();
	}

	@Override
	protected Control createWidget(Composite parent) {
		return new DateTime(getShell(), SWT.DATE | SWT.MEDIUM);
	}

	@Override
	protected DateTime getWidget() {
		return (DateTime) super.getWidget();
	}

	// test methods
	///////////////

	public void testRidgetMapping() {
		SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertSame(DateTimeRidget.class, mapper.getRidgetClass(getWidget()));
	}

	//	public void testSetText() {
	//		IDateTextRidget ridget = getRidget();
	//		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
	//
	//		ridget.setText("01.10.2008");
	//		assertEquals("01.10.2008", ridget.getText());
	//
	//		ridget.setText("01.10");
	//		assertEquals("01.10.    ", ridget.getText());
	//
	//		ridget.setText("22.22.2222");
	//		assertEquals("22.22.2222", ridget.getText());
	//
	//		ridget.setText("");
	//		assertEquals("  .  .    ", ridget.getText());
	//
	//		ridget.setText("  .10.");
	//		assertEquals("  .10.    ", ridget.getText());
	//
	//		ridget.setText("  .  .");
	//		assertEquals("  .  .    ", ridget.getText());
	//
	//		try {
	//			ridget.setText("abc");
	//			fail();
	//		} catch (RuntimeException rex) {
	//			ok();
	//		}
	//
	//		try {
	//			ridget.setText("12102008");
	//			fail();
	//		} catch (RuntimeException rex) {
	//			ok();
	//		}
	//
	//		try {
	//			ridget.setText("12/10/2008");
	//			fail();
	//		} catch (RuntimeException rex) {
	//			ok();
	//		}
	//
	//		try {
	//			ridget.setText("12.ab");
	//			fail();
	//		} catch (RuntimeException rex) {
	//			ok();
	//		}
	//	}

	/**
	 * Tests that setText(null) clears the ridget (i.e. results in an empty
	 * pattern with just the separators)
	 */
	//	public void testSetTextNull() {
	//		IDateTextRidget ridget = getRidget();
	//		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
	//
	//		ridget.setText("01.10.2008");
	//
	//		assertEquals("01.10.2008", ridget.getText());
	//
	//		ridget.setText(null);
	//
	//		assertEquals("  .  .    ", ridget.getText());
	//	}

	public void testUpdateFromModel() {
		//		IDateTextRidget ridget = getRidget();
		//		Text control = getWidget();
		//		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
		//		StringBean bean = new StringBean("12.10.2008");
		//		ridget.bindToModel(bean, StringBean.PROP_VALUE);
		//
		//		// value fully matches pattern
		//		ridget.updateFromModel();
		//
		//		assertEquals("12.10.2008", control.getText());
		//		assertEquals("12.10.2008", ridget.getText());
		//		assertEquals("12.10.2008", bean.getValue());
		//
		//		// value matches sub-pattern
		//		bean.setValue("  .12");
		//		ridget.updateFromModel();
		//
		//		assertEquals("  .12.    ", control.getText());
		//		assertEquals("  .12.    ", ridget.getText());
		//		assertEquals("  .12", bean.getValue());
		//
		//		// value does not match patter; control and ridget unchanged
		//		bean.setValue("abc");
		//		ridget.updateFromModel();
		//
		//		assertEquals("  .12.    ", control.getText());
		//		assertEquals("  .12.    ", control.getText());
		//		assertEquals("abc", bean.getValue());
	}

	public void testMandatoryMarker() {
		//		IDateTextRidget ridget = getRidget();
		//		ridget.setMandatory(true);
		//		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
		//
		//		ridget.setText("31.10.2008");
		//
		//		TestUtils.assertMandatoryMarker(ridget, 1, true);
		//
		//		ridget.setText(null);
		//
		//		TestUtils.assertMandatoryMarker(ridget, 1, false);
		//
		//		ridget.setMandatory(false);
		//
		//		TestUtils.assertMandatoryMarker(ridget, 0, false);
	}
}
