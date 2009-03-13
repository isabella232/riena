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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.beans.common.DoubleBean;
import org.eclipse.riena.beans.common.StringBean;
import org.eclipse.riena.tests.TestUtils;
import org.eclipse.riena.tests.UITestHelper;
import org.eclipse.riena.ui.core.marker.NegativeMarker;
import org.eclipse.riena.ui.ridgets.IDecimalTextRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Tests for the class {@link DecimalTextRidget}.
 */
public class DecimalTextRidgetTest extends AbstractSWTRidgetTest {

	@Override
	protected IRidget createRidget() {
		return new DecimalTextRidget();
	}

	@Override
	protected IDecimalTextRidget getRidget() {
		return (IDecimalTextRidget) super.getRidget();
	}

	@Override
	protected Control createWidget(Composite parent) {
		Control result = new Text(getShell(), SWT.RIGHT | SWT.BORDER | SWT.SINGLE);
		result.setData(UIControlsFactory.KEY_TYPE, UIControlsFactory.TYPE_DECIMAL);
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
		assertSame(DecimalTextRidget.class, mapper.getRidgetClass(getWidget()));
	}

	public void testGroup() {
		assertEquals(localize("123.456,"), DecimalTextRidget.group(localize("123456"), true, true));
		assertEquals(localize("123.456,78"), DecimalTextRidget.group(localize("123456,78"), true, true));
		assertEquals(localize("0,78"), DecimalTextRidget.group(localize("0,78"), true, true));
		assertEquals(localize("0,00001"), DecimalTextRidget.group(localize("0,00001"), true, true));
		assertEquals(localize("1.000,00001"), DecimalTextRidget.group(localize("1000,00001"), true, true));
		assertEquals(localize(","), DecimalTextRidget.group(localize(","), true, true));
	}

	public void testUngroup() {
		assertEquals(localize("123456"), DecimalTextRidget.ungroup(localize("123456")));
		assertEquals(localize("123456,"), DecimalTextRidget.ungroup(localize("123456,")));
		assertEquals(localize("123,45"), DecimalTextRidget.ungroup(localize("123,45")));
		assertEquals(localize("123456,78"), DecimalTextRidget.ungroup(localize("123.456,78")));
		assertEquals(localize("0,0"), DecimalTextRidget.ungroup(localize("0,0")));
		assertEquals(localize("0,0123"), DecimalTextRidget.ungroup(localize("0,0123")));
		assertEquals(localize(","), DecimalTextRidget.ungroup(localize(",")));
	}

	public void testSetText() {
		IDecimalTextRidget ridget = getRidget();
		ridget.setGrouping(true);
		ridget.setPrecision(2);
		Text control = getWidget();

		ridget.setText(localize("12345"));

		assertEquals(localize("12.345,"), control.getText());
		assertEquals(localize("12.345"), ridget.getText());

		ridget.setText(localize("3,145"));

		assertEquals(localize("3,145"), control.getText());
		assertEquals(localize("3,145"), ridget.getText());

		final String lastText = ridget.getText();
		try {
			ridget.setText("abc");
			fail();
		} catch (RuntimeException rex) {
			assertEquals(lastText, control.getText());
			assertEquals(lastText, ridget.getText());
		}
	}

	/**
	 * Test that setText(null) clears the number (equiv. to setText("0")).
	 */
	public void testSetTextNull() {
		ITextRidget ridget = getRidget();

		ridget.setText(localize("42,2"));

		assertEquals(localize("42,2"), ridget.getText());

		ridget.setText(null);

		assertEquals("0", ridget.getText());
	}

	public void testDeleteDecimalSeparator() {
		IDecimalTextRidget ridget = getRidget();
		ridget.setMaxLength(6);
		ridget.setPrecision(4);
		ridget.setGrouping(true);
		ridget.setDirectWriting(true);
		Text control = getWidget();
		Display display = control.getDisplay();

		ridget.setText(localize("1234,9876"));

		assertEquals(localize("1.234,9876"), ridget.getText());

		control.setSelection(5, 5);
		UITestHelper.sendKeyAction(display, UITestHelper.KC_DEL);

		assertEquals(localize("1.234,876"), ridget.getText());
		assertEquals(6, control.getCaretPosition());

		UITestHelper.sendString(display, "\b");

		assertEquals(localize("123,876"), ridget.getText());
		assertEquals(3, control.getCaretPosition());

		ridget.setText(localize("1.234,9876"));
		control.setFocus();
		control.setSelection(4, 7);
		UITestHelper.sendKeyAction(display, UITestHelper.KC_DEL);

		assertEquals(localize("123,876"), ridget.getText());
		assertEquals(4, control.getCaretPosition());

		ridget.setText(localize("1.234,9876"));
		control.setSelection(4, 6);
		UITestHelper.sendKeyAction(display, UITestHelper.KC_DEL);

		assertEquals(localize("123,9876"), ridget.getText());
		assertEquals(3, control.getCaretPosition());

		ridget.setText(localize("1.234,9876"));
		control.setSelection(5, 7);
		UITestHelper.sendKeyAction(display, UITestHelper.KC_DEL);

		assertEquals(localize("1.234,876"), ridget.getText());
		assertEquals(6, control.getCaretPosition());

		ridget.setText(localize("1.234,9876"));
		control.setSelection(5, 10);
		UITestHelper.sendKeyAction(display, UITestHelper.KC_DEL);

		assertEquals(localize("1.234,"), control.getText());
		assertEquals(localize("1.234"), ridget.getText());
		assertEquals(6, control.getCaretPosition());

		ridget.setText(localize("1.234,9876"));
		control.setSelection(0, 6);
		UITestHelper.sendKeyAction(display, UITestHelper.KC_DEL);

		assertEquals(localize(",9876"), ridget.getText());
		assertEquals(0, control.getCaretPosition());

		ridget.setText(localize("1.234,9876"));
		control.selectAll();
		UITestHelper.sendKeyAction(display, UITestHelper.KC_DEL);

		assertEquals(localize(","), control.getText());
		assertEquals(localize(""), ridget.getText());
		assertEquals(0, control.getCaretPosition());
	}

	public void testDeleteNegativeSign() {
		IDecimalTextRidget ridget = getRidget();
		ridget.setSigned(true);
		ridget.setText(localize("1234,56"));
		ridget.setDirectWriting(true);
		Text control = getWidget();
		Display display = control.getDisplay();

		assertTrue(ridget.isMarkNegative());

		assertEquals(localize("1.234,56"), ridget.getText());
		assertEquals(0, ridget.getMarkersOfType(NegativeMarker.class).size());

		control.setFocus();
		UITestHelper.sendString(display, "-");

		assertEquals(localize("-1.234,56"), ridget.getText());
		assertEquals(1, ridget.getMarkersOfType(NegativeMarker.class).size());

		control.setFocus();
		control.setSelection(0, 0);
		UITestHelper.sendKeyAction(display, UITestHelper.KC_DEL);

		assertEquals(localize("1.234,56"), ridget.getText());
		assertEquals(0, ridget.getMarkersOfType(NegativeMarker.class).size());
	}

	public void testReplaceSelection() throws Exception {
		IDecimalTextRidget ridget = getRidget();
		ridget.setMaxLength(6);
		ridget.setPrecision(2);
		ridget.setGrouping(true);
		ridget.setDirectWriting(true);
		Text control = getWidget();
		Display display = control.getDisplay();

		ridget.setText(localize("123.456,78"));
		control.setSelection(0, 7);
		UITestHelper.sendString(display, "9");

		assertEquals(localize("9,78"), ridget.getText());

		ridget.setText(localize("123.456,78"));
		control.setSelection(8, 10);
		UITestHelper.sendString(display, "9");

		assertEquals(localize("123.456,9"), ridget.getText());

		ridget.setText(localize("123.456,78"));
		control.selectAll();
		UITestHelper.sendString(display, "1");

		assertEquals(localize("1,"), control.getText());
		assertEquals(localize("1"), ridget.getText());
		assertEquals(1, control.getCaretPosition());

		ridget.setText(localize("123.456,78"));
		control.setSelection(6, 9);
		UITestHelper.sendString(display, "9");

		assertEquals(localize("123.459,8"), ridget.getText());

		ridget.setText(localize("123.456,78"));
		control.setSelection(6, 8);
		UITestHelper.sendString(display, "9");

		assertEquals(localize("123.459,78"), ridget.getText());

		ridget.setText(localize("123.456,78"));
		control.setSelection(7, 9);
		UITestHelper.sendString(display, "9");

		assertEquals(localize("123.456,98"), ridget.getText());
	}

	public void testJumpOverDecimalSeparator() {
		IDecimalTextRidget ridget = getRidget();
		ridget.setGrouping(true);
		Text control = getWidget();
		Display display = control.getDisplay();

		// jump when directly at the left of the decimal separator 
		ridget.setText(localize("123.456,78"));
		control.setSelection(7);
		UITestHelper.sendString(display, localize(","));

		assertEquals(8, control.getCaretPosition());

		// don't jump if right of decimal separator
		control.setSelection(9);
		UITestHelper.sendString(display, localize(","));

		assertEquals(9, control.getCaretPosition());

		// don't jump if not directly on the left of the decimal separator
		control.setSelection(6);
		UITestHelper.sendString(display, localize(","));

		assertEquals(6, control.getCaretPosition());
	}

	public void testDoubleValueProviderAndHighNumbers() {
		final DoubleBean doubleValueBean = new DoubleBean() {
			@Override
			public Double getValue() {
				return 1000000000000000.0;
			}
		};

		IDecimalTextRidget ridget = getRidget();
		ridget.setMaxLength(6);
		ridget.setPrecision(3);
		ridget.bindToModel(doubleValueBean, DoubleBean.PROP_VALUE);
		ridget.updateFromModel();

		assertEquals(localize("1.000.000.000.000.000"), ridget.getText());
	}

	public void testDoubleValueProviderAndHighNumbersB() {
		final DoubleBean doubleValueBean = new DoubleBean() {
			@Override
			public Double getValue() {
				return 1000000000000000.0;
			}
		};

		IDecimalTextRidget ridget = getRidget();
		ridget.setMaxLength(16);
		ridget.setPrecision(3);
		ridget.bindToModel(doubleValueBean, DoubleBean.PROP_VALUE);
		ridget.updateFromModel();

		assertEquals(localize("1.000.000.000.000.000"), ridget.getText());
		assertEquals(localize("1.000.000.000.000.000,000"), getWidget().getText());
	}

	public void testUpdateFromModel() {
		IDecimalTextRidget ridget = getRidget();
		Text control = getWidget();

		ridget.setMaxLength(6);
		ridget.setPrecision(3);

		StringBean bean = new StringBean(localize("1,2"));
		ridget.bindToModel(bean, StringBean.PROP_VALUE);
		ridget.updateFromModel();

		// Test initial values
		assertEquals(6, ridget.getMaxLength());
		assertEquals(3, ridget.getPrecision());
		assertEquals(localize("1,200"), control.getText());
		assertEquals(localize("1,2"), ridget.getText());
		assertEquals(localize("1,2"), bean.getValue());

		// Test with bean value 0.0
		bean.setValue(localize("0,0"));
		ridget.updateFromModel();

		assertEquals(localize("0,000"), control.getText());
		assertEquals(localize("0"), ridget.getText());
		assertEquals(localize("0,0"), bean.getValue());

		String oldControlValue = control.getText();
		String oldRidgetValue = ridget.getText();
		bean.setValue("abc");
		ridget.updateFromModel();

		// excepted: ignore 'abc' because it is not a number
		assertEquals(oldControlValue, control.getText());
		assertEquals(oldRidgetValue, ridget.getText());
		assertEquals("abc", bean.getValue());
	}

	public void testMaxLength() {
		IDecimalTextRidget ridget = getRidget();
		Text control = getWidget();
		ridget.setMaxLength(6);
		ridget.setPrecision(3);
		StringBean bean = new StringBean();
		ridget.bindToModel(bean, StringBean.PROP_VALUE);

		control.setFocus();
		UITestHelper.sendString(control.getDisplay(), localize("123456,12345\r"));

		assertEquals(localize("123.456,123"), control.getText());
		assertEquals(localize("123.456,123"), ridget.getText());
		assertEquals(localize("123.456,123"), bean.getValue());
	}

	public void testGetSetMaxLength() {
		IDecimalTextRidget ridget = getRidget();

		try {
			ridget.setMaxLength(0);
			fail();
		} catch (RuntimeException rex) {
			ok();
		}

		try {
			ridget.setMaxLength(-1);
			fail();
		} catch (RuntimeException rex) {
			ok();
		}

		expectPropertyChangeEvent(IDecimalTextRidget.PROPERTY_MAXLENGTH, Integer.valueOf(10), Integer.valueOf(5));
		ridget.setMaxLength(5);

		verifyPropertyChangeEvents();
		assertEquals(5, ridget.getMaxLength());

		expectNoPropertyChangeEvent();
		ridget.setMaxLength(5);

		verifyPropertyChangeEvents();
	}

	public void testPrecision() throws Exception {
		IDecimalTextRidget ridget = getRidget();
		Text control = getWidget();
		ridget.setMaxLength(6);
		ridget.setPrecision(3);
		StringBean bean = new StringBean();
		ridget.bindToModel(bean, StringBean.PROP_VALUE);

		control.setFocus();
		UITestHelper.sendString(control.getDisplay(), "123456\r");

		assertEquals(localize("123.456,000"), control.getText());
		assertEquals(localize("123.456"), ridget.getText());
		assertEquals(localize("123.456"), bean.getValue());

		control.setFocus();
		UITestHelper.sendString(control.getDisplay(), "\b\b\b54321\r");

		assertEquals(localize("123.456,543"), control.getText());
		assertEquals(localize("123.456,543"), ridget.getText());
		assertEquals(localize("123.456,543"), bean.getValue());

		ridget.setPrecision(2);

		assertEquals(localize("123.456,54"), control.getText());
		assertEquals(localize("123.456,54"), ridget.getText());
		assertEquals(localize("123.456,54"), bean.getValue());

		ridget.setPrecision(0);

		assertEquals(localize("123.456,"), control.getText());
		assertEquals(localize("123.456"), ridget.getText());
		assertEquals(localize("123.456"), bean.getValue());
	}

	public void testGetSetPrecision() {
		IDecimalTextRidget ridget = getRidget();

		try {
			ridget.setPrecision(-1);
			fail();
		} catch (RuntimeException rex) {
			ok();
		}

		expectPropertyChangeEvent(IDecimalTextRidget.PROPERTY_PRECISION, Integer.valueOf(2), Integer.valueOf(5));
		ridget.setPrecision(5);

		verifyPropertyChangeEvents();
		assertEquals(5, ridget.getPrecision());

		expectNoPropertyChangeEvent();
		ridget.setPrecision(5);

		verifyPropertyChangeEvents();

		expectPropertyChangeEvent(IDecimalTextRidget.PROPERTY_PRECISION, Integer.valueOf(5), Integer.valueOf(0));
		ridget.setPrecision(0);

		verifyPropertyChangeEvents();
		assertEquals(0, ridget.getPrecision());
	}

	public void testIsSetWithSign() {
		IDecimalTextRidget ridget = getRidget();
		Text control = getWidget();
		ridget.setPrecision(3);
		StringBean bean = new StringBean();
		ridget.bindToModel(bean, StringBean.PROP_VALUE);

		assertFalse(ridget.isSigned());

		ridget.setSigned(true);

		assertTrue(ridget.isSigned());

		control.setFocus();
		UITestHelper.sendString(control.getDisplay(), "1-\r");

		assertEquals(localize("-1,000"), control.getText());
		assertEquals(localize("-1"), ridget.getText());
		assertEquals(localize("-1"), bean.getValue());

		ridget.setSigned(false);

		assertFalse(ridget.isSigned());

		control.selectAll();
		UITestHelper.sendString(control.getDisplay(), "1-\r");

		assertEquals(localize("1,000"), control.getText());
		assertEquals(localize("1"), ridget.getText());
		assertEquals(localize("1"), bean.getValue());
	}

	public void testPadFractionDigitsOnFocusOut() {
		IDecimalTextRidget ridget = getRidget();
		Text control = getWidget();
		ridget.setPrecision(3);
		ridget.setSigned(true);
		StringBean bean = new StringBean();
		ridget.bindToModel(bean, StringBean.PROP_VALUE);

		control.setFocus();
		UITestHelper.sendString(control.getDisplay(), localize("1234\t"));

		assertEquals(localize("1.234,000"), control.getText());
		assertEquals(localize("1.234"), ridget.getText());
		assertEquals(localize("1.234"), bean.getValue());

		control.setFocus();
		UITestHelper.sendString(control.getDisplay(), localize("\b,1\t"));

		assertEquals(localize("0,100"), control.getText());
		assertEquals(localize("0,1"), ridget.getText());
		assertEquals(localize("0,1"), bean.getValue());

		control.setFocus();
		UITestHelper.sendString(control.getDisplay(), localize("\b,\t"));

		assertEquals(localize("0,000"), control.getText());
		assertEquals(localize("0"), ridget.getText());
		assertEquals(localize("0"), bean.getValue());

		control.setFocus();
		UITestHelper.sendString(control.getDisplay(), localize("\b,-\t"));

		assertEquals(localize("0,000"), control.getText());
		assertEquals(localize("0"), ridget.getText());
		assertEquals(localize("0"), bean.getValue());
	}

	public void testMandatoryMarker() {
		IDecimalTextRidget ridget = getRidget();
		ridget.setDirectWriting(true);
		Text control = getWidget();

		ridget.setMandatory(true);
		ridget.setText("");

		assertTrue(ridget.isMandatory());
		assertFalse(ridget.isDisableMandatoryMarker());

		control.setFocus();
		UITestHelper.sendString(control.getDisplay(), "1");

		assertTrue(ridget.isMandatory());
		assertTrue(ridget.isDisableMandatoryMarker());

		control.setFocus();
		UITestHelper.sendString(control.getDisplay(), "\b");

		assertTrue(ridget.isMandatory());
		assertFalse(ridget.isDisableMandatoryMarker());
	}

	public void testDisabledMarker() {
		IDecimalTextRidget ridget = getRidget();
		Text control = getWidget();

		ridget.setText(localize("12,00"));

		assertTrue(ridget.isEnabled());
		assertEquals(localize("12,00"), control.getText());
		assertEquals("12", ridget.getText());

		ridget.setEnabled(false);

		assertFalse(ridget.isEnabled());
		assertEquals("", control.getText());
		assertEquals("12", ridget.getText());

		ridget.setEnabled(true);

		assertTrue(ridget.isEnabled());
		assertEquals(localize("12,00"), control.getText());
		assertEquals("12", ridget.getText());

		ridget.setEnabled(false);
		ridget.setText(localize("1234,00"));
		ridget.setEnabled(true);

		assertTrue(ridget.isEnabled());
		assertEquals(localize("1.234,00"), control.getText());
		assertEquals(localize("1.234"), ridget.getText());
	}

	// helping methods
	//////////////////

	private String localize(String number) {
		return TestUtils.getLocalizedNumber(number);
	}

}
