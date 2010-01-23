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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IDefaultActionManager;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IWindowRidget;

/**
 * Experimental. May go away.
 * <p>
 * TODO [ev] docs, tests
 * 
 * @since 2.0
 */
final class DefaultActionManager implements IDefaultActionManager, Listener {

	private final IWindowRidget windowRidget;
	private final Map<IRidget, IActionRidget> ridget2button;

	private Map<Control, Button> control2button;
	private Shell shell;
	private Display display;

	DefaultActionManager(IWindowRidget windowRidget) {
		Assert.isNotNull(windowRidget);
		this.windowRidget = windowRidget;
		ridget2button = new HashMap<IRidget, IActionRidget>(1);
	}

	public void addAction(IActionRidget ridget, IRidget focusRidget) {
		Assert.isNotNull(ridget);
		Assert.isNotNull(focusRidget);
		if (ridget2button == null) {
		}
		ridget2button.put(focusRidget, ridget);
	}

	public void activate() {
		Assert.isTrue(control2button == null);
		if (control2button == null) {
			shell = ((Control) windowRidget.getUIControl()).getShell();
			display = shell.getDisplay();
			control2button = new HashMap<Control, Button>();
			for (Map.Entry<IRidget, IActionRidget> entry : ridget2button.entrySet()) {
				Control control = (Control) entry.getKey().getUIControl();
				Assert.isNotNull(control);

				Button button = (Button) entry.getValue().getUIControl();
				Assert.isNotNull(button);

				control2button.put(control, button);
			}
			display.addFilter(SWT.FocusIn, this);
		}
	}

	public void deactivate() {
		if (display != null) {
			display.removeFilter(SWT.FocusIn, this);
		}
		display = null;
		shell = null;
		control2button = null;
	}

	public void dispose() {
		deactivate();
		ridget2button.clear();
	}

	/**
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public void handleEvent(Event event) {
		if (SWT.FocusIn == event.type && event.widget instanceof Control) {
			Button button = findDefaultButton((Control) event.widget);
			if (button != null && button != shell.getDefaultButton()) {
				// System.out.println("Focus on: " + event.widget + ", " + button);
				shell.setDefaultButton(button);
			}
		}
	}

	// helping methods
	//////////////////

	private Button findDefaultButton(Control start) {
		Button result = null;
		Control control = start;
		while (result == null && control != null) {
			result = control2button.get(control);
			control = control.getParent();
		}
		return result;
	}

}
