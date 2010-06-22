/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
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
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.riena.ui.ridgets.swt.MarkerSupport;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;
import org.eclipse.riena.ui.swt.utils.ImageStore;

/**
 * Tests of the class <code>ToggleButtonRidget</code>.
 * 
 */
public class ToggleButtonRidgetTest extends AbstractSWTRidgetTest {

	private final static String ICON_ECLIPSE = "eclipse.gif";
	private final static String LABEL = "testlabel";
	private final static String LABEL2 = "testlabel2";

	@Override
	protected IToggleButtonRidget createRidget() {
		return new ToggleButtonRidget();
	}

	@Override
	protected Button createWidget(final Composite parent) {
		return new Button(parent, SWT.CHECK);
	}

	@Override
	protected IToggleButtonRidget getRidget() {
		return (IToggleButtonRidget) super.getRidget();
	}

	@Override
	protected Button getWidget() {
		return (Button) super.getWidget();
	}

	public void testRidgetMapping() {
		final Shell shell = getShell();

		final SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();

		final Button buttonToggle = new Button(shell, SWT.TOGGLE);
		assertSame(ToggleButtonRidget.class, mapper.getRidgetClass(buttonToggle));

		final Button buttonCheck = new Button(shell, SWT.CHECK);
		assertSame(ToggleButtonRidget.class, mapper.getRidgetClass(buttonCheck));

		final Button buttonPush = new Button(shell, SWT.PUSH);
		assertNotSame(ToggleButtonRidget.class, mapper.getRidgetClass(buttonPush));

		final Button aButton = new Button(shell, SWT.NONE);
		assertNotSame(ToggleButtonRidget.class, mapper.getRidgetClass(aButton));
	}

	public void testSetUIControl() throws Exception {
		final IToggleButtonRidget ridget = getRidget();
		final Button button = getWidget();

		assertSame(button, ridget.getUIControl());
	}

	public void testSetSelected() throws Exception {
		final IToggleButtonRidget ridget = getRidget();
		final Button button = getWidget();
		final BooleanTestPojo model = new BooleanTestPojo();
		final IObservableValue modelOV = PojoObservables.observeValue(model, "selected");
		ridget.bindToModel(modelOV);

		ridget.setSelected(true);

		assertTrue(ridget.isSelected());
		assertTrue(button.getSelection());

		ridget.setSelected(false);

		assertFalse(ridget.isSelected());
		assertFalse(button.getSelection());
	}

	public void testIsSelected() throws Exception {
		final IToggleButtonRidget ridget = getRidget();

		final BooleanTestPojo model = new BooleanTestPojo();
		model.setSelected(true);
		final IObservableValue modelOV = PojoObservables.observeValue(model, "selected");
		ridget.bindToModel(modelOV);
		ridget.updateFromModel();

		assertTrue(ridget.isSelected());
	}

	public void testBindToModelIObservableValue() throws Exception {
		final IToggleButtonRidget ridget = getRidget();

		final BooleanTestPojo model = new BooleanTestPojo();
		model.setSelected(true);
		final IObservableValue modelOV = PojoObservables.observeValue(model, "selected");
		ridget.bindToModel(modelOV);

		assertNotNull(BeansObservables.observeValue(ridget, IToggleButtonRidget.PROPERTY_SELECTED));
		assertEquals(boolean.class, BeansObservables.observeValue(ridget, IToggleButtonRidget.PROPERTY_SELECTED)
				.getValueType());
		assertFalse(ridget.isSelected());

		ridget.updateFromModel();

		assertTrue(ridget.isSelected());
	}

	public void testBindToModelPropertyName() throws Exception {
		final IToggleButtonRidget ridget = getRidget();

		final BooleanTestPojo model = new BooleanTestPojo();
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
		final IToggleButtonRidget ridget = getRidget();
		final Button button = getWidget();

		final BooleanTestPojo model = new BooleanTestPojo();
		model.setSelected(true);
		ridget.bindToModel(model, "selected");
		ridget.updateFromModel();

		assertTrue(button.getSelection());

		model.setSelected(false);
		ridget.updateFromModel();
		assertFalse(button.getSelection());
	}

	public void testActionListener() {
		final IToggleButtonRidget ridget = getRidget();

		ridget.setSelected(false);

		final FTActionListener listener = new FTActionListener();
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
		} catch (final RuntimeException rex) {
			ok();
		}
	}

	public final void testSetText() throws Exception {
		final IToggleButtonRidget ridget = getRidget();
		final Button button = getWidget();

		ridget.setText("");

		assertEquals("", ridget.getText());
		assertEquals("", button.getText());

		try {
			ridget.setText(null);
			fail();
		} catch (final IllegalArgumentException iae) {
			ok();
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
		final IToggleButtonRidget ridget = getRidget();
		final Button control = getWidget();

		ridget.setIcon(ICON_ECLIPSE);

		assertEquals(ICON_ECLIPSE, ridget.getIcon());
		assertNotNull(control.getImage());

		ridget.setIcon(null);

		assertNull(ridget.getIcon());
		assertNull(control.getImage());

		ridget.setIcon("nonsense");

		final Image missingImage = ImageStore.getInstance().getMissingImage();
		assertEquals("nonsense", ridget.getIcon());
		assertEquals(missingImage, control.getImage());

		Button button = createWidget(getShell());
		final Image buttonImage = button.getDisplay().getSystemImage(SWT.ICON_INFORMATION);
		button.setImage(buttonImage);
		IToggleButtonRidget buttonRidget = createRidget();
		// binding doesn't remove image of button, because the icon of the ridget is null and the method #setIcon wasn't called yet.
		buttonRidget.setUIControl(button);
		assertSame(buttonImage, button.getImage());

		buttonRidget.setIcon(null);
		assertNull(buttonRidget.getIcon());
		assertNull(button.getImage());

		buttonRidget.setIcon(ICON_ECLIPSE);
		assertEquals(ICON_ECLIPSE, buttonRidget.getIcon());
		assertNotNull(button.getImage());
		assertNotSame(buttonImage, button.getImage());

		button = createWidget(getShell());
		button.setImage(buttonImage);
		buttonRidget = createRidget();
		buttonRidget.setIcon(ICON_ECLIPSE);
		// binding replaces image of button, because the icon of the ridget is not null.
		buttonRidget.setUIControl(button);
		assertNotNull(button.getImage());
		assertNotSame(buttonImage, button.getImage());

	}

	/**
	 * Tests the method {@code initText}
	 */
	public final void testInitText() {
		final IToggleButtonRidget ridget = getRidget();
		final Button control = getWidget();

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
		final IToggleButtonRidget ridget = getRidget();
		final Button control = getWidget();

		assertFalse(ridget.isOutputOnly());
		assertTrue(control.isVisible());

		ridget.setOutputOnly(true);

		assertTrue(ridget.isOutputOnly());
		assertTrue(control.isVisible());

		ridget.setOutputOnly(false);

		assertFalse(ridget.isOutputOnly());
		assertTrue(control.isVisible());

		ridget.setOutputOnly(true);
		ridget.setVisible(true);

		assertTrue(ridget.isOutputOnly());
		assertTrue(ridget.isVisible());
		assertTrue(control.isVisible());

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
	}

	/**
	 * Tests that changing the selected state via
	 * {@link IToggleButtonRidget#setSelected(boolean) does not select the
	 * control, when the ridget is disabled.
	 */
	public void testDisabledRidgetDoesNotCheckControlOnRidgetSelection() {
		final IToggleButtonRidget ridget = getRidget();
		final Button control = getWidget();
		final BooleanTestPojo model = new BooleanTestPojo();
		ridget.bindToModel(model, "selected");

		ridget.setSelected(false);
		ridget.setEnabled(false);

		assertFalse(model.isSelected());
		assertFalse(ridget.isSelected());
		assertFalse(control.getSelection());

		ridget.setSelected(true);

		assertTrue(model.isSelected());
		assertTrue(ridget.isSelected());
		if (MarkerSupport.isHideDisabledRidgetContent()) {
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
		final IToggleButtonRidget ridget = getRidget();
		final Button control = getWidget();
		final BooleanTestPojo model = new BooleanTestPojo();
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
		if (MarkerSupport.isHideDisabledRidgetContent()) {
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
		final IToggleButtonRidget ridget = getRidget();
		final Button control = getWidget();

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
		if (MarkerSupport.isHideDisabledRidgetContent()) {
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
		final IToggleButtonRidget ridget = getRidget();
		ridget.setEnabled(true);
		ridget.setSelected(true);

		ridget.addPropertyChangeListener(IToggleButtonRidget.PROPERTY_SELECTED, new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent evt) {
				fail("Unexpected property change event: " + evt);
			}
		});

		ridget.setEnabled(false);

		ridget.setEnabled(true);
	}

	/**
	 * Tests that the disabled state is applied to a new control when set into
	 * the ridget.
	 */
	public void testDisableAndClearOnBind() {
		final IToggleButtonRidget ridget = getRidget();
		final Button control = getWidget();

		ridget.setUIControl(null);
		ridget.setEnabled(false);
		ridget.setSelected(true);
		ridget.setUIControl(control);

		assertFalse(control.isEnabled());
		if (MarkerSupport.isHideDisabledRidgetContent()) {
			assertFalse(control.getSelection());
		} else {
			assertTrue(control.getSelection());
		}

		ridget.setEnabled(true);

		assertTrue(control.isEnabled());
		assertTrue(control.getSelection());
	}

	public void testFireAction() {
		final IToggleButtonRidget ridget = getRidget();
		final FTActionListener listener1 = new FTActionListener();
		final FTActionListener listener2 = new FTActionListener();

		ridget.addListener(listener1);

		final BooleanTestPojo model = new BooleanTestPojo();
		model.setSelected(true);
		final IObservableValue modelOV = PojoObservables.observeValue(model, "selected");
		ridget.bindToModel(modelOV);
		ridget.updateFromModel();

		assertTrue(ridget.isSelected());

		ridget.fireAction();
		ridget.fireAction();
		assertEquals(5, listener1.getCount());

		ridget.addListener(listener2);
		ridget.fireAction();
		assertEquals(7, listener1.getCount());
		assertEquals(2, listener2.getCount());
		assertFalse(ridget.isSelected());
	}

	// helping classes
	// ////////////////

	/**
	 * An object holding one boolean.
	 */
	private static class BooleanTestPojo {

		private boolean selected;

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(final boolean selected) {
			this.selected = selected;
		}
	}

}
