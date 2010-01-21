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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * TODO [ev] Experimental. May go away.
 * <p>
 * TODO [ev] docs
 * 
 * @since 2.0
 */
public class DefaultButtonManager implements Listener, DisposeListener {

	private final Shell shell;

	private Map<Control, Button> control2button;

	public DefaultButtonManager(Shell shell) {
		Assert.isNotNull(shell);
		this.shell = shell;
	}

	public void addButton(Button button, Control focusControl) {
		Assert.isNotNull(button);
		Assert.isNotNull(focusControl);
		if (control2button == null) {
			control2button = new HashMap<Control, Button>();
			Display display = shell.getDisplay();
			display.addFilter(SWT.FocusIn, this);
		}
		if (control2button.put(focusControl, button) == null) {
			focusControl.addDisposeListener(this);
		}
	}

	/**
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public void handleEvent(Event event) {
		if (SWT.FocusIn == event.type && event.widget instanceof Control) {
			Button button = findDefaultButton((Control) event.widget);
			if (button != shell.getDefaultButton()) {
				System.out.println("Focus on: " + event.widget + ", " + button);
				shell.setDefaultButton(button);
			}
		}
	}

	/**
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public void widgetDisposed(DisposeEvent e) {
		if (control2button != null) {
			control2button.remove(e.widget);
			if (control2button.isEmpty()) {
				dispose();
			}
		}
	}

	// helping methods
	//////////////////

	private void dispose() {
		System.out.println("Dispose: " + this);
		Display display = shell.getDisplay();
		display.removeFilter(SWT.FocusIn, this);
		control2button = null;
	}

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
