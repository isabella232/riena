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

import org.eclipse.riena.tests.TestUtils;
import org.eclipse.riena.tests.UITestHelper;
import org.eclipse.riena.ui.ridgets.IDecimalValueTextFieldRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.util.beans.DoubleBean;
import org.eclipse.riena.ui.ridgets.util.beans.StringBean;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * Tests for the class {@link DecimalTextRidget}.
 */
public class DecimalTextRidgetTest extends NumericTextRidgetTest {

	// TODO [ev] decide if we really need to extend NumericTextRidgetTest
	// TODO [ev] add to test suite

	@Override
	protected IRidget createRidget() {
		return new DecimalTextRidget();
	}

	@Override
	protected IDecimalValueTextFieldRidget getRidget() {
		return (IDecimalValueTextFieldRidget) super.getRidget();
	}

	@Override
	protected Control createUIControl(Composite parent) {
		Control result = new Text(getShell(), SWT.RIGHT | SWT.BORDER | SWT.SINGLE);
		result.setData(UIControlsFactory.KEY_TYPE, UIControlsFactory.TYPE_DECIMAL);
		result.setLayoutData(new RowData(100, SWT.DEFAULT));
		return result;
	}

	// test methods
	///////////////

	@Override
	public void testRidgetMapping() {
		DefaultSwtControlRidgetMapper mapper = new DefaultSwtControlRidgetMapper();
		assertSame(DecimalTextRidget.class, mapper.getRidgetClass(getUIControl()));
	}

	public void testDoubleValueProviderAndHighNumbers() {
		final DoubleBean doubleValueBean = new DoubleBean() {
			public Double getValue() {
				return 1000000000000000.0;
			}
		};

		IDecimalValueTextFieldRidget ridget = getRidget();
		ridget.setMaxLength(6);
		ridget.setPrecision(3);
		ridget.bindToModel(doubleValueBean, DoubleBean.PROP_VALUE);
		ridget.updateFromModel();

		assertEquals(TestUtils.getLocalizedNumber("1.000.000.000.000.000"), ridget.getText());
	}

	public void testDoubleValueProviderAndHighNumbersB() {
		final DoubleBean doubleValueBean = new DoubleBean() {
			public Double getValue() {
				return 1000000000000000.0;
			}
		};

		IDecimalValueTextFieldRidget ridget = getRidget();
		ridget.setMaxLength(16);
		ridget.setPrecision(3);
		ridget.bindToModel(doubleValueBean, DoubleBean.PROP_VALUE);
		ridget.updateFromModel();

		assertEquals(TestUtils.getLocalizedNumber("1.000.000.000.000.000"), ridget.getText());
		assertEquals(TestUtils.getLocalizedNumber("1.000.000.000.000.000,000"), getUIControl().getText());
	}

	@Override
	public void testUpdateFromModel() {
		IDecimalValueTextFieldRidget ridget = getRidget();
		Text control = getUIControl();

		ridget.setMaxLength(6);
		ridget.setPrecision(3);

		StringBean bean = new StringBean(TestUtils.getLocalizedNumber("1,2"));
		ridget.bindToModel(bean, StringBean.PROP_VALUE);
		ridget.updateFromModel();

		// Test initial values
		assertEquals(6, ridget.getMaxLength());
		assertEquals(3, ridget.getPrecision());
		assertEquals(TestUtils.getLocalizedNumber("1,200"), control.getText());
		assertEquals(TestUtils.getLocalizedNumber("1,2"), ridget.getText());
		assertEquals(TestUtils.getLocalizedNumber("1,2"), bean.getValue());

		// Test with bean value 0.0
		bean.setValue(TestUtils.getLocalizedNumber("0,0"));
		ridget.updateFromModel();

		assertEquals(TestUtils.getLocalizedNumber("0,000"), control.getText());
		assertEquals(TestUtils.getLocalizedNumber("0,0"), ridget.getText());
		assertEquals(TestUtils.getLocalizedNumber("0,0"), bean.getValue());
	}

	public void testMaxLength() {
		IDecimalValueTextFieldRidget ridget = getRidget();
		Text control = getUIControl();
		ridget.setMaxLength(6);
		ridget.setPrecision(3);
		StringBean bean = new StringBean();
		ridget.bindToModel(bean, StringBean.PROP_VALUE);

		control.setFocus();
		UITestHelper.sendString(control.getDisplay(), "12345612345\r");

		assertEquals(TestUtils.getLocalizedNumber("123.456,123"), control.getText());
		assertEquals(TestUtils.getLocalizedNumber("123.456,123"), ridget.getText());
		assertEquals(TestUtils.getLocalizedNumber("123.456,123"), bean.getValue());

		ridget.setMaxLength(3);
		// TODO [ev] what happens here?
	}

	public void testGetSetMaxLength() {
		IDecimalValueTextFieldRidget ridget = getRidget();

		try {
			ridget.setMaxLength(0);
			fail();
		} catch (RuntimeException rex) {
			// expected
		}

		try {
			ridget.setMaxLength(-1);
			fail();
		} catch (RuntimeException rex) {
			// expected
		}

		expectPropertyChangeEvent(IDecimalValueTextFieldRidget.PROPERTY_MAXLENGTH, Integer.valueOf(10), Integer
				.valueOf(5));
		ridget.setMaxLength(5);

		verifyPropertyChangeEvents();
		assertEquals(5, ridget.getMaxLength());

		expectNoPropertyChangeEvent();
		ridget.setMaxLength(5);

		verifyPropertyChangeEvents();
	}

	public void testPrecision() {
		IDecimalValueTextFieldRidget ridget = getRidget();
		Text control = getUIControl();
		ridget.setMaxLength(6);
		ridget.setPrecision(3);
		StringBean bean = new StringBean();
		ridget.bindToModel(bean, StringBean.PROP_VALUE);

		control.setFocus();
		UITestHelper.sendString(control.getDisplay(), "123456\r");

		assertEquals(TestUtils.getLocalizedNumber("123.456,000"), control.getText());
		assertEquals(TestUtils.getLocalizedNumber("123.456,000"), ridget.getText());
		assertEquals(TestUtils.getLocalizedNumber("123.456,000"), bean.getValue());

		control.setFocus();
		control.setSelection(control.getText().length());
		UITestHelper.sendString(control.getDisplay(), "54321\r");

		assertEquals(TestUtils.getLocalizedNumber("123.456,543"), control.getText());
		assertEquals(TestUtils.getLocalizedNumber("123.456,543"), ridget.getText());
		assertEquals(TestUtils.getLocalizedNumber("123.456,543"), bean.getValue());

		ridget.setPrecision(2);

		assertEquals(TestUtils.getLocalizedNumber("123.456,54"), control.getText());
		assertEquals(TestUtils.getLocalizedNumber("123.456,54"), ridget.getText());
		assertEquals(TestUtils.getLocalizedNumber("123.456,54"), bean.getValue());

		ridget.setPrecision(0);

		assertEquals(TestUtils.getLocalizedNumber("123.456,"), control.getText());
		assertEquals(TestUtils.getLocalizedNumber("123.456,"), ridget.getText());
		assertEquals(TestUtils.getLocalizedNumber("123.456,"), bean.getValue());

		ridget.setPrecision(2);
		// TODO [ev] what happens here?
	}

	public void testGetSetPrecision() {
		IDecimalValueTextFieldRidget ridget = getRidget();

		try {
			ridget.setPrecision(-1);
			fail();
		} catch (RuntimeException rex) {
			// expected
		}

		expectPropertyChangeEvent(IDecimalValueTextFieldRidget.PROPERTY_PRECISION, Integer.valueOf(2), Integer
				.valueOf(5));
		ridget.setPrecision(5);

		verifyPropertyChangeEvents();
		assertEquals(5, ridget.getPrecision());

		expectNoPropertyChangeEvent();
		ridget.setPrecision(5);

		verifyPropertyChangeEvents();

		expectPropertyChangeEvent(IDecimalValueTextFieldRidget.PROPERTY_PRECISION, Integer.valueOf(5), Integer
				.valueOf(0));
		ridget.setPrecision(0);

		verifyPropertyChangeEvents();
		assertEquals(0, ridget.getPrecision());
	}

	public void testIsSetWithSign() {
		IDecimalValueTextFieldRidget ridget = getRidget();
		Text control = getUIControl();
		ridget.setPrecision(3);
		StringBean bean = new StringBean();
		ridget.bindToModel(bean, StringBean.PROP_VALUE);

		assertFalse(ridget.isSigned());

		ridget.setSigned(true);

		assertTrue(ridget.isSigned());

		control.setFocus();
		UITestHelper.sendString(control.getDisplay(), "1-\n");

		assertEquals(TestUtils.getLocalizedNumber("-1,000"), control.getText());
		assertEquals(TestUtils.getLocalizedNumber("-1,000"), ridget.getText());
		assertEquals(TestUtils.getLocalizedNumber("-1,000"), bean.getValue());

		ridget.setSigned(false);

		assertFalse(ridget.isSigned());

		control.setFocus();
		UITestHelper.sendString(control.getDisplay(), "1-\n");

		assertEquals(TestUtils.getLocalizedNumber("1,000"), control.getText());
		assertEquals(TestUtils.getLocalizedNumber("1,000"), ridget.getText());
		assertEquals(TestUtils.getLocalizedNumber("1,000"), bean.getValue());
	}

}
