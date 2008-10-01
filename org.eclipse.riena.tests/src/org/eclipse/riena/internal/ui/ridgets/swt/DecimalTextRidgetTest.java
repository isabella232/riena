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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.riena.ui.ridgets.IDecimalValueTextFieldRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtControlRidgetMapper;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * Tests for the class {@link DecimalTextRidget}.
 */
public class DecimalTextRidgetTest extends NumericTextRidgetTest {

	// TODO [ev] decide if we really need to extend NumericTextRidgetTest
	// TODO [ev] add to test suite

	@Override
	protected IRidget createRidget() {
		return new DecimalTextRidget();
	}

	@Override
	protected IDecimalValueTextFieldRidget getRidget() {
		return (IDecimalValueTextFieldRidget) super.getRidget();
	}

	@Override
	protected Control createUIControl(Composite parent) {
		Control result = new Text(getShell(), SWT.RIGHT | SWT.BORDER | SWT.SINGLE);
		result.setData(UIControlsFactory.KEY_TYPE, UIControlsFactory.TYPE_DECIMAL);
		result.setLayoutData(new RowData(100, SWT.DEFAULT));
		return result;
	}

	// test methods
	///////////////

	@Override
	public void testRidgetMapping() {
		DefaultSwtControlRidgetMapper mapper = new DefaultSwtControlRidgetMapper();
		assertSame(DecimalTextRidget.class, mapper.getRidgetClass(getUIControl()));
	}

}
