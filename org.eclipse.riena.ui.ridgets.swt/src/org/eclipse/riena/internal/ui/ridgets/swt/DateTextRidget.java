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

import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.IDateTextFieldRidget;
import org.eclipse.riena.ui.ridgets.databinding.DateToStringConverter;
import org.eclipse.riena.ui.ridgets.databinding.StringToDateConverter;
import org.eclipse.riena.ui.ridgets.validation.ValidDate;
import org.eclipse.riena.ui.ridgets.validation.ValidIntermediateDate;

/**
 * TODO [ev] docs
 */
public class DateTextRidget extends TextRidget implements IDateTextFieldRidget {

	private ValidDate validDateRule;
	private ValidIntermediateDate validIntermediateDateRule;
	private StringToDateConverter uiControlToModelconverter;
	private DateToStringConverter modelToUIControlConverter;

	public DateTextRidget() {
		setFormat(IDateTextFieldRidget.FORMAT_DDMMYYYY);
	}

	public final void setFormat(String datePattern) {
		removeValidationRule(validDateRule);
		removeValidationRule(validIntermediateDateRule);

		validDateRule = new ValidDate(datePattern);
		validIntermediateDateRule = new ValidIntermediateDate(datePattern);
		uiControlToModelconverter = new StringToDateConverter(datePattern);
		modelToUIControlConverter = new DateToStringConverter(datePattern);

		addValidationRule(validDateRule, ValidationTime.ON_UPDATE_TO_MODEL);
		addValidationRule(validIntermediateDateRule, ValidationTime.ON_UI_CONTROL_EDIT);
		setUIControlToModelConverter(uiControlToModelconverter);
		setModelToUIControlConverter(modelToUIControlConverter);
	}

}
