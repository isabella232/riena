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

import org.eclipse.riena.tests.UITestHelper;
import org.eclipse.riena.ui.ridgets.IDateTextFieldRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtControlRidgetMapper;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * Tests for the class {@link DateTextRidget}.
 */
public class DateTextRidgetTest extends AbstractSWTRidgetTest {

	@Override
	protected IRidget createRidget() {
		DateTextRidget result = new DateTextRidget();
		result.setFormat(IDateTextFieldRidget.FORMAT_DDMMYYYY);
		return result;
	}

	@Override
	protected IDateTextFieldRidget getRidget() {
		return (IDateTextFieldRidget) super.getRidget();
	}

	@Override
	protected Control createUIControl(Composite parent) {
		Control result = new Text(getShell(), SWT.RIGHT | SWT.BORDER | SWT.SINGLE);
		result.setData(UIControlsFactory.KEY_TYPE, UIControlsFactory.TYPE_DATE);
		result.setLayoutData(new RowData(100, SWT.DEFAULT));
		return result;
	}

	@Override
	protected Text getUIControl() {
		return (Text) super.getUIControl();
	}

	// test methods
	///////////////

	public void testRidgetMapping() {
		DefaultSwtControlRidgetMapper mapper = new DefaultSwtControlRidgetMapper();
		assertSame(DateTextRidget.class, mapper.getRidgetClass(getUIControl()));
	}

	public void testDelete() {
		IDateTextFieldRidget ridget = getRidget();
		ridget.setFormat(IDateTextFieldRidget.FORMAT_DDMMYYYY);

		assertText("01.10^.2008", UITestHelper.KC_DEL, "01.10.^2008");
		assertText("01.10^.^2008", UITestHelper.KC_DEL, "01.10^.2008");
		assertText("01.10^.2^008", UITestHelper.KC_DEL, "01.10^. 008");
		assertText("01.1^0.2^008", UITestHelper.KC_DEL, "01. 1^. 008");
		assertText("^01.10.2008^", UITestHelper.KC_DEL, "  ^.  .    ");

		assertText("01.10.^2008", "\b", "01.10^.2008");
		assertText("01.10^.^2008", "\b", "01.10^.2008");
		assertText("01.10^.2^008", "\b", "01.10^. 008");
		assertText("01.1^0.2^008", "\b", "01. 1^. 008");
		assertText("^01.10.2008^", "\b", "  ^.  .    ");
	}

	public void testReplace() {
		IDateTextFieldRidget ridget = getRidget();
		ridget.setFormat(IDateTextFieldRidget.FORMAT_DDMMYYYY);

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
		IDateTextFieldRidget ridget = getRidget();
		ridget.setFormat(IDateTextFieldRidget.FORMAT_DDMMYYYY);

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
		// TODO [ev] implement
		//		IDecimalValueTextFieldRidget ridget = getRidget();
		//		ridget.setGrouping(true);
		//		ridget.setPrecision(2);
		//		Text control = getUIControl();
		//
		//		ridget.setText(localize("12345"));
		//
		//		assertEquals(localize("12.345,"), control.getText());
		//		assertEquals(localize("12.345"), ridget.getText());
		//
		//		ridget.setText(localize("3,145"));
		//
		//		assertEquals(localize("3,145"), control.getText());
		//		assertEquals(localize("3,145"), ridget.getText());
		//
		//		final String lastText = ridget.getText();
		//		try {
		//			ridget.setText("abc");
		//			fail();
		//		} catch (RuntimeException rex) {
		//			assertEquals(lastText, control.getText());
		//			assertEquals(lastText, ridget.getText());
		//		}
	}

	public void testUpdateFromModel() {
		// TODO [ev] implement
		//		IDecimalValueTextFieldRidget ridget = getRidget();
		//		Text control = getUIControl();
		//
		//		ridget.setMaxLength(6);
		//		ridget.setPrecision(3);
		//
		//		StringBean bean = new StringBean(localize("1,2"));
		//		ridget.bindToModel(bean, StringBean.PROP_VALUE);
		//		ridget.updateFromModel();
		//
		//		// Test initial values
		//		assertEquals(6, ridget.getMaxLength());
		//		assertEquals(3, ridget.getPrecision());
		//		assertEquals(localize("1,200"), control.getText());
		//		assertEquals(localize("1,2"), ridget.getText());
		//		assertEquals(localize("1,2"), bean.getValue());
		//
		//		// Test with bean value 0.0
		//		bean.setValue(localize("0,0"));
		//		ridget.updateFromModel();
		//		ridget.updateFromModel();
		//
		//		assertEquals(localize(",000"), control.getText());
		//		assertEquals(localize("0"), ridget.getText());
		//		assertEquals(localize("0,0"), bean.getValue());
	}

	public void testMaxLength() {
		// TODO [ev] implement
		//		IDecimalValueTextFieldRidget ridget = getRidget();
		//		Text control = getUIControl();
		//		ridget.setMaxLength(6);
		//		ridget.setPrecision(3);
		//		StringBean bean = new StringBean();
		//		ridget.bindToModel(bean, StringBean.PROP_VALUE);
		//
		//		control.setFocus();
		//		UITestHelper.sendString(control.getDisplay(), localize("123456,12345\r"));
		//
		//		assertEquals(localize("123.456,123"), control.getText());
		//		assertEquals(localize("123.456,123"), ridget.getText());
		//		assertEquals(localize("123.456,123"), bean.getValue());
	}

	// helping methods
	//////////////////

	private void assertText(String before, String keySeq, String after) {
		forcetText(before);

		checkText(before);
		checkSelection(before);
		checkCaret(before);

		Text control = getUIControl();
		control.setFocus();
		UITestHelper.sendString(control.getDisplay(), keySeq);

		checkText(after);
		checkCaret(after);
	}

	private void assertText(String before, int keyCode, String after) {
		forcetText(before);

		checkText(before);
		checkSelection(before);
		checkCaret(before);

		Text control = getUIControl();
		control.setFocus();
		UITestHelper.sendKeyAction(control.getDisplay(), keyCode);

		checkText(after);
		checkCaret(after);
	}

	private void checkText(String input) {
		String expected = removePositionMarkers(input);
		Text control = getUIControl();
		assertEquals(expected, control.getText());
	}

	private void checkSelection(String input) {
		int start = input.indexOf('^');
		int end = input.lastIndexOf('^');
		String expected = "";
		if (start < end) {
			expected = input.substring(start + 1, end);
		}
		// System.out.println("exp sel: " + expected);
		Text control = getUIControl();
		assertEquals(expected, control.getSelectionText());
	}

	private void checkCaret(String input) {
		int start = input.indexOf('^');
		int end = input.lastIndexOf('^');
		int expected = start < end ? end - 1 : end;
		// System.out.println("exp car: " + expected);
		Text control = getUIControl();
		assertEquals(expected, control.getCaretPosition());
	}

	private String removePositionMarkers(String input) {
		StringBuilder result = new StringBuilder(input.length());
		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			if (ch != '^') {
				result.append(ch);
			}
		}
		return result.toString();
	}

	private void forcetText(String text) {
		int start = text.indexOf('^');
		int end = text.lastIndexOf('^');

		Text control = getUIControl();
		Listener[] listeners = control.getListeners(SWT.Verify);
		for (Listener listener : listeners) {
			control.removeListener(SWT.Verify, listener);
		}
		control.setText(removePositionMarkers(text));
		for (Listener listener : listeners) {
			control.addListener(SWT.Verify, listener);
		}
		if (start == end) {
			control.setSelection(start, start);
		} else {
			control.setSelection(start, end - 1);
		}
	}
}
