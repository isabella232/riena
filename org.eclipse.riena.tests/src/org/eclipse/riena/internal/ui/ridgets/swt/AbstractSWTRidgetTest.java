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

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.eclipse.riena.tests.UITestHelper;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.tests.base.PropertyChangeEventEquals;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Tests for the class {@link AbstractSwtRidget}.
 */
public abstract class AbstractSWTRidgetTest extends TestCase {

	private DefaultRealm realm;
	private Shell shell;
	private Control control;
	private IRidget ridget;
	private PropertyChangeListener propertyChangeListenerMock;

	@Override
	protected void setUp() throws Exception {
		realm = new DefaultRealm();

		shell = new Shell();
		shell.setLayout(new RowLayout(SWT.VERTICAL));

		control = createUIControl(shell);

		ridget = createRidget();
		ridget.setUIControl(control);
		propertyChangeListenerMock = EasyMock.createMock(PropertyChangeListener.class);
		ridget.addPropertyChangeListener(propertyChangeListenerMock);

		shell.setSize(100, 100);
		shell.setLocation(0, 0);
		shell.open();
	}

	@Override
	protected void tearDown() throws Exception {
		ridget = null;
		control.dispose();
		control = null;
		shell.dispose();
		shell = null;
		realm.dispose();
		realm = null;
	}

	// protected methods
	// //////////////////

	protected abstract Control createUIControl(final Composite parent);

	protected abstract IRidget createRidget();

	protected Control getUIControl() {
		return control;
	}

	protected IRidget getRidget() {
		return ridget;
	}

	protected final Shell getShell() {
		return shell;
	}

	// easy mock helper methods
	// /////////////////////////

	protected final void verifyPropertyChangeEvents() {
		EasyMock.verify(propertyChangeListenerMock);
	}

	protected final void expectNoPropertyChangeEvent() {
		EasyMock.reset(propertyChangeListenerMock);
		EasyMock.replay(propertyChangeListenerMock);
	}

	protected final void expectPropertyChangeEvents(PropertyChangeEvent... propertyChangeEvents) {
		EasyMock.reset(propertyChangeListenerMock);
		for (PropertyChangeEvent propertyChangeEvent : propertyChangeEvents) {
			propertyChangeListenerMock.propertyChange(createArgumentMatcher(propertyChangeEvent));
		}
		EasyMock.replay(propertyChangeListenerMock);
	}

	protected final void expectPropertyChangeEvent(String propertyName, Object oldValue, Object newValue) {
		expectPropertyChangeEvents(new PropertyChangeEvent(getRidget(), propertyName, oldValue, newValue));
	}

	// test methods
	// /////////////

	public void testIsVisible() {
		shell.open();
		ridget.setVisible(false);
		assertFalse(ridget.isVisible());

		ridget.setVisible(true);
		assertTrue(ridget.isVisible());
	}

	public void testGetToolTip() {
		ridget.setUIControl(null);

		assertEquals(null, ridget.getToolTipText());

		ridget.setToolTipText("foo");

		assertEquals("foo", ridget.getToolTipText());

		Control control = getUIControl();
		control.setToolTipText(null);
		ridget.setUIControl(control);

		assertEquals("foo", ridget.getToolTipText());
		assertEquals("foo", ((Control) ridget.getUIControl()).getToolTipText());
	}

	public void testGetFocusable() {
		IRidget ridget = getRidget();

		assertTrue(ridget.isFocusable());

		ridget.setFocusable(false);

		assertFalse(ridget.isFocusable());

		ridget.setFocusable(true);

		assertTrue(ridget.isFocusable());
	}

	public void testSetFocusable() {
		IRidget ridget = getRidget();
		Control control = getUIControl();
		Control firstControl = new Text(getShell(), SWT.SINGLE);
		firstControl.moveAbove(control);

		control.setFocus();
		if (control.isFocusControl()) { // skip if control cannot receive focus

			ridget.setFocusable(false);
			firstControl.setFocus();

			assertTrue(firstControl.isFocusControl());

			UITestHelper.sendString(control.getDisplay(), "\t");

			assertFalse(control.isFocusControl());

			ridget.setFocusable(true);
			UITestHelper.sendString(control.getDisplay(), "\t");

			assertTrue(control.isFocusControl());
		}
	}

	// helping methods
	// ////////////////

	private PropertyChangeEvent createArgumentMatcher(PropertyChangeEvent propertyChangeEvent) {
		return PropertyChangeEventEquals.eqPropertyChangeEvent(propertyChangeEvent);
	}

}
