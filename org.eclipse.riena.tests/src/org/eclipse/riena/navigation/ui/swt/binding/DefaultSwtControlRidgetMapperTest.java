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
package org.eclipse.riena.navigation.ui.swt.binding;

import java.beans.PropertyChangeListener;

import junit.framework.TestCase;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.riena.internal.ui.ridgets.swt.ActionRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.LabelRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.ToggleButtonRidget;
import org.eclipse.riena.navigation.ui.swt.binding.DefaultSwtControlRidgetMapper.Mapping;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.listener.IFocusListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Tests of the class <code>DefaultSwtControlRidgetMapper</code>
 */
public class DefaultSwtControlRidgetMapperTest extends TestCase {

	private DefaultSwtControlRidgetMapper mapper;
	private Shell shell;

	@Override
	protected void setUp() throws Exception {
		mapper = new DefaultSwtControlRidgetMapper();
		shell = new Shell();
	}

	@Override
	protected void tearDown() throws Exception {
		mapper = null;
		shell.dispose();
		shell = null;
	}

	/**
	 * Tests the method
	 * <code>addMapping(Class<? extends Widget> , Class<? extends IRidget> )</code>
	 * .
	 * 
	 * @throws Exception
	 *             - handled by JUnit
	 */
	public void testAddMapping() throws Exception {

		mapper.addMapping(MockComposite.class, MockRidget.class);

		Class<? extends IRidget> ridget = mapper.getRidgetClass(MockComposite.class);
		assertNotNull(ridget);
		assertEquals(ridget.getName(), MockRidget.class.getName());

	}

	/**
	 * Tests the method
	 * <code>addMapping(Class<? extends Widget> , Class<? extends IRidget> , int )</code>
	 * .
	 * 
	 * @throws Exception
	 *             - handled by JUnit
	 */
	public void testAddMappingSwtStyle() throws Exception {

		mapper.addMapping(MockComposite.class, MockRidget2.class, SWT.BORDER);
		mapper.addMapping(MockComposite.class, MockRidget.class);

		Class<? extends IRidget> ridget = mapper.getRidgetClass(MockComposite.class);
		assertNotNull(ridget);
		assertEquals(ridget.getName(), MockRidget.class.getName());

		MockComposite widget = new MockComposite(shell, SWT.BORDER);
		ridget = mapper.getRidgetClass(widget);
		assertNotNull(ridget);
		assertEquals(ridget.getName(), MockRidget2.class.getName());
		widget.dispose();

	}

	/**
	 * Tests the method <code>getRidgetClass(Class<? extends Widget>)</code>.
	 * 
	 * @throws Exception
	 *             - handled by JUnit
	 */
	public void testGetRidgetClass() throws Exception {

		Class<? extends IRidget> ridget = mapper.getRidgetClass(Label.class);
		assertNotNull(ridget);
		assertEquals(ridget.getName(), LabelRidget.class.getName());

		try {
			ridget = mapper.getRidgetClass(MockComposite.class);
			fail("BindingException expected");
		} catch (BindingException e) {
		}

	}

	/**
	 * Tests the method <code>getRidgetClass(Widget)</code>.
	 * 
	 * @throws Exception
	 *             - handled by JUnit
	 */
	public void testGetRidgetClassWidget() throws Exception {

		Button button = new Button(shell, SWT.DEFAULT);
		Class<? extends IRidget> ridget = mapper.getRidgetClass(button);
		assertNotNull(ridget);
		assertEquals(ridget.getName(), ActionRidget.class.getName());

		button = new Button(shell, SWT.FLAT);
		ridget = mapper.getRidgetClass(button);
		assertNotNull(ridget);
		assertEquals(ridget.getName(), ActionRidget.class.getName());

		button = new Button(shell, SWT.CHECK);
		ridget = mapper.getRidgetClass(button);
		assertNotNull(ridget);
		assertEquals(ridget.getName(), ToggleButtonRidget.class.getName());

	}

	/**
	 * Tests the method <code>isMatching(Class<? extends Widget>)</code>.
	 * 
	 * @throws Exception
	 *             - handled by JUnit
	 */
	public void testIsMatching() throws Exception {

		Mapping mapping = new Mapping(MockComposite.class, MockRidget.class);
		assertTrue(mapping.isMatching(MockComposite.class));
		assertFalse(mapping.isMatching(MockComposite2.class));
		mapping = new Mapping(MockComposite.class, MockRidget.class, SWT.CHECK);
		assertFalse(mapping.isMatching(MockComposite.class));
		assertFalse(mapping.isMatching(MockComposite2.class));

	}

	/**
	 * Tests the method <code>isMatching(Widget>)</code>.
	 * 
	 * @throws Exception
	 *             - handled by JUnit
	 */
	public void testIsMatchingWidget() throws Exception {

		Mapping mapping = new Mapping(MockComposite.class, MockRidget.class);
		MockComposite comp = new MockComposite(shell, SWT.DEFAULT);
		assertTrue(mapping.isMatching(comp));
		comp.dispose();
		MockComposite2 comp2 = new MockComposite2(shell, SWT.DEFAULT);
		assertFalse(mapping.isMatching(comp2));
		comp2.dispose();

		mapping = new Mapping(MockComposite.class, MockRidget.class, SWT.ABORT);
		comp = new MockComposite(shell, SWT.ALPHA);
		assertFalse(mapping.isMatching(comp));
		comp.dispose();
		comp = new MockComposite(shell, SWT.ABORT);
		assertTrue(mapping.isMatching(comp));
		comp.dispose();
		comp = new MockComposite(shell, SWT.ABORT | SWT.ALT);
		assertTrue(mapping.isMatching(comp));
		comp.dispose();

	}

	/**
	 * Mock extention of <code>Composite</code>.
	 */
	private class MockComposite extends Composite {

		public MockComposite(Composite parent, int style) {
			super(parent, style);
		}

	}

	/**
	 * Another mock extention of <code>Composite</code>.
	 */
	private class MockComposite2 extends Composite {

		public MockComposite2(Composite parent, int style) {
			super(parent, style);
		}

	}

	/**
	 * Mock implementation of ridget.
	 */
	private class MockRidget implements IRidget {

		public Object getUIControl() {
			return null;
		}

		public void setUIControl(Object uiControl) {
		}

		public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		}

		public void addPropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener) {
		}

		public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		}

		public void removePropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener) {
		}

		public boolean isVisible() {
			return false;
		}

		public void setVisible(boolean visible) {
		}

		public void addFocusListener(IFocusListener listener) {
		}

		public void removeFocusListener(IFocusListener listener) {
		}

		public void updateFromModel() {
		}

		public void requestFocus() {
		}

		public boolean hasFocus() {
			return false;
		}

		public boolean isFocusable() {
			return false;
		}

		public void setFocusable(boolean focusable) {
		}

		public String getToolTipText() {
			return null;
		}

		public void setToolTipText(String toolTipText) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.riena.ui.ridgets.IRidget#isBlocked()
		 */
		public boolean isBlocked() {
			// TODO Auto-generated method stub
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.riena.ui.ridgets.IRidget#setBlocked(boolean)
		 */
		public void setBlocked(boolean blocked) {
			// TODO Auto-generated method stub

		}
	}

	/**
	 * Another mock implementation of ridget.
	 */
	private class MockRidget2 implements IRidget {

		public Object getUIControl() {
			return null;
		}

		public void setUIControl(Object uiControl) {
		}

		public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		}

		public void addPropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener) {
		}

		public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
		}

		public void removePropertyChangeListener(String propertyName, PropertyChangeListener propertyChangeListener) {
		}

		public boolean isVisible() {
			return false;
		}

		public void setVisible(boolean visible) {
		}

		public void addFocusListener(IFocusListener listener) {
		}

		public void removeFocusListener(IFocusListener listener) {
		}

		public void updateFromModel() {
		}

		public void requestFocus() {
		}

		public boolean hasFocus() {
			return false;
		}

		public boolean isFocusable() {
			return false;
		}

		public void setFocusable(boolean focusable) {
		}

		public String getToolTipText() {
			return null;
		}

		public void setToolTipText(String toolTipText) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.riena.ui.ridgets.IRidget#isBlocked()
		 */
		public boolean isBlocked() {
			// TODO Auto-generated method stub
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.riena.ui.ridgets.IRidget#setBlocked(boolean)
		 */
		public void setBlocked(boolean blocked) {
			// TODO Auto-generated method stub

		}
	}
}
