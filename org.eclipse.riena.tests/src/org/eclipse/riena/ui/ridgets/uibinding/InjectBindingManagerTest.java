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
package org.eclipse.riena.ui.ridgets.uibinding;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.internal.ui.ridgets.swt.LabelRidget;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.riena.ui.ridgets.swt.DefaultRealm;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class <code>DefaultBindingManager</code>.
 */
@UITestCase
public class InjectBindingManagerTest extends TestCase {

	private static final String BINDING_PROPERTY = "binding_property"; //$NON-NLS-1$
	private IBindingManager manager;
	private RidgetContainer ridgetContainer;
	private Shell shell;
	private DefaultRealm realm;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		realm = new DefaultRealm();
		manager = new InjectBindingManager(new BindingPropertyLocator(), new ControlRidgetMapper());
		ridgetContainer = new RidgetContainer();
		shell = new Shell();
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		manager = null;
		ridgetContainer = null;
		SwtUtilities.disposeWidget(shell);
		realm.dispose();
		realm = null;
	}

	/**
	 * Tests the method <code>injectRidget</code>.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testInjectRidget() throws Exception {

		Label label1 = new Label(shell, SWT.NONE);

		IRidget ridget = ReflectionUtils.invokeHidden(manager, "createRidget", label1);

		ReflectionUtils.invokeHidden(manager, "injectRidget", ridgetContainer, "label1", ridget);

		// injected, but not binded
		assertNotNull(ridgetContainer.getLabel1());
		assertNull(ridgetContainer.getLabel1().getUIControl());

	}

	/**
	 * Tests the method <code>getRidget</code>.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testGetRidget() throws Exception {

		Label label1 = new Label(shell, SWT.NONE);

		IRidget ridget = ReflectionUtils.invokeHidden(manager, "createRidget", label1);
		ReflectionUtils.invokeHidden(manager, "injectRidget", ridgetContainer, "label1", ridget);

		IRidget ridget1 = ReflectionUtils.invokeHidden(manager, "getRidget", "label1", ridgetContainer);
		assertSame(ridget, ridget1);

	}

	// helping classes
	//////////////////

	private static final class BindingPropertyLocator implements IBindingPropertyLocator {

		public String locateBindingProperty(Object uiControl) {
			Control control = (Control) uiControl;
			return (String) control.getData(BINDING_PROPERTY);
		}
	}

	/**
	 * This Mapper returns always the class <code>LabelRidget</code>.
	 */
	private static class ControlRidgetMapper implements IControlRidgetMapper<Object> {

		public void addMapping(Class<? extends Object> controlClazz, Class<? extends IRidget> ridgetClazz) {
			// not supported in this test
		}

		public void addMapping(Class<? extends Object> controlClazz, Class<? extends IRidget> ridgetClazz,
				IMappingCondition condition) {
			// not supported in this test
		}

		public Class<? extends IRidget> getRidgetClass(Class<? extends Object> controlClazz) {
			return LabelRidget.class;
		}

		public Class<? extends IRidget> getRidgetClass(Object control) {
			return LabelRidget.class;
		}

	}

	public static class RidgetContainer implements IRidgetContainer {

		private ILabelRidget label1;
		private ILabelRidget label2;
		private Map<String, IRidget> ridgets;

		public RidgetContainer() {
			ridgets = new HashMap<String, IRidget>();
		}

		public void addRidget(String id, IRidget ridget) {
			ridgets.put(id, ridget);

		}

		public IRidget getRidget(String id) {
			return ridgets.get(id);
		}

		public Collection<? extends IRidget> getRidgets() {
			return ridgets.values();
		}

		public ILabelRidget getLabel1() {
			return label1;
		}

		public void setLabel1(ILabelRidget label1) {
			this.label1 = label1;
		}

		public ILabelRidget getLabel2() {
			return label2;
		}

		public void setLabel2(ILabelRidget label2) {
			this.label2 = label2;
		}

		public void configureRidgets() {
		}

		@SuppressWarnings("unchecked")
		public <R extends IRidget> R getRidget(Class<R> ridgetClazz, String id) {
			return (R) getRidget(id);
		}

	}

}
