/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
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

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.BindingException;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;

/**
 * Adapter of the SWT Widget <code>Button</code> with the style SWT.CHECK or
 * SWT.TOGGLE .
 */
public class ToggleButtonRidget extends AbstractValueRidget implements IToggleButtonRidget {

	private final ActionObserver actionObserver;
	private Binding controlBinding;
	private String text;
	private boolean selected;
	private boolean blocked;

	public ToggleButtonRidget() {
		this(null);
	}

	public ToggleButtonRidget(Button button) {
		super();
		actionObserver = new ActionObserver();
		setUIControl(button);
	}

	/**
	 * @deprecated use BeansObservables.observeValue(ridget instance,
	 *             IToggleButtonRidget.PROPERTY_SELECTED);
	 */
	public IObservableValue getRidgetObservable() {
		return BeansObservables.observeValue(this, IToggleButtonRidget.PROPERTY_SELECTED);
	}

	@Override
	protected void checkUIControl(Object uiControl) {
		AbstractSWTRidget.assertType(uiControl, Button.class);
		if (uiControl != null) {
			Button uiButton = (Button) uiControl;
			int style = uiButton.getStyle();
			if ((style & SWT.CHECK) != SWT.CHECK && (style & SWT.TOGGLE) != SWT.TOGGLE) {
				throw new BindingException("Button must be a check box or a toggle button"); //$NON-NLS-1$
			}
		}
	}

	@Override
	protected void bindUIControl() {
		DataBindingContext context = getValueBindingSupport().getContext();
		Button control = getUIControl();
		if (control != null) {
			controlBinding = context.bindValue(SWTObservables.observeSelection(control), BeansObservables.observeValue(
					this, IToggleButtonRidget.PROPERTY_SELECTED), null, null);
			updateText();
		}
	}

	@Override
	protected void unbindUIControl() {
		if (controlBinding != null) {
			controlBinding.dispose();
			controlBinding = null;
		}
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		if (this.selected != selected) {
			boolean oldValue = this.selected;
			this.selected = selected;
			// updateFromModel();
			actionObserver.fireAction();
			firePropertyChange(IToggleButtonRidget.PROPERTY_SELECTED, Boolean.valueOf(oldValue), Boolean
					.valueOf(selected));
		}
	}

	public Button getUIControl() {
		return (Button) super.getUIControl();
	}

	public void addListener(IActionListener listener) {
		actionObserver.addListener(listener);
	}

	public void addListener(Object target, String action) {
		IActionListener listener = EventHandler.create(IActionListener.class, target, action);
		actionObserver.addListener(listener);
	}

	public void removeListener(IActionListener listener) {
		actionObserver.removeListener(listener);
	}

	public final String getText() {
		return text;
	}

	public final void setText(String newText) {
		this.text = newText;
		updateText();
	}

	public String getIcon() {
		throw new UnsupportedOperationException("not implemented"); //$NON-NLS-1$
	}

	public void setIcon(String iconName) {
		throw new UnsupportedOperationException("not implemented"); //$NON-NLS-1$
	}

	// helping methods
	// ////////////////

	private void updateText() {
		Button button = getUIControl();
		if (button != null) {
			String buttonText = text == null ? "" : text; //$NON-NLS-1$
			button.setText(buttonText);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidget#isBlocked()
	 */
	public boolean isBlocked() {
		return blocked;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidget#setBlocked(boolean)
	 */
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;

	}

}
