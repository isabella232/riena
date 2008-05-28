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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.riena.ui.ridgets.IEditableRidget;

/**
 * Abstract implementation of an {@link IEditableRidget} for SWT.
 */
public abstract class AbstractEditableRidget extends AbstractValueRidget implements IEditableRidget {

	public void addValidationRule(IValidator validationRule) {
		boolean onEditValidatorsChanged = getValueBindingSupport().addValidationRule(validationRule);
		if (onEditValidatorsChanged) {
			// TODO updateValidationRulesToUIControl();
		}
	}

	public IConverter getUIControlToModelConverter() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public Collection<IValidator> getValidationRules() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public void removeValidationRule(IValidator validationRule) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public void setUIControlToModelConverter(IConverter converter) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public void validationRulesChecked(IStatus status) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
