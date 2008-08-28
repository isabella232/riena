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

import junit.framework.TestCase;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.tests.FTActionListener;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.riena.ui.ridgets.swt.DefaultRealm;
import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtControlRidgetMapper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

/**
 * Tests of the class <code>ToggleButtonRidget</code>.
 * 
 */
public class ToggleButtonRidgetTest extends TestCase {

	private final static String PLUGIN_ID = "org.eclipse.riena.ui.tests:";
	private final static String ICON_ECLIPSE = PLUGIN_ID + "/icons/eclipse.gif";
	private final static String LABEL = "testlabel";
	private final static String LABEL2 = "testlabel2";

	private DefaultRealm realm;
	private Shell shell;
	private Button button;
	private ToggleButtonRidget ridget;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		realm = new DefaultRealm();
		shell = new Shell();
		button = new Button(shell, SWT.CHECK);
		ridget = new ToggleButtonRidget();
		ridget.setUIControl(button);
		shell.setLayout(new FillLayout());
		shell.setSize(100, 100);
		shell.setLocation(100, 100);
		shell.open();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		shell.dispose();
		shell = null;
		realm.dispose();
		realm = null;
	}

	public void testRidgetMapping() {
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

		assertSame(button, ridget.getUIControl());

	}

	public void testSetSelected() throws Exception {

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

		BooleanTestBean model = new BooleanTestBean();
		model.setSelected(true);
		IObservableValue modelOV = BeansObservables.observeValue(model, "selected");
		ridget.bindToModel(modelOV);
		ridget.updateFromModel();
		assertTrue(ridget.isSelected());
	}

	public void testBindToModelIObservableValue() throws Exception {

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
		Button control = ridget.getUIControl();

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
		Button control = ridget.getUIControl();

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
		Button control = ridget.getUIControl();

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
