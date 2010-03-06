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

import org.eclipse.core.runtime.Assert;

import org.eclipse.riena.ui.ridgets.AbstractRidget;
import org.eclipse.riena.ui.ridgets.IInfoFlyoutRidget;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingPropertyLocator;
import org.eclipse.riena.ui.swt.InfoFlyout;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;

/**
 * A ridget for the {@link InfoFlyout} widget.
 */
public class InfoFlyoutRidget extends AbstractRidget implements IInfoFlyoutRidget {

	private InfoFlyout infoFlyout;

	public InfoFlyoutRidget() {
		super();
	}

	public boolean isVisible() {
		throw new UnsupportedOperationException("not supported"); //$NON-NLS-1$
	}

	public void setVisible(boolean visible) {
		throw new UnsupportedOperationException("not supported"); //$NON-NLS-1$
	}

	public boolean isEnabled() {
		throw new UnsupportedOperationException("not supported"); //$NON-NLS-1$
	}

	public void setEnabled(boolean enabled) {
		throw new UnsupportedOperationException("not supported"); //$NON-NLS-1$
	}

	public InfoFlyout getUIControl() {
		return infoFlyout;
	}

	public void setUIControl(Object uiControl) {
		infoFlyout = (InfoFlyout) uiControl;
	}

	public void requestFocus() {
		throw new UnsupportedOperationException("not supported"); //$NON-NLS-1$
	}

	public boolean hasFocus() {
		return false;
	}

	public boolean isFocusable() {
		return false;
	}

	public void setFocusable(boolean focusable) {
		throw new UnsupportedOperationException("not supported"); //$NON-NLS-1$
	}

	public String getToolTipText() {
		throw new UnsupportedOperationException("not supported"); //$NON-NLS-1$
	}

	public void setToolTipText(String toolTipText) {
		throw new UnsupportedOperationException("not supported"); //$NON-NLS-1$
	}

	public String getID() {
		IBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
		return locator.locateBindingProperty(getUIControl());
	}

	public void setIcon(String icon) {
		getUIControl().setIcon(icon);
	}

	public void setMessage(String message) {
		Assert.isNotNull(message);
		getUIControl().setMessage(message);
	}

	public void open() {
		getUIControl().open();

	}
}
