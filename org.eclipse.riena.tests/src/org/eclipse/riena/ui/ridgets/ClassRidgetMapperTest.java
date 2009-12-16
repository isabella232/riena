/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets;

import java.beans.PropertyChangeListener;
import java.util.Map;

import org.eclipse.core.databinding.BindingException;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.ui.ridgets.swt.ComboRidget;
import org.eclipse.riena.navigation.IAction;
import org.eclipse.riena.ui.ridgets.ClassRidgetMapper;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.listener.IFocusListener;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;

/**
 *
 */
public class ClassRidgetMapperTest extends RienaTestCase {
	private ClassRidgetMapper mapper;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// only used to get the initial mappings
		// Create new instance of SwtControlRidgetMapper to start with initial mappings only (not additional mappings added in previous test cases)
		ReflectionUtils.setHidden(SwtControlRidgetMapper.class, "instance", ReflectionUtils.newInstanceHidden(
				SwtControlRidgetMapper.class, new Object[0]));

		mapper = ClassRidgetMapper.getInstance();
	}

	@Override
	protected void tearDown() throws Exception {

		// Create new instance of ClassRidgetMapper to start with initial mappings only (not additional mappings added in previous test cases)
		ReflectionUtils.setHidden(ClassRidgetMapper.class, "instance", ReflectionUtils.newInstanceHidden(
				ClassRidgetMapper.class, new Object[0]));
		mapper = null;

		super.tearDown();
	}

	public void testAddMapping() throws Exception {
		mapper.addMapping(IMockRidget.class, MockRidget.class);

		Class<? extends IRidget> ridget = mapper.getRidgetClass(IMockRidget.class);
		assertNotNull(ridget);
		assertEquals(MockRidget.class.getName(), ridget.getName());

		Map<Class<? extends IRidget>, Class<? extends IRidget>> mappings = ReflectionUtils
				.getHidden(mapper, "mappings");
		int size = mappings.size();
		mapper.addMapping(null, null);
		assertEquals(size, mappings.size());
	}

	public void testAddMappingWithRidgetClassOnly() throws Exception {
		mapper.addMapping(MockRidget.class);
		Class<? extends IRidget> ridget = mapper.getRidgetClass(IMockRidget.class);
		assertSameRidgetClass(MockRidget.class, ridget);

		mapper.addMapping(MockRidget2.class);
		ridget = mapper.getRidgetClass(IMockRidget.class);
		assertSameRidgetClass(MockRidget.class, ridget);

		mapper.addMapping(MockRidgetImplementigSeveralInterfaces.class);
		try {
			ridget = mapper.getRidgetClass(IMockRidget3.class);
			fail("BindingException expected");
		} catch (BindingException e) {
			ok("BindingException expected");
		}
	}

	public void testGetRidgetClass() throws Exception {

		Class<? extends IRidget> ridget = mapper.getRidgetClass(IComboRidget.class);
		assertNotNull(ridget);
		assertEquals(ComboRidget.class.getName(), ridget.getName());

		try {
			mapper.getRidgetClass(IMockRidget.class);
			fail("BindingException expected");
		} catch (BindingException e) {
			ok("BindingException expected");
		}

	}

	private void assertSameRidgetClass(Class<? extends IRidget> expected, Class<? extends IRidget> actual) {
		assertNotNull(actual);
		assertEquals(expected.getName(), actual.getName());
	}

	/**
	 * Mock interface extending IRidget.
	 */
	static interface IMockRidget extends IRidget {

	}

	/**
	 * Mock interface extending IRidget.
	 */
	static interface IMockRidget2 extends IMockRidget {

	}

	static interface IMockRidget3 extends IMockRidget {

	}

	private static class MockNonRidgetSuperClass implements Comparable<MockNonRidgetSuperClass> {

		public int compareTo(MockNonRidgetSuperClass o) {
			return 0;
		}

	}

	private static class MockRidgetImplementigSeveralInterfaces extends MockNonRidgetSuperClass implements
			IMockRidget3, IAction {

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

		public boolean isEnabled() {
			return false;
		}

		public void setEnabled(boolean enabled) {
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

		public boolean isBlocked() {
			return false;
		}

		public void setBlocked(boolean blocked) {
		}

		public String getID() {
			return null;
		}

		public void run() {
		}

	}

	/**
	 * abstract mock implementation of ridget.
	 */
	private static abstract class AbstractMockRidget implements IMockRidget2 {
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

		public boolean isEnabled() {
			return false;
		}

		public void setEnabled(boolean enabled) {
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

		public boolean isBlocked() {
			return false;
		}

		public void setBlocked(boolean blocked) {
		}

		public String getID() {
			return null;
		}
	}

	/**
	 * Mock implementation of ridget.
	 */
	private static class MockRidget implements IMockRidget {

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

		public boolean isEnabled() {
			return false;
		}

		public void setEnabled(boolean enabled) {
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

		public boolean isBlocked() {
			return false;
		}

		public void setBlocked(boolean blocked) {
		}

		public String getID() {
			return null;
		}
	}

	/**
	 * Another mock implementation of ridget.
	 */
	private static class MockRidget2 extends AbstractMockRidget {

	}

}
