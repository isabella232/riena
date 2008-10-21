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

import org.eclipse.riena.ui.ridgets.IDateTextFieldRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtControlRidgetMapper;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * Tests for the class {@link DecimalTextRidget}.
 */
// TODO [ev] add to test suite
public class DateTextRidgetTest extends AbstractSWTRidgetTest {

	@Override
	protected IRidget createRidget() {
		return new DateTextRidget();
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

}
