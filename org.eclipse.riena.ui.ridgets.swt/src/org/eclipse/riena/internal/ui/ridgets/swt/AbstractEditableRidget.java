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

import java.util.Collection;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.riena.ui.core.marker.IMessageMarker;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.IEditableRidget;
import org.eclipse.riena.ui.ridgets.IValidationCallback;
import org.eclipse.riena.ui.ridgets.validation.IValidationRuleStatus;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * Abstract implementation of an {@link IEditableRidget} for SWT.
 */
public abstract class AbstractEditableRidget extends AbstractValueRidget implements IEditableRidget {

	/**
	 * The 'error' background color is shown for this duration of time, in
	 * miliseconds ({@value} ).
	 */
	protected static final int FLASH_DURATION_MS = 300;

	private boolean isFlashInProgress = false;

	public void addValidationRule(IValidator validationRule, ValidationTime validationTime) {
		Assert.isNotNull(validationRule);
		Assert.isNotNull(validationTime);
		getValueBindingSupport().addValidationRule(validationRule, validationTime);
	}

	public IConverter getUIControlToModelConverter() {
		return getValueBindingSupport().getUIControlToModelConverter();
	}

	public Collection<IValidator> getValidationRules() {
		return getValueBindingSupport().getValidationRules();
	}

	public void removeValidationRule(IValidator validationRule) {
		getValueBindingSupport().removeValidationRule(validationRule);
	}

	public void setUIControlToModelConverter(IConverter converter) {
		getValueBindingSupport().setUIControlToModelConverter(converter);
	}

	/**
	 * Subclasses should call this method to update validation state of the
	 * ridget.
	 * 
	 * @see IValidationCallback#validationRulesChecked(IStatus)
	 * 
	 * @param status
	 *            The result of validation.
	 */
	public void validationRulesChecked(IStatus status) {
		getValueBindingSupport().validationRulesChecked(status);
		if (status.isOK()) {
			setErrorMarked(false);
		} else {
			if (status.getCode() != IValidationRuleStatus.ERROR_BLOCK_WITH_FLASH) {
				setErrorMarked(true);
			} else {
				flash();
			}
		}
	}

	public void addValidationMessage(IMessageMarker messageMarker, IValidator validationRule) {
		getValueBindingSupport().addValidationMessage(messageMarker, validationRule);
	}

	public void addValidationMessage(IMessageMarker messageMarker) {
		getValueBindingSupport().addValidationMessage(messageMarker);
	}

	public void addValidationMessage(String message, IValidator validationRule) {
		getValueBindingSupport().addValidationMessage(message, validationRule);
	}

	public void addValidationMessage(String message) {
		getValueBindingSupport().addValidationMessage(message);
	}

	public void removeValidationMessage(IMessageMarker messageMarker, IValidator validationRule) {
		getValueBindingSupport().removeValidationMessage(messageMarker, validationRule);
	}

	public void removeValidationMessage(IMessageMarker messageMarker) {
		getValueBindingSupport().removeValidationMessage(messageMarker);
	}

	public void removeValidationMessage(String message, IValidator validationRule) {
		getValueBindingSupport().removeValidationMessage(message, validationRule);
	}

	public void removeValidationMessage(String message) {
		getValueBindingSupport().removeValidationMessage(message);
	}

	// protected methods
	////////////////////

	/**
	 * Toggles the background color of the control to
	 * {@link SharedColors#COLOR_FLASH_ERROR} for a short duration of time.
	 */
	protected synchronized final void flash() {
		final Control control = getUIControl();
		if (!isFlashInProgress && control != null) {
			isFlashInProgress = true;

			final Display display = control.getDisplay();
			final Color oldBgColor = control.getBackground();
			Color bgColor = Activator.getSharedColor(display, SharedColors.COLOR_FLASH_ERROR);
			control.setBackground(bgColor);
			Runnable op = new Runnable() {
				public void run() {
					try {
						Thread.sleep(AbstractEditableRidget.FLASH_DURATION_MS);
					} catch (InterruptedException e) {
						// ignore
					} finally {
						if (!control.isDisposed()) {
							display.syncExec(new Runnable() {
								public void run() {
									control.setBackground(oldBgColor);
								}
							});
						}
						isFlashInProgress = false;
					}
				}
			};
			new Thread(op).start();
		}
	}

}
