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

import org.eclipse.core.databinding.BindingException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;

/**
 * Tests of the class {@link CComboRidget}.
 */
public class CComboRidgetTest extends AbstractComboRidgetTest {

	@Override
	protected Control createWidget(Composite parent) {
		return new CCombo(parent, SWT.READ_ONLY);
	}

	@Override
	protected Control createWidget(Composite parent, int style) {
		return new CCombo(parent, style);
	}

	@Override
	protected IRidget createRidget() {
		return new CComboRidget();
	}

	public void testRidgetMapping() {
		SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertTrue(getWidget() instanceof CCombo);
		assertSame(CComboRidget.class, mapper.getRidgetClass(getWidget()));
	}

	// testing methods
	//////////////////

	public void testRequireReadOnlyUIControl() {
		try {
			// not SWT.READ_ONLY => exception 
			getRidget().setUIControl(createWidget(getShell(), SWT.NONE));
			fail();
		} catch (BindingException bex) {
			ok();
		}
	}

}
