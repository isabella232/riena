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
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.beans.common.StringBean;
import org.eclipse.riena.internal.ui.swt.test.TestUtils;
import org.eclipse.riena.internal.ui.swt.test.UITestHelper;
import org.eclipse.riena.ui.ridgets.IDateTextRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Tests for the class {@link DateTextRidget}.
 */
public class DateTextRidgetTest extends AbstractSWTRidgetTest {

	@Override
	protected IRidget createRidget() {
		DateTextRidget result = new DateTextRidget();
		result.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
		return result;
	}

	@Override
	protected IDateTextRidget getRidget() {
		return (IDateTextRidget) super.getRidget();
	}

	@Override
	protected Control createWidget(Composite parent) {
		Control result = new Text(getShell(), SWT.RIGHT | SWT.BORDER | SWT.SINGLE);
		result.setData(UIControlsFactory.KEY_TYPE, UIControlsFactory.TYPE_DATE);
		result.setLayoutData(new RowData(100, SWT.DEFAULT));
		return result;
	}

	@Override
	protected Text getWidget() {
		return (Text) super.getWidget();
	}

	// test methods
	///////////////

	public void testRidgetMapping() {
		SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertSame(DateTextRidget.class, mapper.getRidgetClass(getWidget()));
	}

	public void testDelete() {
		IDateTextRidget ridget = getRidget();
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);

		assertText("^01.10.2008", UITestHelper.KC_DEL, " ^1.10.2008");
		assertText("0^1.10.2008", UITestHelper.KC_DEL, " 0^.10.2008");
		assertText("01.10.200^8", UITestHelper.KC_DEL, "01.10. 200^");
		assertText("01.10.2008^", UITestHelper.KC_DEL, "01.10.2008^");
		assertText("01.10^.2008", UITestHelper.KC_DEL, "01.10. ^008");
		assertText("01.10^. 008", UITestHelper.KC_DEL, "01.10.  ^08");
		assertText("01.10^.^2008", UITestHelper.KC_DEL, "01.10^.2008");
		assertText("01.10^.2^008", UITestHelper.KC_DEL, "01.10^. 008");
		assertText("01.1^0.2^008", UITestHelper.KC_DEL, "01. 1^. 008");
		assertText("^01.10.2008^", UITestHelper.KC_DEL, "  ^.  .    ");

		assertText("^01.10.2008", "\b", "^01.10.2008");
		assertText("0^1.10.2008", "\b", " ^1.10.2008");
		assertText("01.10.200^8", "\b", "01.10. 20^8");
		assertText("01.10.2008^", "\b", "01.10. 200^");
		assertText("01.10.^2008", "\b", "01. 1^.2008");
		assertText("01. 1.^2008", "\b", "01.  ^.2008");
		assertText("01.10^.^2008", "\b", "01.10^.2008");
		assertText("01.10^.2^008", "\b", "01.10^. 008");
		assertText("01.1^0.2^008", "\b", "01. 1^. 008");
		assertText("^01.10.2008^", "\b", "  ^.  .    ");

		assertText("^  .  .    ", UITestHelper.KC_DEL, "  ^.  .    ");
		assertText("  ^.  .    ", UITestHelper.KC_DEL, "  .  ^.    ");
		assertText("  . ^ .    ", UITestHelper.KC_DEL, "  .  ^.    ");
		assertText("  .  ^.    ", UITestHelper.KC_DEL, "  .  .    ^");
		assertText("  .  .    ^", "\b", "  .  ^.    ");
		assertText("  . ^ .    ", "\b", "  ^.  .    ");
		assertText("  ^.  .    ", "\b", "  ^.  .    ");
		assertText(" ^ .  .    ", "\b", " ^ .  .    ");
	}

	public void testReplace() {
		IDateTextRidget ridget = getRidget();
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);

		assertText("01.10^.^2008", "1", "01.10^.2008");
		assertText("01.10^.^2008", ".", "01.10.^2008");
		assertText("01.10^.2^008", "1", "01.10.1^008");
		assertText("01.1^0.^2008", "3", "01.13^.2008");
		assertText("01.1^0.2^008", "3", "01.13^. 008");
		assertText("^01^.10.2008", "3", " 3^.10.2008");
		assertText("^01.^10.2008", "3", " 3^.10.2008");
		assertText("^01.1^0.2008", "3", " 3^. 0.2008");
		assertText("^01.10.2^008", "3", " 3^.  . 008");
		assertText("^01.10.2008^", "3", " 3^.  .    ");
	}

	public void testInsert() {
		IDateTextRidget ridget = getRidget();
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);

		assertText("  ^.  .    ", "01102008", "01.10.2008^");
		assertText(" ^ .  .    ", "01.10.2008", "01.10.2008^");
		assertText("^  .  .    ", "01.10.20081234", "01.10.2008^");
		assertText("  ^.10.2008", "0123", "01^.10.2008");
		assertText("  ^.  .2008", "1208", "12.08^.2008");
		assertText("  .  ^.2008", "1208", "  .12^.2008");
		assertText("01.  .^2008", "10", "01.  .^2008");
		assertText("  .  .    ^", "2008", "  .  .2008^");
		assertText("  .  ^.    ", "102008", "  .10.2008^");
	}

	public void testSetText() {
		IDateTextRidget ridget = getRidget();
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);

		ridget.setText("01.10.2008");
		assertEquals("01.10.2008", ridget.getText());

		ridget.setText("01.10");
		assertEquals("01.10.    ", ridget.getText());

		ridget.setText("22.22.2222");
		assertEquals("22.22.2222", ridget.getText());

		ridget.setText("");
		assertEquals("  .  .    ", ridget.getText());

		ridget.setText("  .10.");
		assertEquals("  .10.    ", ridget.getText());

		ridget.setText("  .  .");
		assertEquals("  .  .    ", ridget.getText());

		try {
			ridget.setText("abc");
			fail();
		} catch (RuntimeException rex) {
			ok();
		}

		try {
			ridget.setText("12102008");
			fail();
		} catch (RuntimeException rex) {
			ok();
		}

		try {
			ridget.setText("12/10/2008");
			fail();
		} catch (RuntimeException rex) {
			ok();
		}

		try {
			ridget.setText("12.ab");
			fail();
		} catch (RuntimeException rex) {
			ok();
		}
	}

	/**
	 * Tests that setText(null) clears the ridget (i.e. results in an empty
	 * pattern with just the separators)
	 */
	public void testSetTextNull() {
		IDateTextRidget ridget = getRidget();
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);

		ridget.setText("01.10.2008");

		assertEquals("01.10.2008", ridget.getText());

		ridget.setText(null);

		assertEquals("  .  .    ", ridget.getText());
	}

	public void testSetFormatAfterSetText() {
		IDateTextRidget ridget = getRidget();
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
		Text control = getWidget();
		StringBean bean = new StringBean();
		ridget.bindToModel(bean, StringBean.PROP_VALUE);

		ridget.setText("01.10.2008");

		assertEquals("01.10.2008", control.getText());
		assertEquals("01.10.2008", ridget.getText());
		assertEquals("01.10.2008", bean.getValue());

		ridget.setFormat(IDateTextRidget.FORMAT_HHMM);

		assertEquals("  :  ", control.getText());
		assertEquals("  :  ", ridget.getText());
		assertEquals("01.10.2008", bean.getValue());
	}

	public void testUpdateFromModel() {
		IDateTextRidget ridget = getRidget();
		Text control = getWidget();
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
		StringBean bean = new StringBean("12.10.2008");
		ridget.bindToModel(bean, StringBean.PROP_VALUE);

		// value fully matches pattern
		ridget.updateFromModel();

		assertEquals("12.10.2008", control.getText());
		assertEquals("12.10.2008", ridget.getText());
		assertEquals("12.10.2008", bean.getValue());

		// value matches sub-pattern
		bean.setValue("  .12");
		ridget.updateFromModel();

		assertEquals("  .12.    ", control.getText());
		assertEquals("  .12.    ", ridget.getText());
		assertEquals("  .12", bean.getValue());

		// value does not match patter; control and ridget unchanged
		bean.setValue("abc");
		ridget.updateFromModel();

		assertEquals("  .12.    ", control.getText());
		assertEquals("  .12.    ", control.getText());
		assertEquals("abc", bean.getValue());
	}

	public void testAutoFillYYYY() {
		IDateTextRidget ridget = getRidget();
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);

		assertText("  .  .    ^", "00\t", "  .  .2000");
		assertText("  .  .^    ", "01\t", "  .  .2001");
		assertText("  .  . ^   ", "29\t", "  .  .2029");
		assertText("  .  .    ^", "30\t", "  .  .1930");
		assertText("  .  .    ^", "99\t", "  .  .1999");
	}

	public void testAutoFillYYYYWithError() {
		IDateTextRidget ridget = getRidget();
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);

		ridget.setText("31.10.2008");
		assertFalse(ridget.isErrorMarked());
		assertText("31.10.    ^", "8\t", "31.10.   8");
		assertTrue(ridget.isErrorMarked());

		ridget.setText("31.10.2008");
		assertFalse(ridget.isErrorMarked());
		assertText("31.10.    ^", "008\t", "31.10. 008");
		assertTrue(ridget.isErrorMarked());
	}

	public void testDoNotFillYY() {
		IDateTextRidget ridget = getRidget();

		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYY);
		assertText("  .  .  ^", "00\t", "  .  .00");

		ridget.setFormat("ddMMyy");
		assertText("      ^", "00\t", "    00");
	}

	public void testMandatoryMarker() {
		IDateTextRidget ridget = getRidget();
		ridget.setMandatory(true);
		ridget.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);

		ridget.setText("31.10.2008");

		TestUtils.assertMandatoryMarker(ridget, 1, true);

		ridget.setText(null);

		TestUtils.assertMandatoryMarker(ridget, 1, false);

		ridget.setMandatory(false);

		TestUtils.assertMandatoryMarker(ridget, 0, false);
	}

	// helping methods
	//////////////////

	private void assertText(String before, String keySeq, String after) {
		TestUtils.assertText(getWidget(), before, keySeq, after);
	}

	private void assertText(String before, int keyCode, String after) {
		TestUtils.assertText(getWidget(), before, keyCode, after);
	}

}
