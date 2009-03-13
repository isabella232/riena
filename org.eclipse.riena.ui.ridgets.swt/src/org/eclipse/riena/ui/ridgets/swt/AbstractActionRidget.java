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
package org.eclipse.riena.ui.ridgets.swt;

import java.beans.EventHandler;

import org.eclipse.swt.graphics.Image;

import org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.ActionObserver;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;

/**
 *
 */
public abstract class AbstractActionRidget extends AbstractSWTRidget implements IActionRidget {
	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private String text;
	private String icon;
	protected ActionObserver actionObserver;
	private boolean textAlreadyInitialized;
	private boolean useRidgetIcon;

	public AbstractActionRidget() {
		actionObserver = new ActionObserver();
		textAlreadyInitialized = false;
		useRidgetIcon = false;
	}

	/**
	 * If the text of the ridget has no value, initialize it with the text of
	 * the UI control.
	 */
	protected void initText() {
		if ((text == null) && (!textAlreadyInitialized)) {
			if ((getUIControl()) != null && !(getUIControl().isDisposed())) {
				text = getUIControlText();
				if (text == null) {
					text = EMPTY_STRING;
				}
				textAlreadyInitialized = true;
			}
		}
	}

	protected abstract String getUIControlText();

	public final void addListener(IActionListener listener) {
		actionObserver.addListener(listener);
	}

	public final void addListener(Object target, String action) {
		addListener(EventHandler.create(IActionListener.class, target, action));
	}

	public final void removeListener(IActionListener listener) {
		actionObserver.removeListener(listener);
	}

	/**
	 * Always returns true because mandatory markers do not make sense for this
	 * ridget.
	 */
	@Override
	public boolean isDisableMandatoryMarker() {
		return true;
	}

	public String getIcon() {
		return icon;
	}

	public final String getText() {
		return text;
	}

	public void setIcon(String icon) {
		boolean oldUseRidgetIcon = useRidgetIcon;
		useRidgetIcon = true;
		String oldIcon = this.icon;
		this.icon = icon;
		if (hasChanged(oldIcon, icon) || !oldUseRidgetIcon) {
			updateUIIcon();
		}
	}

	public final void setText(String newText) {
		this.text = newText;
		updateUIText();
	}

	// helping methods
	// ////////////////

	protected void updateUIText() {
		if (getUIControl() != null) {
			setUIControlText(getText());
		}
	}

	protected abstract void setUIControlText(String text);

	protected void updateUIIcon() {
		if (getUIControl() != null) {
			Image image = null;
			if (icon != null) {
				image = getManagedImage(icon);
			}
			if ((image != null) || useRidgetIcon) {
				setUIControlImage(image);
			}
		}
	}

	protected abstract void setUIControlImage(Image image);

}
