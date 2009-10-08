/*******************************************************************************
 * Copyright (c) 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *    Florian Pirchner - added tests
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.ui.ridgets.ITraverseRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;

/**
 * Tests for the {@link ScaleRidget}
 */
public class ScaleRidgetTest extends AbstractTraverseRidgetTest {

	@Override
	protected Widget createWidget(Composite parent) {
		return new Scale(parent, SWT.NONE);
	}

	@Override
	protected ITraverseRidget createRidget() {
		return new ScaleRidget();
	}

	@Override
	protected Scale getWidget() {
		return (Scale) super.getWidget();
	}

	@Override
	protected ITraverseRidget getRidget() {
		return (ITraverseRidget) super.getRidget();
	}

	@Override
	protected int getIncrement(Control control) {
		return ((Scale) control).getIncrement();
	}

	@Override
	protected int getMaximum(Control control) {
		return ((Scale) control).getMaximum();
	}

	@Override
	protected int getMinimum(Control control) {
		return ((Scale) control).getMinimum();
	}

	@Override
	protected int getPageIncrement(Control control) {
		return ((Scale) control).getPageIncrement();
	}

	@Override
	protected int getValue(Control control) {
		return ((Scale) control).getSelection();
	}

	@Override
	protected void setValue(Control control, int value) {
		((Scale) control).setSelection(value);
	}

	// test methods
	// /////////////

	public void testRidgetMapping() {
		SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertSame(ScaleRidget.class, mapper.getRidgetClass(getWidget()));
	}
}
