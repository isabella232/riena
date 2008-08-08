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
package org.eclipse.riena.ui.ridgets.databinding;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.databinding.conversion.Converter;

/**
 * Converts a String that matches a given pattern to a Date.
 * 
 * @see #org.eclipse.riena.ui.ridgets.databinding.StringToDateValidator
 */
public class StringToDateConverter extends Converter {

	private DateFormat format;

	/**
	 * Creates a StringToDateConverter.
	 * 
	 * @param pattern
	 *            The pattern to match e.g. MM/dd/yyyy.
	 */
	public StringToDateConverter(String pattern) {
		super(String.class, Date.class);
		format = new SimpleDateFormat(pattern);
	}

	/**
	 * @see org.eclipse.core.databinding.conversion.IConverter#convert(java.lang.Object)
	 */
	public Object convert(Object fromObject) {
		if (fromObject == null || "".equals(fromObject)) { //$NON-NLS-1$
			return null;
		}
		try {
			return format.parse((String) fromObject);
		} catch (ParseException e) {
			throw new ConversionFailure("Cannot convert \"" + fromObject + "\" to a java.util.Date.", e); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

}
