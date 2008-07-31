/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.riena.navigation.ui.swt.binding.DefaultSwtControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;

/**
 * Tests of the class <code>TextFieldRidget</code>.
 * 
 */
public class TextRidgetTest extends AbstractSWTRidgetTest {

	@Override
	protected IRidget createRidget() {
		return new TextRidget();
	}

	@Override
	protected Control createUIControl(Composite parent) {
		return new Text(getShell(), SWT.NONE);
	}

	@Override
	protected ITextFieldRidget getRidget() {
		return (ITextFieldRidget) super.getRidget();
	}

	@Override
	protected Text getUIControl() {
		return (Text) super.getUIControl();
	}

	public void testRidgetMapping() {
		DefaultSwtControlRidgetMapper mapper = new DefaultSwtControlRidgetMapper();
		assertSame(TextRidget.class, mapper.getRidgetClass(getUIControl()));
	}

	public void testCreate() throws Exception {
		assertFalse(getRidget().isDirectWriting());
		assertEquals("", getRidget().getText());
	}

	public void testSetUIControl() throws Exception {
		assertSame(getUIControl(), getRidget().getUIControl());
	}

	public void testSetText() throws Exception {
		TextBean model = new TextBean();
		IObservableValue modelOV = BeansObservables.observeValue(model, "text1");
		ITextFieldRidget ridget = getRidget();
		ridget.bindToModel(modelOV);

		ridget.setText("first");
		assertEquals("first", ridget.getText());
		assertEquals("first", model.getText1());
		assertEquals("first", getUIControl().getText());
	}

	public void testGetText() throws Exception {
		ITextFieldRidget ridget = getRidget();

		assertEquals("", ridget.getText());

		TextBean model = new TextBean();
		IObservableValue modelOV = BeansObservables.observeValue(model, "text2");
		ridget.bindToModel(modelOV);

		assertEquals("", ridget.getText());

		ridget.updateFromModel();

		assertEquals(model.getText2(), ridget.getText());
	}

	public void testBindToModelIObservableValue() throws Exception {
		ITextFieldRidget ridget = getRidget();

		TextBean model = new TextBean();
		IObservableValue modelOV = BeansObservables.observeValue(model, "text1");
		ridget.bindToModel(modelOV);

		assertEquals("", ridget.getText());

		ridget.updateFromModel();

		assertEquals(model.getText1(), ridget.getText());
	}

	public void testBindToModelPropertyName() throws Exception {
		ITextFieldRidget ridget = getRidget();

		TextBean model = new TextBean();
		ridget.bindToModel(model, "text2");

		assertEquals("", ridget.getText());

		ridget.updateFromModel();

		assertEquals(model.getText2(), ridget.getText());
	}

	public void testUpdateFromModel() throws Exception {
		ITextFieldRidget ridget = getRidget();

		TextBean model = new TextBean();
		ridget.bindToModel(model, "text2");
		String newText = "second";
		model.setText2(newText);
		ridget.updateFromModel();
		assertEquals(newText, getUIControl().getText());
	}

	public void testFocusGainedDoesSelectOnSingleText() {
		ITextFieldRidget ridget = getRidget();
		Text control = getUIControl();

		ridget.setText("foo");
		control.setSelection(0, 0);

		assertEquals("", control.getSelectionText());

		Event e = new Event();
		e.type = SWT.FocusIn;
		e.widget = control;
		e.widget.notifyListeners(e.type, e);

		assertEquals(0, control.getStyle() & SWT.MULTI);
		assertEquals("foo", control.getSelectionText());
	}

	public void testFocusGainedDoesNotSelectOnMultiLineText() {
		ITextFieldRidget ridget = getRidget();
		Text control = new Text(getShell(), SWT.MULTI);
		ridget.setUIControl(control);

		ridget.setText("line 1\nline 2");
		control.setSelection(0, 0);

		assertEquals("", control.getSelectionText());

		Event e = new Event();
		e.type = SWT.FocusIn;
		e.widget = control;
		e.widget.notifyListeners(e.type, e);

		assertFalse((control.getStyle() & SWT.MULTI) == 0);
		assertEquals("", control.getSelectionText());
	}

	/**
	 * POJO-Bean with two strings.
	 */
	private class TextBean {
		private String text1;
		private String text2;

		public TextBean() {
			text1 = "one";
			text2 = "two";
		}

		public String getText1() {
			return text1;
		}

		public void setText1(String text1) {
			this.text1 = text1;
		}

		public String getText2() {
			return text2;
		}

		public void setText2(String text2) {
			this.text2 = text2;
		}
	}

}
