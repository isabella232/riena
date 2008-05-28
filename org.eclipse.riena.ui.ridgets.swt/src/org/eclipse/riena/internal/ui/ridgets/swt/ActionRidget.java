/*******************************************************************************
 * Copyright (c) 2008 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.beans.EventHandler;

import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;

public class ActionRidget extends AbstractMarkableRidget implements IActionRidget {

	private String text;
	private Button button;
	private ActionObserver actionObserver;

	public ActionRidget() {
		actionObserver = new ActionObserver();
	}

	protected void checkUIControl(Object uiControl) {
		assertType(uiControl, Button.class);
	}

	@Override
	protected void bindUIControl() {
		Control control = getUIControl();
		if (control != null) {
			button = (Button) control;
			button.addSelectionListener(actionObserver);
			updateText();
		}
	}

	@Override
	protected void unbindUIControl() {
		if (button != null) {
			button.removeSelectionListener(actionObserver);
			button = null;
		}
	}

	public final void addListener(IActionListener listener) {
		actionObserver.addListener(listener);
	}

	public final void addListener(Object target, String action) {
		addListener(EventHandler.create(IActionListener.class, target, action));
	}

	public final void removeListener(IActionListener listener) {
		actionObserver.removeListener(listener);
	}

	public final String getText() {
		return text;
	}

	public final void setText(String newText) {
		this.text = newText;
		updateText();
	}

	public final String getIcon() {
		throw new UnsupportedOperationException("not implemented"); //$NON-NLS-1$
	}

	public final void setIcon(String iconName) {
		throw new UnsupportedOperationException("not implemented"); //$NON-NLS-1$
	}

	// helping methods
	// ////////////////

	private void updateText() {
		if (button != null) {
			String buttonText = text == null ? "" : text; //$NON-NLS-1$
			button.setText(buttonText);
		}
	}

}
