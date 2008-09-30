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
package org.eclipse.riena.ui.ridgets.filter;

import junit.framework.TestCase;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.ui.ridgets.swt.LabelRidget;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * Tests of the class {@link RidgetMatcher}.
 */
public class RidgetMatcherTest extends TestCase {

	private Shell shell;

	@Override
	protected void setUp() throws Exception {

		Display display = Display.getDefault();

		Realm realm = SWTObservables.getRealm(display);
		assertNotNull(realm);
		ReflectionUtils.invokeHidden(realm, "setDefault", realm);

		shell = new Shell();

	}

	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.disposeWidget(shell);
	}

	/**
	 * Tests the method {@code matches}.
	 */
	public void testMatches() {

		RidgetMatcher matcher = new RidgetMatcher("4711");
		assertFalse(matcher.matches(null));

		assertFalse(matcher.matches((IRidget) null));

		assertFalse(matcher.matches(new Object()));

		Label control = new Label(shell, SWT.NONE);
		ILabelRidget ridget = new LabelRidget(control);
		assertFalse(matcher.matches(ridget));

		control.setData(SWTBindingPropertyLocator.BINDING_PROPERTY, "4711");
		assertTrue(matcher.matches(ridget));

		SwtUtilities.disposeWidget(control);

	}
}
