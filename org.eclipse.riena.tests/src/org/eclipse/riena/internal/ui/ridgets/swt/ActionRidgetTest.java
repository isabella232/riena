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

import org.eclipse.core.databinding.BindingException;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtControlRidgetMapper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;

/**
 * Tests of the class {@link ComboRidget}.
 */
public class ActionRidgetTest extends AbstractSWTRidgetTest {

	private final static String PLUGIN_ID = "org.eclipse.riena.ui.tests:";
	private final static String ICON_ECLIPSE = PLUGIN_ID + "/icons/eclipse.gif";

	private final static String LABEL = "testlabel";
	private final static String LABEL2 = "testlabel2";

	@Override
	public void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected Control createUIControl(Composite parent) {
		return new Button(parent, SWT.PUSH);
	}

	@Override
	protected IRidget createRidget() {
		return new ActionRidget();
	}

	@Override
	protected Button getUIControl() {
		return (Button) super.getUIControl();
	}

	@Override
	protected IActionRidget getRidget() {
		return (IActionRidget) super.getRidget();
	}

	public void testRidgetMapping() {
		DefaultSwtControlRidgetMapper mapper = new DefaultSwtControlRidgetMapper();
		assertSame(ActionRidget.class, mapper.getRidgetClass(getUIControl()));
	}

	public void testSetUIControl() {
		IActionRidget ridget = getRidget();

		ridget.setUIControl(null);
		assertNull(ridget.getUIControl());

		ridget.setUIControl(getUIControl());
		assertSame(getUIControl(), ridget.getUIControl());
	}

	public void testSetUIControlInvalid() {
		IActionRidget ridget = getRidget();

		try {
			ridget.setUIControl(getShell());
			fail();
		} catch (BindingException bex) {
			// expected
		}
	}

	public void testAddListenerInvalid() {
		try {
			getRidget().addListener(null);
			fail();
		} catch (RuntimeException rex) {
			// expected
		}
	}

	public void testAddListener() {
		Button control = getUIControl();
		IActionRidget ridget = getRidget();

		FTActionListener listener1 = new FTActionListener();
		FTActionListener listener2 = new FTActionListener();

		ridget.addListener(listener1);
		ridget.addListener(listener2);
		ridget.addListener(listener2);

		fireSelectionEvent(control);

		assertEquals(1, listener1.getCount());
		assertEquals(2, listener2.getCount());

		ridget.removeListener(listener1);
		fireSelectionEvent(control);

		assertEquals(1, listener1.getCount());
		assertEquals(4, listener2.getCount());

		ridget.removeListener(listener2);
		fireSelectionEvent(control);

		assertEquals(1, listener1.getCount());
		assertEquals(5, listener2.getCount());

		ridget.removeListener(listener2);
		fireSelectionEvent(control);

		assertEquals(1, listener1.getCount());
		assertEquals(5, listener2.getCount());
	}

	public final void testSetText() throws Exception {
		IActionRidget ridget = getRidget();
		Button control = getUIControl();

		ridget.setText("");

		assertEquals("", ridget.getText());
		assertEquals("", control.getText());

		ridget.setText(null);

		assertEquals(null, ridget.getText());
		assertEquals("", control.getText());

		ridget.setText(LABEL);

		assertEquals(LABEL, ridget.getText());
		assertEquals(LABEL, control.getText());

		ridget.setUIControl(null);
		ridget.setText(LABEL2);

		assertEquals(LABEL2, ridget.getText());
		assertEquals(LABEL, control.getText());

		ridget.setUIControl(control);

		assertEquals(LABEL2, ridget.getText());
		assertEquals(LABEL2, control.getText());
	}

	/**
	 * Test method get/setIcon().
	 */
	public final void testSetIcon() {
		IActionRidget ridget = getRidget();
		Button control = (Button) ridget.getUIControl();

		ridget.setIcon(ICON_ECLIPSE);

		assertEquals(ICON_ECLIPSE, ridget.getIcon());
		assertNotNull(control.getImage());

		ridget.setIcon(null);

		assertNull(ridget.getIcon());
		assertNull(control.getImage());
	}

	/**
	 * Tests the method {@code initText}
	 */
	public void testInitText() {

		IActionRidget ridget = getRidget();
		Button control = (Button) ridget.getUIControl();

		ReflectionUtils.setHidden(ridget, "textAlreadyInitialized", false);
		ridget.setText(null);
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

	// helping methods
	// ////////////////

	private void fireSelectionEvent(Button control) {
		Event event = new Event();
		event.type = SWT.Selection;
		event.widget = control;
		control.notifyListeners(SWT.Selection, event);
	}

	private static final class FTActionListener implements IActionListener {
		private int count;

		public void callback() {
			count++;
		}

		int getCount() {
			return count;
		}

	}
}
