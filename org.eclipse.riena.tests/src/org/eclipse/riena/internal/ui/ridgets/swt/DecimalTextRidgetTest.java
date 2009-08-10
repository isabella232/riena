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

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
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
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.IDecimalTextRidget;
import org.eclipse.riena.ui.ridgets.INumericTextRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.validation.ValidationRuleStatus;
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

		assertEquals(localize("12.345,00"), control.getText());
		assertEquals(localize("12.345"), ridget.getText());

		String controlLastText = control.getText();
		String ridgetLastText = ridget.getText();
		try {
			ridget.setText("abc");
			fail();
		} catch (RuntimeException rex) {
			assertEquals(controlLastText, control.getText());
			assertEquals(ridgetLastText, ridget.getText());
		}
	}

	/**
	 * Test that setText(x) with x = {"", null } clears the number.
	 */
	public void testSetTextNull() {
		ITextRidget ridget = getRidget();
		Text control = getWidget();

		String[] testValues = new String[] { null, "" };

		for (String value : testValues) {
			ridget.setText(localize("42,2"));

			assertEquals(localize("42,2"), ridget.getText());

			DoubleBean doubleBean = new DoubleBean(3.14d);
			ridget.bindToModel(doubleBean, DoubleBean.PROP_VALUE);
			ridget.setText(value);

			String msg = String.format("setText('%s')", value);

			assertEquals(msg, "", ridget.getText());
			assertEquals(msg, localize(","), control.getText());
			assertEquals(msg, null, doubleBean.getValue());
		}
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

		assertEquals(localize("0,9876"), ridget.getText());
		assertEquals(1, control.getCaretPosition());

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

		assertEquals(2, ridget.getPrecision());
		assertEquals(localize("0,00"), control.getText());
		assertEquals("0", ridget.getText());

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

	public void testUpdateFromModelNull() {
		IDecimalTextRidget ridget = getRidget();
		Text control = getWidget();

		assertEquals(2, ridget.getPrecision());

		ridget.setText(localize("3,14"));
		DoubleBean bean = new DoubleBean(null);
		ridget.bindToModel(bean, StringBean.PROP_VALUE);
		ridget.updateFromModel();

		assertEquals(localize(""), ridget.getText());
		assertEquals(localize(","), control.getText());
		assertEquals(null, bean.getValue());
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

	public void testExceedPrecisionWithSetText() {
		IDecimalTextRidget ridget = getRidget();
		Text control = getWidget();

		assertEquals(2, ridget.getPrecision());

		ridget.setText(localize("1,23"));

		try {
			ridget.setText(localize("3,145"));
			fail();
		} catch (RuntimeException rex) {
			// expected
			assertEquals(localize("1,23"), ridget.getText());
			assertEquals(localize("1,23"), control.getText());
		}
	}

	public void testExceedPrecisionWithUpdate() {
		IDecimalTextRidget ridget = getRidget();
		Text control = getWidget();
		DoubleBean value = new DoubleBean(1.23d);
		ridget.bindToModel(value, DoubleBean.PROP_VALUE);

		assertEquals(2, ridget.getPrecision());

		ridget.updateFromModel();

		try {
			value.setValue(3.145d);
			ridget.updateFromModel();
			fail();
		} catch (RuntimeException rex) {
			// expected
			assertEquals(localize("1,23"), ridget.getText());
			assertEquals(localize("1,23"), control.getText());
		}
	}

	public void testExceedMaxLengthWithSetText() {
		IDecimalTextRidget ridget = getRidget();
		Text control = getWidget();

		ridget.setMaxLength(3);

		assertEquals(3, ridget.getMaxLength());

		ridget.setText(localize("123,00"));

		try {
			ridget.setText(localize("1234,00"));
			fail();
		} catch (RuntimeException rex) {
			// expected
			assertEquals(localize("123"), ridget.getText());
			assertEquals(localize("123,00"), control.getText());
		}

		ridget.setText(localize("321"));
		try {
			ridget.setText(localize("1234"));
			fail();
		} catch (RuntimeException rex) {
			// expected
			assertEquals(localize("321"), ridget.getText());
			assertEquals(localize("321,00"), control.getText());
		}
	}

	public void testExceedMaxLengthWithUpdate() {
		IDecimalTextRidget ridget = getRidget();
		Text control = getWidget();

		ridget.setMaxLength(3);

		assertEquals(3, ridget.getMaxLength());

		DoubleBean value = new DoubleBean(123.00d);
		ridget.bindToModel(value, DoubleBean.PROP_VALUE);
		ridget.updateFromModel();

		try {
			value.setValue(1234.12d);
			ridget.updateFromModel();
			fail();
		} catch (RuntimeException rex) {
			// expected
			assertEquals(localize("123"), ridget.getText());
			assertEquals(localize("123,00"), control.getText());
		}

		value.setValue(321d);
		ridget.updateFromModel();
		try {
			value.setValue(1234d);
			ridget.updateFromModel();
			fail();
		} catch (RuntimeException rex) {
			// expected
			assertEquals(localize("321"), ridget.getText());
			assertEquals(localize("321,00"), control.getText());
		}
	}

	public void testIsSetWithSign() {
		IDecimalTextRidget ridget = getRidget();
		Text control = getWidget();
		ridget.setPrecision(3);
		StringBean bean = new StringBean();
		ridget.bindToModel(bean, StringBean.PROP_VALUE);

		assertTrue(ridget.isSigned());

		control.setFocus();
		UITestHelper.sendString(control.getDisplay(), "1-\r");

		assertEquals(localize("-1,000"), control.getText());
		assertEquals(localize("-1"), ridget.getText());
		assertEquals(localize("-1"), bean.getValue());

		control.selectAll();
		UITestHelper.sendString(control.getDisplay(), "0-\r");

		assertEquals(localize("0,000"), control.getText());
		assertEquals(localize("0"), ridget.getText());
		assertEquals(localize("0"), bean.getValue());

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
		UITestHelper.sendString(control.getDisplay(), localize("\b,0\t"));

		assertEquals(localize("0,000"), control.getText());
		assertEquals(localize("0"), ridget.getText());
		assertEquals(localize("0"), bean.getValue());

		ridget.setText("1");
		control.setFocus();
		UITestHelper.sendString(control.getDisplay(), localize("\b0\t"));

		assertEquals(localize("0,000"), control.getText());
		assertEquals(localize("0"), ridget.getText());
		assertEquals(localize("0"), bean.getValue());

		control.setFocus();
		UITestHelper.sendString(control.getDisplay(), localize("\b,\t"));

		assertEquals(localize(","), control.getText());
		assertEquals(localize(""), ridget.getText());
		assertEquals("", bean.getValue());

		control.setFocus();
		UITestHelper.sendString(control.getDisplay(), localize("\b,-\t"));

		assertEquals(localize(","), control.getText());
		assertEquals(localize(""), ridget.getText());
		assertEquals(localize(""), bean.getValue());
	}

	public void testMandatoryMarkerFromUI() {
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

	public void testMandatoryMarker() {
		IDecimalTextRidget ridget = getRidget();
		ridget.setMandatory(true);

		ridget.setText(localize("12,34"));

		TestUtils.assertMandatoryMarker(ridget, 1, true);

		ridget.setText(null);

		TestUtils.assertMandatoryMarker(ridget, 1, false);

		ridget.setMandatory(false);

		TestUtils.assertMandatoryMarker(ridget, 0, false);
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

	public void testValidationAfterUpdate() {
		IDecimalTextRidget ridget = getRidget();
		ridget.addValidationRule(new IValidator() {
			public IStatus validate(Object input) {
				IStatus result = ValidationRuleStatus.ok();
				try {
					double value = Double.parseDouble(input.toString());
					if (value == 0d) {
						result = ValidationRuleStatus.error(false, "cannot be 0");
					}
				} catch (NumberFormatException nfe) {
					result = ValidationRuleStatus.error(false, "number format exception");
				}
				return result;
			}

		}, ValidationTime.ON_UPDATE_TO_MODEL);

		assertFalse(ridget.isErrorMarked());

		ridget.revalidate();

		assertTrue(ridget.isErrorMarked());

		DoubleBean bean = new DoubleBean(0d);
		ridget.bindToModel(bean, DoubleBean.PROP_VALUE);
		ridget.updateFromModel();

		assertTrue(ridget.isErrorMarked());
	}

	public void testRemoveLeadingCruft() {
		assertEquals(localize("0,"), NumericTextRidget.removeLeadingCruft(localize("0,")));
		assertEquals(localize("0,"), NumericTextRidget.removeLeadingCruft(localize("00,")));
		assertEquals(localize("1,"), NumericTextRidget.removeLeadingCruft(localize("001,")));
		assertEquals(localize(","), NumericTextRidget.removeLeadingCruft(localize("-,")));
		assertEquals(localize("0,"), NumericTextRidget.removeLeadingCruft(localize("-0,")));
		assertEquals(localize("0,"), NumericTextRidget.removeLeadingCruft(localize("-00,")));
		assertEquals(localize("-10,"), NumericTextRidget.removeLeadingCruft(localize("-0010,")));
	}

	/**
	 * As per bug #275134.
	 */
	public void testSetSignedThrowsException() {
		INumericTextRidget ridget = getRidget();
		ridget.setText(localize("1234,56"));

		ridget.setSigned(false);

		try {
			ridget.setText(localize("-47,11"));
			fail();
		} catch (RuntimeException exc) {
			ok("expected");
		}
		assertEquals(localize("1.234,56"), ridget.getText());
	}

	/**
	 * As per bug #280603.
	 */
	public void testSetPrecisionBeforeBindAndUpdateFromModel() {
		IDecimalTextRidget ridget = getRidget();
		Text control = getWidget();

		DoubleBean doubleBean = new DoubleBean(3.141592d);
		ridget.setPrecision(6);
		ridget.bindToModel(doubleBean, DoubleBean.PROP_VALUE);
		ridget.updateFromModel();

		assertEquals(localize("3,141592"), ridget.getText());
		assertEquals(localize("3,141592"), control.getText());
	}

	/**
	 * As per bug #280603.
	 */
	public void testSetPrecisionAfterBindAndUpdateFromModel() {
		IDecimalTextRidget ridget = getRidget();
		Text control = getWidget();

		DoubleBean doubleBean = new DoubleBean(0.123456789d);
		ridget.bindToModel(doubleBean, DoubleBean.PROP_VALUE);
		ridget.setPrecision(9);
		ridget.updateFromModel();

		assertEquals(localize("0,123456789"), ridget.getText());
		assertEquals(localize("0,123456789"), control.getText());
	}

	// helping methods
	//////////////////

	private String localize(String number) {
		return TestUtils.getLocalizedNumber(number);
	}

}
