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
package org.eclipse.riena.ui.ridgets.marker;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.riena.core.marker.AbstractMarker;
import org.eclipse.riena.ui.core.marker.IMessageMarker;

public class ValidationMessageMarker extends AbstractMarker implements IMessageMarker {

	private static final String MESSAGE_MARKER_ATTRIBUTE = "wrappedMessageMarker"; //$NON-NLS-1$

	private IValidator validationRule;

	public ValidationMessageMarker(IMessageMarker messageMarker) {
		super(false);
		setAttribute(MESSAGE_MARKER_ATTRIBUTE, messageMarker);
	}

	public ValidationMessageMarker(IMessageMarker messageMarker, IValidator validationRule) {
		this(messageMarker);
		this.validationRule = validationRule;
	}

	public String getMessage() {
		return getMessageMarker().getMessage();
	}

	public IValidator getValidationRule() {
		return validationRule;
	}

	public IMessageMarker getMessageMarker() {
		return (IMessageMarker) getAttribute(MESSAGE_MARKER_ATTRIBUTE);
	}

	/**
	 * @see org.eclipse.riena.core.marker.AbstractMarker#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof ValidationMessageMarker) {
			ValidationMessageMarker otherValidationMessageMarker = (ValidationMessageMarker) other;
			return super.equals(other)
					&& ((getValidationRule() == null && otherValidationMessageMarker.getValidationRule() == null) || (getValidationRule() != null && getValidationRule()
							.equals(otherValidationMessageMarker.getValidationRule())));
		}
		return false;
	}

	/**
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

}
