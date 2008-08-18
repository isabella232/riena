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
package org.eclipse.riena.ui.ridgets.swt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.uibinding.DefaultBindingManager;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingManager;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingPropertyLocator;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * Factory creating ridgets for SWT controls.
 * 
 * @see #createRidget(Control)
 */
public final class SwtRidgetFactory {

	static {
		Display display = Display.getCurrent();
		Assert.isNotNull(display);
		new DefaultRealm(display);
	}

	private static final IBindingManager DUMMY_BINDING_MAN = new DefaultBindingManager(
			new DummyBindingPropertyLocator(), new DefaultSwtControlRidgetMapper());

	private static final DummyContainer CONTAINER = new DummyContainer();

	private static final List<Object> CONTROL_LIST = new ArrayList<Object>(1);

	public static IRidget createRidget(Control control) {
		Assert.isNotNull(control);
		CONTAINER.clear();
		CONTROL_LIST.clear();
		CONTROL_LIST.add(control);
		DUMMY_BINDING_MAN.injectRidgets(CONTAINER, CONTROL_LIST);
		DUMMY_BINDING_MAN.bind(CONTAINER, CONTROL_LIST);
		IRidget result = CONTAINER.getRidget(null);
		return result;
	}

	private SwtRidgetFactory() {
		// prevent instantiation
	}

	// helping classes
	// ////////////////

	private static final class DummyBindingPropertyLocator implements IBindingPropertyLocator {
		public String locateBindingProperty(Object uiControl) {
			return "dummy"; //$NON-NLS-1$
		}
	}

	private static final class DummyContainer implements IRidgetContainer {

		private IRidget ridget;

		public void addRidget(String id, IRidget ridget) {
			this.ridget = ridget;
		}

		public void configureRidgets() {
			// nothing
		}

		public IRidget getRidget(String id) {
			return ridget;
		}

		public Collection<? extends IRidget> getRidgets() {
			return Arrays.asList(ridget);
		}

		void clear() {
			this.ridget = null;
		}
	}

}
