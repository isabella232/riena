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
package org.eclipse.riena.ui.ridgets.uibinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.riena.internal.ui.ridgets.swt.DefaultRealm;
import org.eclipse.riena.internal.ui.ridgets.swt.LabelRidget;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

/**
 * Tests of the class <code>DefaultBindingManager</code>.
 */
public class InjectBindingManagerTest extends TestCase {

	/**
	 * 
	 */
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
		shell.dispose();
		shell = null;
		realm.dispose();
		realm = null;
	}

	/**
	 * Tests the method <code>injectRidgets</code>.
	 * 
	 * @throws Exception
	 *             - handled by JUnit
	 */
	public void testInjectRidgets() throws Exception {

		List<Object> uiControls = new ArrayList<Object>(2);
		Label label1 = new Label(shell, SWT.NONE);
		label1.setData(BINDING_PROPERTY, "label1"); //$NON-NLS-1$
		uiControls.add(label1);
		Label label2 = new Label(shell, SWT.NONE);
		label2.setData(BINDING_PROPERTY, "label2"); //$NON-NLS-1$
		uiControls.add(label2);

		manager.injectRidgets(ridgetContainer, uiControls);

		// injected, but not binded
		assertNotNull(ridgetContainer.getLabel1());
		assertNull(ridgetContainer.getLabel1().getUIControl());
		// injected, but not binded
		assertNotNull(ridgetContainer.getLabel2());
		assertNull(ridgetContainer.getLabel2().getUIControl());

		label1.dispose();
		label2.dispose();

	}

	/**
	 * Tests the method <code>bind</code>.
	 * 
	 * @throws Exception
	 *             - handled by JUnit
	 */
	public void testBind() throws Exception {

		List<Object> uiControls = new ArrayList<Object>(2);
		Label label1 = new Label(shell, SWT.NONE);
		label1.setData(BINDING_PROPERTY, "label1"); //$NON-NLS-1$
		uiControls.add(label1);

		manager.injectRidgets(ridgetContainer, uiControls);
		manager.bind(ridgetContainer, uiControls);

		// injected and binded
		assertNotNull(ridgetContainer.getLabel1());
		assertSame(label1, ridgetContainer.getLabel1().getUIControl());

		label1.dispose();

	}

	/**
	 * Tests the method <code>unbind</code>.
	 * 
	 * @throws Exception
	 *             - handled by JUnit
	 */
	public void testUnbind() throws Exception {

		List<Object> uiControls = new ArrayList<Object>(2);
		Label label1 = new Label(shell, SWT.NONE);
		label1.setData(BINDING_PROPERTY, "label1"); //$NON-NLS-1$
		uiControls.add(label1);

		manager.injectRidgets(ridgetContainer, uiControls);
		manager.bind(ridgetContainer, uiControls);

		// binded
		assertSame(label1, ridgetContainer.getLabel1().getUIControl());

		manager.unbind(ridgetContainer, uiControls);

		// unbinded
		assertNull(ridgetContainer.getLabel1().getUIControl());

		label1.dispose();

	}

	private final class BindingPropertyLocator implements IBindingPropertyLocator {

		public String locateBindingProperty(Object uiControl) {
			Control control = (Control) uiControl;
			return (String) control.getData(BINDING_PROPERTY);
		}
	}

	/**
	 * This Mapper returns always the class <code>LabelRidget</code>.
	 */
	private class ControlRidgetMapper implements IControlRidgetMapper<Widget> {

		/**
		 * @see org.eclipse.riena.ui.ridgets.uibinding.IControlRidgetMapper#addMapping(java.lang.Class,
		 *      java.lang.Class)
		 */
		public void addMapping(Class<? extends Widget> controlClazz, Class<? extends IRidget> ridgetClazz) {
			// not supported in this test
		}

		/**
		 * @see org.eclipse.riena.ui.ridgets.uibinding.IControlRidgetMapper#addSpecialMapping(java.lang.String,
		 *      java.lang.Class)
		 */
		public void addSpecialMapping(String controlName, Class<? extends Object> ridgetClazz) {
			// not supported in this test
		}

		/**
		 * @see org.eclipse.riena.ui.ridgets.uibinding.IControlRidgetMapper#getRidgetClass(java.lang.Class)
		 */
		public Class<? extends IRidget> getRidgetClass(Class<? extends Widget> controlClazz) {
			return LabelRidget.class;
		}

		/**
		 * @see org.eclipse.riena.ui.ridgets.uibinding.IControlRidgetMapper#getRidgetClass(java.lang.Object)
		 */
		public Class<? extends IRidget> getRidgetClass(Widget control) {
			return LabelRidget.class;
		}

	}

	public class RidgetContainer implements IRidgetContainer {

		private ILabelRidget label1;
		private ILabelRidget label2;
		private Map<String, IRidget> ridgets;

		public RidgetContainer() {
			ridgets = new HashMap<String, IRidget>();
		}

		/**
		 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#addRidget(java.lang.String,
		 *      org.eclipse.riena.ui.ridgets.IRidget)
		 */
		public void addRidget(String id, IRidget ridget) {
			ridgets.put(id, ridget);

		}

		/**
		 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#getRidget(java.lang.String)
		 */
		public IRidget getRidget(String id) {
			return ridgets.get(id);
		}

		/**
		 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#getRidgets()
		 */
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

		/**
		 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
		 */
		public void configureRidgets() {
		}

	}

}
