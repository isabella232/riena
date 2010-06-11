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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;
import org.eclipse.riena.ui.swt.CompletionCombo;

/**
 * Tests of the class {@link ComboRidget}.
 */
public class CompletionComboRidgetTest extends AbstractComboRidgetTest {

	@Override
	protected Control createWidget(Composite parent) {
		return new CompletionCombo(parent, SWT.NONE);
	}

	@Override
	protected Control createWidget(Composite parent, int style) {
		return new CompletionCombo(parent, style);
	}

	@Override
	protected IRidget createRidget() {
		return new CompletionComboRidget();
	}

	public void testRidgetMapping() {
		SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertTrue(getWidget() instanceof CompletionCombo);
		assertSame(CompletionComboRidget.class, mapper.getRidgetClass(getWidget()));
	}
}
