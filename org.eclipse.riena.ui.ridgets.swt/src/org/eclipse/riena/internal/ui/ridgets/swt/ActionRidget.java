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

import java.beans.EventHandler;

import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;

public class ActionRidget extends AbstractMarkableRidget implements IActionRidget {

	private Button button;
	private String text;
	private String icon;
	private ActionObserver actionObserver;

	public ActionRidget() {
		actionObserver = new ActionObserver();
	}

	@Override
	protected void checkUIControl(Object uiControl) {
		assertType(uiControl, Button.class);
	}

	@Override
	protected void bindUIControl() {
		Button control = getUIControl();
		if (control != null) {
			button = control;
			if (text == null) {
				text = button.getText();
			}
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

	/**
	 * @see org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidget#getUIControl()
	 */
	@Override
	public Button getUIControl() {
		return (Button) super.getUIControl();
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

	/**
	 * @see org.eclipse.riena.ui.ridgets.IActionRidget#getIcon()
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IActionRidget#setIcon(java.lang.String)
	 */
	public void setIcon(String icon) {
		String oldIcon = this.icon;
		this.icon = icon;
		if (hasChanged(oldIcon, icon)) {
			updateIconInControl();
		}
	}

	// helping methods
	// ////////////////

	private void updateText() {
		if (button != null) {
			String buttonText = text == null ? "" : text; //$NON-NLS-1$
			button.setText(buttonText);
		}
	}

	private void updateIconInControl() {
		Button control = getUIControl();
		if (control != null) {
			Image image = null;
			if (icon != null) {
				image = getManagedImage(icon);
			}
			control.setImage(image);
		}
	}

}
