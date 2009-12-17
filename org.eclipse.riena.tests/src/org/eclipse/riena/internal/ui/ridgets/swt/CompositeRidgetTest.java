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

import org.eclipse.core.databinding.BindingException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.ui.ridgets.ICompositeRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;

/**
 * Tests for {@link CompositeRidget}.
 */
public class CompositeRidgetTest extends AbstractSWTRidgetTest {

	@Override
	protected IRidget createRidget() {
		return new CompositeRidget();
	}

	@Override
	protected Widget createWidget(Composite parent) {
		Widget result = new Composite(parent, SWT.NONE);
		result.setData(SWTBindingPropertyLocator.BINDING_PROPERTY, "pbId");
		return result;
	}

	@Override
	protected ICompositeRidget getRidget() {
		return (ICompositeRidget) super.getRidget();
	}

	@Override
	protected Composite getWidget() {
		return (Composite) super.getWidget();
	}

	// testing methods
	//////////////////

	public void testRidgetMapping() {
		SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		Composite control = getWidget();

		assertNotNull(control.getData(SWTBindingPropertyLocator.BINDING_PROPERTY));
		assertSame(CompositeRidget.class, mapper.getRidgetClass(control));
	}

	public void testRidgetMappingWithNoId() {
		SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		Composite control = new Composite(getShell(), SWT.NONE);

		assertEquals(null, control.getData(SWTBindingPropertyLocator.BINDING_PROPERTY));
		try {
			mapper.getRidgetClass(control);
			fail();
		} catch (BindingException bex) {
			ok("expected");
		}

		control.setData(SWTBindingPropertyLocator.BINDING_PROPERTY, "");

		assertEquals("", control.getData(SWTBindingPropertyLocator.BINDING_PROPERTY));
		try {
			mapper.getRidgetClass(control);
			fail();
		} catch (BindingException bex) {
			ok("expected");
		}
	}

	@Override
	public void testGetFocusable() {
		createTextWidget();
		super.testGetFocusable();
	}

	@Override
	public void testSetFocusable() {
		createTextWidget();
		super.testSetFocusable();
	}

	@Override
	public void testRequestFocus() throws Exception {
		createTextWidget();
		super.testRequestFocus();
	}

	// helping methods
	//////////////////

	/*
	 * Giving focus assumes there is something in the composite than can receive
	 * it. This method will create a Text widget / ridget to set that up.
	 */
	private void createTextWidget() {
		Composite control = getWidget();
		Text txt = new Text(control, SWT.BORDER);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(txt, "txt");
		TextRidget txtRidget = new TextRidget();
		txtRidget.setUIControl(txt);

		ICompositeRidget ridget = getRidget();
		ridget.addRidget("txt", txtRidget);
	}
}
