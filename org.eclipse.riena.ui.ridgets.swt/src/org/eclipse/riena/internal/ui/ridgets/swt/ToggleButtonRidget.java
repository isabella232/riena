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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.BindingException;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;

/**
 * Adapter of the SWT Widget <code>Button</code> with the style SWT.CHECK or
 * SWT.TOGGLE .
 */
public class ToggleButtonRidget extends AbstractValueRidget implements IToggleButtonRidget {

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private final ActionObserver actionObserver;
	private Binding controlBinding;
	private String text;
	private String icon;
	private boolean selected;
	private boolean textAlreadyInitialized;
	private boolean useRidgetIcon;

	public ToggleButtonRidget() {
		super();
		actionObserver = new ActionObserver();
		textAlreadyInitialized = false;
		useRidgetIcon = false;
		addPropertyChangeListener(IMarkableRidget.PROPERTY_ENABLED, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				boolean isEnabled = ((Boolean) evt.getNewValue()).booleanValue();
				updateSelection(isEnabled);
			}
		});
	}

	@Override
	protected void checkUIControl(Object uiControl) {
		AbstractSWTRidget.assertType(uiControl, Button.class);
		if (uiControl != null) {
			Button uiButton = (Button) uiControl;
			int style = uiButton.getStyle();
			if ((style & SWT.CHECK) != SWT.CHECK && (style & SWT.TOGGLE) != SWT.TOGGLE
					&& (style & SWT.RADIO) != SWT.RADIO) {
				throw new BindingException("Button must be a check box, a radio button or a toggle button"); //$NON-NLS-1$
			}
		}
	}

	@Override
	protected void bindUIControl() {
		DataBindingContext context = getValueBindingSupport().getContext();
		Button control = getUIControl();
		if (control != null) {
			controlBinding = context.bindValue(SWTObservables.observeSelection(control), BeansObservables.observeValue(
					this, IToggleButtonRidget.PROPERTY_SELECTED), new UpdateValueStrategy(
					UpdateValueStrategy.POLICY_UPDATE), new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE)
					.setBeforeSetValidator(new CancelControlUpdateWhenDisabled()));
			initText();
			updateUIText();
			updateSelection(isEnabled());
			updateUIIcon();
		}
	}

	@Override
	protected void unbindUIControl() {
		if (controlBinding != null) {
			controlBinding.dispose();
			controlBinding = null;
		}
	}

	@Override
	IObservableValue getRidgetObservable() {
		return BeansObservables.observeValue(this, IToggleButtonRidget.PROPERTY_SELECTED);
	}

	/**
	 * Always returns true because mandatory markers do not make sense for this
	 * ridget.
	 */
	@Override
	public boolean isDisableMandatoryMarker() {
		return true;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		if (this.selected != selected) {
			boolean oldValue = this.selected;
			this.selected = selected;
			actionObserver.fireAction();
			firePropertyChange(IToggleButtonRidget.PROPERTY_SELECTED, Boolean.valueOf(oldValue), Boolean
					.valueOf(selected));
		}
	}

	@Override
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
		updateUIText();
	}

	public String getIcon() {
		return icon;
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

	// helping methods
	// ////////////////

	/**
	 * If the text of the ridget has no value, initialize it with the text of
	 * the UI control.
	 */
	private void initText() {
		if ((text == null) && (!textAlreadyInitialized)) {
			if ((getUIControl()) != null && !(getUIControl().isDisposed())) {
				text = getUIControl().getText();
				if (text == null) {
					text = EMPTY_STRING;
				}
				textAlreadyInitialized = true;
			}
		}
	}

	/**
	 * Update the selection state of this ridget's control (button)
	 * 
	 * @param isRidgetEnabled
	 *            true if this ridget is enabled, false otherwise
	 */
	private void updateSelection(boolean isRidgetEnabled) {
		Button control = getUIControl();
		if (control != null && MarkerSupport.HIDE_DISABLED_RIDGET_CONTENT) {
			if (!isRidgetEnabled) {
				control.setSelection(false);
			} else {
				control.setSelection(isSelected());
			}
		}
	}

	private void updateUIText() {
		Button control = getUIControl();
		if (control != null) {
			control.setText(text);
		}
	}

	/**
	 * Updates the images of the control.
	 */
	private void updateUIIcon() {
		Button control = getUIControl();
		if (control != null) {
			Image image = null;
			if (icon != null) {
				image = getManagedImage(icon);
			}
			if ((image != null) || useRidgetIcon) {
				control.setImage(image);
			}
		}
	}

	// helping classes
	//////////////////

	/**
	 * When the ridget is disabled, this validator will prevent the selected
	 * attribute of a control (Button) from changing -- unless
	 * HIDE_DISABLED_RIDGET_CONTENT is {@code false}.
	 */
	private final class CancelControlUpdateWhenDisabled implements IValidator {
		public IStatus validate(Object value) {
			boolean cancel = MarkerSupport.HIDE_DISABLED_RIDGET_CONTENT && !isEnabled();
			return cancel ? Status.CANCEL_STATUS : Status.OK_STATUS;
		}
	};

}
