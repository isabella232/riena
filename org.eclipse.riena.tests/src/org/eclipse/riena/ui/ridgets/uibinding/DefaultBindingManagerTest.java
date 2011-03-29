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
package org.eclipse.riena.ui.ridgets.uibinding;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.internal.ui.ridgets.swt.LabelRidget;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.DefaultRealm;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@code DefaultBindingManager}
 */
@UITestCase
public class DefaultBindingManagerTest extends TestCase {

	static final String BINDING_PROPERTY = "binding_property"; //$NON-NLS-1$
	private IBindingManager manager;
	private SubModuleController ridgetContainer;
	private Shell shell;
	private DefaultRealm realm;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		realm = new DefaultRealm();
		manager = new DefaultBindingManager(new BindingPropertyLocator(), new ControlRidgetMapper());
		ridgetContainer = new SubModuleController();
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
		SwtUtilities.dispose(shell);
		realm.dispose();
		realm = null;
	}

	/**
	 * Tests the method <code>injectRidgets</code>.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testInjectRidgets() throws Exception {

		final List<Object> uiControls = new ArrayList<Object>(2);
		final Label label1 = new Label(shell, SWT.NONE);
		label1.setData(BINDING_PROPERTY, "label1"); //$NON-NLS-1$
		uiControls.add(label1);
		final Label label2 = new Label(shell, SWT.NONE);
		label2.setData(BINDING_PROPERTY, "label2"); //$NON-NLS-1$
		uiControls.add(label2);

		manager.injectRidgets(ridgetContainer, uiControls);

		// injected, but not binded
		assertNotNull(ridgetContainer.getRidget("label1"));
		assertNull(ridgetContainer.getRidget("label1").getUIControl());
		// injected, but not binded
		assertNotNull(ridgetContainer.getRidget("label2"));
		assertNull(ridgetContainer.getRidget("label2").getUIControl());

		label1.dispose();
		label2.dispose();

	}

	/**
	 * Tests the method <code>bind</code>.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testBind() throws Exception {

		final List<Object> uiControls = new ArrayList<Object>(2);
		final Label label1 = new Label(shell, SWT.NONE);
		label1.setData(BINDING_PROPERTY, "label1"); //$NON-NLS-1$
		uiControls.add(label1);

		manager.injectRidgets(ridgetContainer, uiControls);
		manager.bind(ridgetContainer, uiControls);

		// injected and binded
		assertNotNull(ridgetContainer.getRidget("label1"));
		assertSame(label1, ridgetContainer.getRidget("label1").getUIControl());

		label1.dispose();

	}

	/**
	 * Tests the method <code>unbind</code>.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testUnbind() throws Exception {

		final List<Object> uiControls = new ArrayList<Object>(2);
		final Label label1 = new Label(shell, SWT.NONE);
		label1.setData(BINDING_PROPERTY, "label1"); //$NON-NLS-1$
		uiControls.add(label1);

		manager.injectRidgets(ridgetContainer, uiControls);
		manager.bind(ridgetContainer, uiControls);

		// binded
		assertSame(label1, ridgetContainer.getRidget("label1").getUIControl());

		manager.unbind(ridgetContainer, uiControls);

		// unbinded
		assertNull(ridgetContainer.getRidget("label1").getUIControl());

		label1.dispose();

	}

	/**
	 * Tests the method {@code createRidget}.
	 */
	public void testCreateRidget() {

		final Label label1 = new Label(shell, SWT.NONE);

		final IRidget ridget = ReflectionUtils.invokeHidden(manager, "createRidget", label1);
		assertNotNull(ridget);
		assertTrue(ridget instanceof LabelRidget);

		label1.dispose();

	}

	private static final class BindingPropertyLocator implements IBindingPropertyLocator {

		public String locateBindingProperty(final Object uiControl) {
			final Control control = (Control) uiControl;
			return (String) control.getData(BINDING_PROPERTY);
		}
	}

	/**
	 * This Mapper returns always the class <code>LabelRidget</code>.
	 */
	private static class ControlRidgetMapper implements IControlRidgetMapper<Object> {

		public void addMapping(final Class<? extends Object> controlClazz, final Class<? extends IRidget> ridgetClazz) {
			// not supported in this test
		}

		public void addMapping(final Class<? extends Object> controlClazz, final Class<? extends IRidget> ridgetClazz,
				final IMappingCondition condition) {
			// not supported in this test
		}

		public Class<? extends IRidget> getRidgetClass(final Class<? extends Object> controlClazz) {
			return LabelRidget.class;
		}

		public Class<? extends IRidget> getRidgetClass(final Object control) {
			return LabelRidget.class;
		}

	}

}
