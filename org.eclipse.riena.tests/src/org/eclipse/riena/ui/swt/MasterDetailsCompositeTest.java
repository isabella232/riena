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
package org.eclipse.riena.ui.swt;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Tests of the class {@link MasterDetailsComposite}
 */
@UITestCase
public class MasterDetailsCompositeTest extends TestCase {

	private Shell shell;
	private MasterDetailsComposite masterDetails;

	@Override
	protected void setUp() throws Exception {
		shell = new Shell();
		masterDetails = new MasterDetailsComposite(shell, SWT.NONE);
	}

	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.disposeWidget(shell);
	}

	public void testGetUIControlsWithBindingProperty() {
		Text txtFirstName = UIControlsFactory.createText(masterDetails.getDetails(), SWT.BORDER, "txtFirstName");
		Text txtLastName = UIControlsFactory.createText(masterDetails.getDetails(), SWT.BORDER, "txtLastName");
		Text txtWithoutID = UIControlsFactory.createText(masterDetails.getDetails(), SWT.BORDER);

		List<Object> controls = masterDetails.getUIControls();

		assertNotNull(controls);
		assertTrue(controls.contains(txtFirstName));
		assertTrue(controls.contains(txtLastName));

		assertFalse(controls.contains(txtWithoutID));
	}

	public void testGetUIControlsWithBindingPropertyFromComposite() {
		Composite detail = new Composite(masterDetails.getDetails(), SWT.NONE);

		Text txtFirstName = UIControlsFactory.createText(detail, SWT.BORDER, "txtFirstName");
		Text txtLastName = UIControlsFactory.createText(detail, SWT.BORDER, "txtLastName");
		Text txtWithoutID = UIControlsFactory.createText(detail, SWT.BORDER);

		List<Object> controls = masterDetails.getUIControls();

		assertNotNull(controls);
		assertTrue(controls.contains(txtFirstName));
		assertTrue(controls.contains(txtLastName));

		assertFalse(controls.contains(txtWithoutID));
	}
}
