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
 * Tests for the class {@link ScaleRidget}.
 */
public class ScaleRidgetTest extends AbstractTraverseRidgetTest {

	@Override
	protected Widget createWidget(final Composite parent) {
		return new Scale(parent, SWT.NONE);
	}

	@Override
	protected ITraverseRidget createRidget() {
		return new MyScaleRidget();
	}

	@Override
	protected Scale getWidget() {
		return (Scale) super.getWidget();
	}

	@Override
	protected ITraverseRidget getRidget() {
		return super.getRidget();
	}

	@Override
	protected int getIncrement(final Control control) {
		return ((Scale) control).getIncrement();
	}

	@Override
	protected int getMaximum(final Control control) {
		return ((Scale) control).getMaximum();
	}

	@Override
	protected int getMinimum(final Control control) {
		return ((Scale) control).getMinimum();
	}

	@Override
	protected int getPageIncrement(final Control control) {
		return ((Scale) control).getPageIncrement();
	}

	@Override
	protected int getValue(final Control control) {
		return ((Scale) control).getSelection();
	}

	@Override
	protected void setValue(final Control control, final int value) {
		((Scale) control).setSelection(value);
	}

	// test methods
	// /////////////

	public void testRidgetMapping() {
		final SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertSame(ScaleRidget.class, mapper.getRidgetClass(getWidget()));
	}

	/**
	 * Tests the <i>protected</i> method {@code initFromUIControl()}.
	 */
	public void testInitFromUIControl() {

		final Scale scale = getWidget();
		scale.setMaximum(22);
		scale.setMinimum(1);
		scale.setIncrement(2);
		scale.setPageIncrement(11);

		MyScaleRidget ridget = (MyScaleRidget) createRidget();
		scale.setMaximum(22);
		scale.setMinimum(1);
		scale.setIncrement(2);
		scale.setPageIncrement(11);
		ridget.setUIControl(scale);
		assertEquals(22, ridget.getMaximum());
		assertEquals(1, ridget.getMinimum());
		assertEquals(2, ridget.getIncrement());
		assertEquals(11, ridget.getPageIncrement());

		ridget = (MyScaleRidget) createRidget();
		ridget.setMaximum(300);
		ridget.setMinimum(2);
		ridget.setIncrement(3);
		ridget.setPageIncrement(10);
		scale.setMaximum(22);
		scale.setMinimum(1);
		scale.setIncrement(2);
		scale.setPageIncrement(11);
		ridget.setUIControl(scale);
		assertEquals(300, ridget.getMaximum());
		assertEquals(2, ridget.getMinimum());
		assertEquals(3, ridget.getIncrement());
		assertEquals(10, ridget.getPageIncrement());

		ridget = (MyScaleRidget) createRidget();
		ridget.setMaximum(33);
		ridget.setIncrement(3);
		scale.setMaximum(22);
		scale.setMinimum(1);
		scale.setIncrement(2);
		scale.setPageIncrement(11);
		ridget.setUIControl(scale); // 
		assertEquals(33, ridget.getMaximum());
		assertEquals(1, ridget.getMinimum());
		assertEquals(3, ridget.getIncrement());
		assertEquals(11, ridget.getPageIncrement());

	}

	/**
	 * This class extends the class {@code ScaleRidget} only make some protected
	 * methods public. So these methods can be tested easier.
	 */
	private class MyScaleRidget extends ScaleRidget {

		@Override
		public void initFromUIControl() {
			super.initFromUIControl();
		}

	}

}
