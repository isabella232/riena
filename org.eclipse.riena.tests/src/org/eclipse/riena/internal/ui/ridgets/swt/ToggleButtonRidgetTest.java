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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.tests.FTActionListener;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtControlRidgetMapper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Tests of the class <code>ToggleButtonRidget</code>.
 * 
 */
public class ToggleButtonRidgetTest extends AbstractSWTRidgetTest {

	private final static String PLUGIN_ID = "org.eclipse.riena.ui.tests:";
	private final static String ICON_ECLIPSE = PLUGIN_ID + "/icons/eclipse.gif";
	private final static String LABEL = "testlabel";
	private final static String LABEL2 = "testlabel2";

	@Override
	protected IRidget createRidget() {
		return new ToggleButtonRidget();
	}

	@Override
	protected Control createUIControl(Composite parent) {
		return new Button(parent, SWT.CHECK);
	}

	@Override
	protected IToggleButtonRidget getRidget() {
		return (IToggleButtonRidget) super.getRidget();
	}

	@Override
	protected Button getUIControl() {
		return (Button) super.getUIControl();
	}

	public void testRidgetMapping() {
		Shell shell = getShell();

		DefaultSwtControlRidgetMapper mapper = new DefaultSwtControlRidgetMapper();

		Button buttonToggle = new Button(shell, SWT.TOGGLE);
		assertSame(ToggleButtonRidget.class, mapper.getRidgetClass(buttonToggle));

		Button buttonCheck = new Button(shell, SWT.CHECK);
		assertSame(ToggleButtonRidget.class, mapper.getRidgetClass(buttonCheck));

		Button buttonPush = new Button(shell, SWT.PUSH);
		assertNotSame(ToggleButtonRidget.class, mapper.getRidgetClass(buttonPush));

		Button aButton = new Button(shell, SWT.NONE);
		assertNotSame(ToggleButtonRidget.class, mapper.getRidgetClass(aButton));
	}

	public void testSetUIControl() throws Exception {
		IToggleButtonRidget ridget = getRidget();
		Button button = getUIControl();

		assertSame(button, ridget.getUIControl());
	}

	public void testSetSelected() throws Exception {
		IToggleButtonRidget ridget = getRidget();
		Button button = getUIControl();
		BooleanTestBean model = new BooleanTestBean();
		IObservableValue modelOV = BeansObservables.observeValue(model, "selected");
		ridget.bindToModel(modelOV);

		ridget.setSelected(true);

		assertTrue(ridget.isSelected());
		assertTrue(button.getSelection());

		ridget.setSelected(false);

		assertFalse(ridget.isSelected());
		assertFalse(button.getSelection());
	}

	public void testIsSelected() throws Exception {
		IToggleButtonRidget ridget = getRidget();

		BooleanTestBean model = new BooleanTestBean();
		model.setSelected(true);
		IObservableValue modelOV = BeansObservables.observeValue(model, "selected");
		ridget.bindToModel(modelOV);
		ridget.updateFromModel();

		assertTrue(ridget.isSelected());
	}

	public void testBindToModelIObservableValue() throws Exception {
		IToggleButtonRidget ridget = getRidget();

		BooleanTestBean model = new BooleanTestBean();
		model.setSelected(true);
		IObservableValue modelOV = BeansObservables.observeValue(model, "selected");
		ridget.bindToModel(modelOV);

		assertNotNull(BeansObservables.observeValue(ridget, IToggleButtonRidget.PROPERTY_SELECTED));
		assertEquals(boolean.class, BeansObservables.observeValue(ridget, IToggleButtonRidget.PROPERTY_SELECTED)
				.getValueType());
		assertFalse(ridget.isSelected());

		ridget.updateFromModel();

		assertTrue(ridget.isSelected());
	}

	public void testBindToModelPropertyName() throws Exception {
		IToggleButtonRidget ridget = getRidget();

		BooleanTestBean model = new BooleanTestBean();
		model.setSelected(true);
		ridget.bindToModel(model, "selected");

		assertNotNull(BeansObservables.observeValue(ridget, IToggleButtonRidget.PROPERTY_SELECTED));
		assertEquals(boolean.class, BeansObservables.observeValue(ridget, IToggleButtonRidget.PROPERTY_SELECTED)
				.getValueType());
		assertFalse(ridget.isSelected());

		ridget.updateFromModel();

		assertTrue(ridget.isSelected());
	}

	public void testUpdateFromModel() throws Exception {
		IToggleButtonRidget ridget = getRidget();
		Button button = getUIControl();

		BooleanTestBean model = new BooleanTestBean();
		model.setSelected(true);
		ridget.bindToModel(model, "selected");
		ridget.updateFromModel();

		assertTrue(button.getSelection());

		model.setSelected(false);
		ridget.updateFromModel();
		assertFalse(button.getSelection());
	}

	public void testActionListener() {
		IToggleButtonRidget ridget = getRidget();

		ridget.setSelected(false);

		FTActionListener listener = new FTActionListener();
		ridget.addListener(listener);
		ridget.setSelected(true);

		assertEquals(1, listener.getCount());

		ridget.setSelected(true);

		assertEquals(1, listener.getCount());

		ridget.setSelected(false);

		assertEquals(2, listener.getCount());

		ridget.removeListener(listener);
		ridget.setSelected(true);

		assertEquals(2, listener.getCount());

		try {
			ridget.addListener(null);
			fail();
		} catch (RuntimeException rex) {
			// expected
		}
	}

	public final void testSetText() throws Exception {
		IToggleButtonRidget ridget = getRidget();
		Button button = getUIControl();

		ridget.setText("");

		assertEquals("", ridget.getText());
		assertEquals("", button.getText());

		try {
			ridget.setText(null);
			fail();
		} catch (IllegalArgumentException iae) {
			// expected
		}

		ridget.setText(LABEL);

		assertEquals(LABEL, ridget.getText());
		assertEquals(LABEL, button.getText());

		ridget.setUIControl(null);
		ridget.setText(LABEL2);

		assertEquals(LABEL2, ridget.getText());
		assertEquals(LABEL, button.getText());

		ridget.setUIControl(button);

		assertEquals(LABEL2, ridget.getText());
		assertEquals(LABEL2, button.getText());
	}

	/**
	 * Test method get/setIcon().
	 */
	public final void testSetIcon() {
		IToggleButtonRidget ridget = getRidget();
		Button control = getUIControl();

		ridget.setIcon(ICON_ECLIPSE);

		assertEquals(ICON_ECLIPSE, ridget.getIcon());
		assertNotNull(control.getImage());

		ridget.setIcon(null);

		assertNull(ridget.getIcon());
		assertNull(control.getImage());

		ridget.setIcon("nonsense");

		Image missingImage = ReflectionUtils.invokeHidden(ridget, "getMissingImage", new Object[] {});
		assertEquals("nonsense", ridget.getIcon());
		assertEquals(missingImage, control.getImage());

	}

	/**
	 * Tests the method {@code initText}
	 */
	public final void testInitText() {
		IToggleButtonRidget ridget = getRidget();
		Button control = getUIControl();

		ReflectionUtils.setHidden(ridget, "textAlreadyInitialized", false);
		ReflectionUtils.setHidden(ridget, "text", null);
		control.setText("Hello!");

		ReflectionUtils.invokeHidden(ridget, "initText", new Object[] {});
		assertEquals("Hello!", ridget.getText());
		assertEquals("Hello!", control.getText());
		assertTrue((Boolean) ReflectionUtils.getHidden(ridget, "textAlreadyInitialized"));

		control.setText("World");
		ReflectionUtils.invokeHidden(ridget, "initText", new Object[] {});
		assertEquals("Hello!", ridget.getText());
		assertEquals("World", control.getText());

	}

	/**
	 * Tests that control is hidden when the ToggleButtonRidget is
	 * "output only".
	 */
	public void testOutputRidgetNotVisible() {
		IToggleButtonRidget ridget = getRidget();
		Button control = getUIControl();

		assertFalse(ridget.isOutputOnly());
		assertTrue(control.isVisible());

		ridget.setOutputOnly(true);

		assertTrue(ridget.isOutputOnly());
		assertFalse(control.isVisible());

		ridget.setOutputOnly(false);

		assertFalse(ridget.isOutputOnly());
		assertTrue(control.isVisible());

		ridget.setOutputOnly(true);
		ridget.setVisible(true);

		assertTrue(ridget.isOutputOnly());
		assertTrue(ridget.isVisible());
		assertFalse(control.isVisible());

		ridget.setVisible(false);
		ridget.setOutputOnly(false);

		assertFalse(ridget.isOutputOnly());
		assertFalse(ridget.isVisible());
		assertFalse(control.isVisible());

		ridget.setVisible(true);
		ridget.setOutputOnly(false);

		assertFalse(ridget.isOutputOnly());
		assertTrue(ridget.isVisible());
		assertTrue(control.isVisible());

		ridget.setVisible(false);
		ridget.setOutputOnly(true);

		assertTrue(ridget.isOutputOnly());
		assertFalse(ridget.isVisible());
		assertFalse(control.isVisible());

		ridget.setVisible(true);
		ridget.setOutputOnly(true);
		ridget.setVisible(false);
		ridget.setVisible(true);

		assertTrue(ridget.isOutputOnly());
		assertTrue(ridget.isVisible());
		assertFalse(control.isVisible());
	}

	/**
	 * Tests that changing the selected state via
	 * {@link IToggleButtonRidget#setSelected(boolean) does not select the
	 * control, when the ridget is disabled.
	 */
	public void testDisabledRidgetDoesNotCheckControlOnRidgetSelection() {
		IToggleButtonRidget ridget = getRidget();
		Button control = getUIControl();
		BooleanTestBean model = new BooleanTestBean();
		ridget.bindToModel(model, "selected");

		ridget.setSelected(false);
		ridget.setEnabled(false);

		assertFalse(model.isSelected());
		assertFalse(ridget.isSelected());
		assertFalse(control.getSelection());

		ridget.setSelected(true);

		assertTrue(model.isSelected());
		assertTrue(ridget.isSelected());
		if (MarkerSupport.HIDE_DISABLED_RIDGET_CONTENT) {
			assertFalse(control.getSelection());
		} else {
			assertTrue(control.getSelection());
		}

		ridget.setEnabled(true);

		assertTrue(model.isSelected());
		assertTrue(ridget.isSelected());
		assertTrue(control.getSelection());
	}

	/**
	 * Tests that changing the selected state via a bound model, does not select
	 * the control, when the ridget is disabled.
	 */
	public void testDisabledRidgetDoesNotCheckControlOnModelSelection() {
		IToggleButtonRidget ridget = getRidget();
		Button control = getUIControl();
		BooleanTestBean model = new BooleanTestBean();
		ridget.bindToModel(model, "selected");
		ridget.setEnabled(false);

		model.setSelected(false);
		ridget.updateFromModel();

		assertFalse(model.isSelected());
		assertFalse(ridget.isSelected());
		assertFalse(control.getSelection());

		model.setSelected(true);
		ridget.updateFromModel();

		assertTrue(model.isSelected());
		assertTrue(ridget.isSelected());
		if (MarkerSupport.HIDE_DISABLED_RIDGET_CONTENT) {
			assertFalse(control.getSelection());
		} else {
			assertTrue(control.getSelection());
		}

		ridget.setEnabled(true);

		assertTrue(model.isSelected());
		assertTrue(ridget.isSelected());
		assertTrue(control.getSelection());
	}

	/**
	 * Tests that disabling the ridget unselects the checkbox button, even when
	 * no model is bound to the ridget.
	 */
	public void testDisableRidgetRemovesSelection() {
		IToggleButtonRidget ridget = getRidget();
		Button control = getUIControl();
		ridget.setEnabled(true);
		ridget.setSelected(true);

		assertTrue(ridget.isEnabled());
		assertTrue(ridget.isSelected());
		assertTrue(control.isEnabled());
		assertTrue(control.getSelection());

		ridget.setEnabled(false);

		assertFalse(ridget.isEnabled());
		assertTrue(ridget.isSelected());
		assertFalse(control.isEnabled());
		if (MarkerSupport.HIDE_DISABLED_RIDGET_CONTENT) {
			assertFalse(control.getSelection());
		} else {
			assertTrue(control.getSelection());
		}

		ridget.setEnabled(true);

		assertTrue(ridget.isEnabled());
		assertTrue(ridget.isSelected());
		assertTrue(control.isEnabled());
		assertTrue(control.getSelection());
	}

	/**
	 * Tests that disabling the ridget does not fire 'selected' events, even
	 * though the control is modified.
	 */
	public void testDisabledDoesNotFireSelected() {
		IToggleButtonRidget ridget = getRidget();
		ridget.setEnabled(true);
		ridget.setSelected(true);
		ridget.addPropertyChangeListener(IToggleButtonRidget.PROPERTY_SELECTED, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				fail("Unexpected property change event: " + evt);
			}
		});

		ridget.setEnabled(false);

		ridget.setEnabled(true);
	}

	/**
	 * Check that disabling / enabling works when we don't have a bound control.
	 */
	public void testDisableWithoutUIControl() {
		IToggleButtonRidget ridget = getRidget();
		ridget.setUIControl(null);

		ridget.setEnabled(false);

		assertFalse(ridget.isEnabled());

		ridget.setEnabled(true);

		assertTrue(ridget.isEnabled());
	}

	/**
	 * Tests that the disabled state is applied to a new control when set into
	 * the ridget.
	 */
	public void testDisableAndClearOnBind() {
		IToggleButtonRidget ridget = getRidget();
		Button control = getUIControl();

		ridget.setUIControl(null);
		ridget.setEnabled(false);
		ridget.setSelected(true);
		ridget.setUIControl(control);

		assertFalse(control.isEnabled());
		if (MarkerSupport.HIDE_DISABLED_RIDGET_CONTENT) {
			assertFalse(control.getSelection());
		} else {
			assertTrue(control.getSelection());
		}

		ridget.setEnabled(true);

		assertTrue(control.isEnabled());
		assertTrue(control.getSelection());
	}

	// helping classes
	// ////////////////

	/**
	 * POJO-Bean with only one boolean.
	 */
	private static class BooleanTestBean {

		private boolean selected;

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}
	}

}
