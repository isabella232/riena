/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import static org.eclipse.riena.ui.swt.utils.SwtUtilities.*;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;

import org.eclipse.riena.ui.ridgets.swt.AbstractSWTRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractToggleButtonRidget;

/**
 * Adapter of the SWT Widget <code>Button</code> with the style SWT.CHECK or
 * SWT.TOGGLE .
 */
public class ToggleButtonRidget extends AbstractToggleButtonRidget {

	@Override
	protected void checkUIControl(final Object uiControl) {
		AbstractSWTRidget.assertType(uiControl, Button.class);
		if (uiControl != null) {
			final Button button = (Button) uiControl;
			if (!(hasStyle(button, SWT.CHECK) || hasStyle(button, SWT.RADIO) || hasStyle(button, SWT.TOGGLE))) {
				throw new BindingException("Button must be a check box, a radio button or a toggle button"); //$NON-NLS-1$
			}
		}
	}

	@Override
	public Button getUIControl() {
		return (Button) super.getUIControl();
	}

	@Override
	protected ISWTObservableValue getUIControlSelectionObservable() {
		return SWTObservables.observeSelection(getUIControl());
	}

	@Override
	protected void setUIControlSelection(final boolean selected) {
		getUIControl().setSelection(selected);
	}

	@Override
	protected String getUIControlText() {
		return getUIControl().getText();
	}

	@Override
	protected void setUIControlText(final String text) {
		getUIControl().setText(text);
	}

	@Override
	protected void setUIControlImage(final Image image) {
		getUIControl().setImage(image);
	}

}
