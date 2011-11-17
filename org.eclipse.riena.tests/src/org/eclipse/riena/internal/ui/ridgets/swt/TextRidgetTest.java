/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;

/**
 * Tests for the class {@link TextRidget}.
 * 
 * @see TextRidgetTest2
 */
public class TextRidgetTest extends AbstractSWTRidgetTest {

	@Override
	protected IRidget createRidget() {
		return new TextRidget();
	}

	@Override
	protected Control createWidget(final Composite parent) {
		return new Text(getShell(), SWT.NONE);
	}

	@Override
	protected ITextRidget getRidget() {
		return (ITextRidget) super.getRidget();
	}

	@Override
	protected Text getWidget() {
		return (Text) super.getWidget();
	}

	public void testRidgetMapping() {
		final SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertSame(TextRidget.class, mapper.getRidgetClass(getWidget()));
	}

	public void testCreate() throws Exception {
		assertFalse(getRidget().isDirectWriting());
		assertEquals("", getRidget().getText());
	}

	public void testSetUIControl() throws Exception {
		assertSame(getWidget(), getRidget().getUIControl());
	}

	public void testSetText() throws Exception {
		final TextPojo model = new TextPojo();
		final IObservableValue modelOV = PojoObservables.observeValue(model, "text1");
		final ITextRidget ridget = getRidget();
		ridget.bindToModel(modelOV);

		ridget.setText("first");
		assertEquals("first", ridget.getText());
		assertEquals("first", model.getText1());
		assertEquals("first", getWidget().getText());
	}

	/**
	 * Tests that setText(null) clears the ridget (i.e. resuls in "")
	 */
	public void testSetTextNull() {
		final ITextRidget ridget = getRidget();

		ridget.setText("huhu");

		assertTrue(ridget.getText().length() > 0);

		ridget.setText(null);

		assertEquals("", ridget.getText());
	}

	public void testGetText() throws Exception {
		final ITextRidget ridget = getRidget();

		assertEquals("", ridget.getText());

		final TextPojo model = new TextPojo();
		final IObservableValue modelOV = PojoObservables.observeValue(model, "text2");
		ridget.bindToModel(modelOV);

		assertEquals("", ridget.getText());

		ridget.updateFromModel();

		assertEquals(model.getText2(), ridget.getText());
	}

	public void testBindToModelIObservableValue() throws Exception {
		final ITextRidget ridget = getRidget();

		final TextPojo model = new TextPojo();
		final IObservableValue modelOV = PojoObservables.observeValue(model, "text1");
		ridget.bindToModel(modelOV);

		assertEquals("", ridget.getText());

		ridget.updateFromModel();

		assertEquals(model.getText1(), ridget.getText());
	}

	public void testBindToModelPropertyName() throws Exception {
		final ITextRidget ridget = getRidget();

		final TextPojo model = new TextPojo();
		ridget.bindToModel(model, "text2");

		assertEquals("", ridget.getText());

		ridget.updateFromModel();

		assertEquals(model.getText2(), ridget.getText());
	}

	public void testUpdateFromModel() throws Exception {
		final ITextRidget ridget = getRidget();

		final TextPojo model = new TextPojo();
		ridget.bindToModel(model, "text2");
		final String newText = "second";
		model.setText2(newText);
		ridget.updateFromModel();
		assertEquals(newText, getWidget().getText());
	}

	public void testFocusGainedDoesSelectOnSingleText() {
		final ITextRidget ridget = getRidget();
		final Text control = getWidget();

		ridget.setText("foo");
		control.setSelection(0, 0);

		assertEquals("", control.getSelectionText());

		final Event e = new Event();
		e.type = SWT.FocusIn;
		e.widget = control;
		e.widget.notifyListeners(e.type, e);

		assertEquals(0, control.getStyle() & SWT.MULTI);
		assertEquals("foo", control.getSelectionText());
	}

	public void testFocusGainedDoesNotSelectOnMultiLineText() {
		final ITextRidget ridget = getRidget();
		final Text control = new Text(getShell(), SWT.MULTI);
		ridget.setUIControl(control);

		ridget.setText("line 1\nline 2");
		control.setSelection(0, 0);

		assertEquals("", control.getSelectionText());

		final Event e = new Event();
		e.type = SWT.FocusIn;
		e.widget = control;
		e.widget.notifyListeners(e.type, e);

		assertFalse((control.getStyle() & SWT.MULTI) == 0);
		assertEquals("", control.getSelectionText());
	}

	/**
	 * As per Bug 315691
	 */
	public void testSetOutputPreservesBackground() {
		final ITextRidget ridget = getRidget();
		final Text control = getWidget();

		final Color oldBg = control.getBackground();

		ridget.setEnabled(false);
		ridget.setOutputOnly(true);
		ridget.setEnabled(true);
		ridget.setOutputOnly(false);

		assertEquals(oldBg, control.getBackground());
	}

	/**
	 * Tests if all nested properties of a <b>bean</b> will be observed by the
	 * JFace data binding.
	 * <p>
	 * Note: All involved value holder must be beans. With pojos a rebind to the
	 * model will be necessary.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testBindToModelNestedPropertyName() throws Exception {
		final ITextRidget ridget = getRidget();

		final TextBeanHolder modelHolder = new TextBeanHolder();
		final TextBean model = new TextBean();
		modelHolder.setBean(model);
		ridget.bindToModel(modelHolder, "bean.text2");

		assertEquals("", ridget.getText());

		ridget.updateFromModel();

		assertEquals(modelHolder.getBean().getText2(), ridget.getText());

		final TextBean model2 = new TextBean();
		model2.setText2("three");
		modelHolder.setBean(model2);
		ridget.updateFromModel();

		assertEquals(modelHolder.getBean().getText2(), ridget.getText());
	}

	// helping classes
	//////////////////

	/**
	 * An object holding two strings.
	 */
	private static class TextPojo {

		private String text1;
		private String text2;

		public TextPojo() {
			text1 = "one";
			text2 = "two";
		}

		public String getText1() {
			return text1;
		}

		@SuppressWarnings("unused")
		public void setText1(final String text1) {
			this.text1 = text1;
		}

		public String getText2() {
			return text2;
		}

		public void setText2(final String text2) {
			this.text2 = text2;
		}

	}

	/**
	 * A bean holding two strings.
	 */
	private static class TextBean {

		private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

		private String text1;
		private String text2;

		public TextBean() {
			text1 = "one";
			text2 = "two";
		}

		public String getText1() {
			return text1;
		}

		@SuppressWarnings("unused")
		public void setText1(final String text1) {
			propertyChangeSupport.firePropertyChange("text1", this.text1, this.text1 = text1);
		}

		public String getText2() {
			return text2;
		}

		public void setText2(final String text2) {
			propertyChangeSupport.firePropertyChange("text2", this.text2, this.text2 = text2);
		}

		public void removePropertyChangeListener(final PropertyChangeListener listener) {
			propertyChangeSupport.removePropertyChangeListener(listener);
		}

		public void addPropertyChangeListener(final PropertyChangeListener listener) {
			propertyChangeSupport.addPropertyChangeListener(listener);
		}

	}

	private class TextBeanHolder {

		private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

		private TextBean bean;

		public TextBean getBean() {
			return bean;
		}

		public void setBean(final TextBean bean) {
			propertyChangeSupport.firePropertyChange("bean", this.bean, this.bean = bean);
		}

		public void removePropertyChangeListener(final PropertyChangeListener listener) {
			propertyChangeSupport.removePropertyChangeListener(listener);
		}

		public void addPropertyChangeListener(final PropertyChangeListener listener) {
			propertyChangeSupport.addPropertyChangeListener(listener);
		}
	}

}
