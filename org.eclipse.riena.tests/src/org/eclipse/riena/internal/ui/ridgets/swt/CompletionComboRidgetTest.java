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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;
import org.eclipse.riena.ui.swt.CompletionCombo;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Tests of the class {@link ComboRidget}.
 */
public class CompletionComboRidgetTest extends AbstractComboRidgetTest {

	@Override
	protected Control createWidget(final Composite parent) {
		return UIControlsFactory.createCompletionCombo(parent);
	}

	@Override
	protected Control createWidget(final Composite parent, final int style) {
		return UIControlsFactory.createCompletionCombo(parent, style);
	}

	@Override
	protected IRidget createRidget() {
		return new CompletionComboRidget();
	}

	// testing methods
	//////////////////

	public void testRidgetMapping() {
		final SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertTrue(getWidget() instanceof CompletionCombo);
		assertSame(CompletionComboRidget.class, mapper.getRidgetClass(getWidget()));
	}

	public void testMandatoryChangesTextBackgroundOnly() {
		final IComboRidget ridget = getRidget();
		final CompletionCombo control = (CompletionCombo) getWidget();
		final Color bg = control.getBackground();
		final Color mandatory = new Color(null, 255, 255, 175);

		try {
			assertEquals(bg, control.getBackground());
			assertEquals(bg, control.getTextBackground());
			assertEquals(bg, control.getListBackground());

			ridget.setMandatory(true);

			assertEquals(bg, control.getBackground());
			assertEquals(mandatory, control.getTextBackground());
			assertEquals(bg, control.getListBackground());

			ridget.setMandatory(false);

			assertEquals(bg, control.getBackground());
			assertEquals(bg, control.getTextBackground());
			assertEquals(bg, control.getListBackground());
		} finally {
			mandatory.dispose();
		}
	}
}
