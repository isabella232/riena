/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
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
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.IDateTextRidget;
import org.eclipse.riena.ui.ridgets.swt.DefaultRealm;
import org.eclipse.riena.ui.swt.CompletionCombo;
import org.eclipse.riena.ui.swt.DatePickerComposite;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Tests for {@link DisabledMarkerVisualizer}.
 */
@UITestCase
public class DisabledMarkerVisualizerTest extends RienaTestCase {

	private Shell shell;
	private DefaultRealm realm;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		shell = new Shell();
		realm = new DefaultRealm();
	}

	@Override
	protected void tearDown() throws Exception {
		if (shell != null) {
			shell.dispose();
			shell = null;
		}
		if (realm != null) {
			realm.dispose();
			realm = null;
		}
		super.tearDown();
	}

	// testing methods
	//////////////////

	public void testGetChildrenFromComposite() {
		final Composite parent = new Composite(shell, SWT.NONE);
		new Label(parent, SWT.NONE);
		new Text(parent, SWT.NONE);

		final DisabledMarkerVisualizer visualizer = new DisabledMarkerVisualizer(null);
		final Control[] result = ReflectionUtils.invokeHidden(visualizer, "getChildren", parent);

		assertEquals(2, result.length);
		assertTrue(result[0] instanceof Label);
		assertTrue(result[1] instanceof Text);
	}

	/**
	 * As per Bug 318301.
	 */
	public void testGetChildrenFromCCombo() {
		final CCombo parent = new CCombo(shell, SWT.NONE);

		final DisabledMarkerVisualizer visualizer = new DisabledMarkerVisualizer(null);
		final Control[] result = ReflectionUtils.invokeHidden(visualizer, "getChildren", parent);

		assertEquals(2, result.length);
		assertTrue(result[0] instanceof Text);
		assertTrue(result[1] instanceof Button);
	}

	/**
	 * As per Bug 322030
	 */
	public void testVisualizerAndDatePickerEnablement() {
		final IDateTextRidget ridget = new DateTextRidget();
		final DatePickerComposite control = new DatePickerComposite(shell, SWT.NONE);
		final Button pickerButton = ReflectionUtils.getHidden(control, "pickerButton");
		ridget.setUIControl(control);
		ridget.setOutputOnly(true);

		assertFalse(pickerButton.isEnabled());

		final DisabledMarkerVisualizer visualizer = new DisabledMarkerVisualizer(ridget);
		ReflectionUtils.invokeHidden(visualizer, "updateDisabled", control, false);

		assertFalse(pickerButton.isEnabled());

		ReflectionUtils.invokeHidden(visualizer, "updateDisabled", control, true);

		assertFalse(pickerButton.isEnabled());
	}

	/**
	 * As per Bug 322030
	 */
	public void testVisualizerAndCompletionComboEnablement() {
		final IComboRidget ridget = new CompletionComboRidget();
		final CompletionCombo control = UIControlsFactory.createCompletionCombo(shell, SWT.NONE);
		final Button arrow = ReflectionUtils.getHidden(control, "arrow");
		ridget.setUIControl(control);
		ridget.setOutputOnly(true);

		assertFalse(arrow.isEnabled());

		final DisabledMarkerVisualizer visualizer = new DisabledMarkerVisualizer(ridget);
		ReflectionUtils.invokeHidden(visualizer, "updateDisabled", control, false);

		assertFalse(arrow.isEnabled());

		ReflectionUtils.invokeHidden(visualizer, "updateDisabled", control, true);

		assertFalse(arrow.isEnabled());
	}
}
