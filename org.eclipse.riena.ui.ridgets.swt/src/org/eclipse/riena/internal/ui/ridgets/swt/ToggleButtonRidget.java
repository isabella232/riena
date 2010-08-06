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
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.internal.databinding.property.value.SimplePropertyObservableValue;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.internal.databinding.swt.ButtonSelectionProperty;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;

import org.eclipse.riena.ui.ridgets.swt.AbstractSWTRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractToggleButtonRidget;

/**
 * Adapter of the SWT Widget <code>Button</code> with the style SWT.CHECK,
 * SWT.RADIO or SWT.TOGGLE.
 */
// Note: TBR and ATBR could be merged - TBR is the only subclass
public class ToggleButtonRidget extends AbstractToggleButtonRidget {

	private IObservableValue selectionObservable;

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
	protected IObservableValue getUIControlSelectionObservable() {
		if (selectionObservable == null) {
			selectionObservable = new SelectionObservableWithOutputOnly(getUIControl());
		}
		return selectionObservable;
	}

	@Override
	protected void unbindUIControl() {
		if (selectionObservable != null) {
			selectionObservable.dispose();
			selectionObservable = null;
		}
		super.unbindUIControl();
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

	// helping classes
	//////////////////

	/**
	 * Custom IObservableValue that will revert selection changes when the
	 * ridget is output-only.
	 * 
	 * @see http://bugs.eclipse.org/271762
	 * @see http://bugs.eclipse.org/321935
	 */
	private final class SelectionObservableWithOutputOnly extends SimplePropertyObservableValue implements
			SelectionListener {

		private final Button button;

		public SelectionObservableWithOutputOnly(final Button source) {
			super(getValueBindingSupport().getContext().getValidationRealm(), source, new ButtonSelectionProperty());
			Assert.isNotNull(source);
			this.button = source;
			this.button.addSelectionListener(this);
		}

		@Override
		protected Object doGetValue() {
			return isOutputOnly() ? Boolean.valueOf(isSelected()) : super.doGetValue();
		}

		public void widgetSelected(final org.eclipse.swt.events.SelectionEvent e) {
			if (isOutputOnly()) {
				button.setSelection(isSelected());
			}
		}

		public void widgetDefaultSelected(final org.eclipse.swt.events.SelectionEvent e) {
			// unused
		}

		@Override
		public synchronized void dispose() {
			if (!button.isDisposed()) {
				button.removeSelectionListener(this);
			}
		}
	}

}
