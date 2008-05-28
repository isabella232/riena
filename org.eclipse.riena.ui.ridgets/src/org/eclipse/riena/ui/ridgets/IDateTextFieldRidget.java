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
package org.eclipse.riena.ui.ridgets;

/**
 * Ridget for a date text field.
 */
public interface IDateTextFieldRidget extends ITextFieldRidget {

	/** <code>FORMAT_DDMMYYYY</code> */
	String FORMAT_DDMMYYYY = "dd.MM.yyyy";
	/** <code>FORMAT_DDMMYY</code> */
	String FORMAT_DDMMYY = "dd.MM.yy";
	/** <code>FORMAT_DDMM</code> */
	String FORMAT_DDMM = "dd.MM";
	/** <code>FORMAT_MMYYYY</code> */
	String FORMAT_MMYYYY = "MM.yyyy";
	/** <code>FORMAT_YYYY</code> */
	String FORMAT_YYYY = "yyyy";
	/** <code>FORMAT_HHMMSS</code> */
	String FORMAT_HHMMSS = "HH:mm:ss";
	/** <code>FORMAT_HHMM</code> */
	String FORMAT_HHMM = "HH:mm";
	/** <code>FORMAT_DDMMYYYYHHMM</code> */
	String FORMAT_DDMMYYYYHHMM = "dd.MM.yyyy HH:mm";

	/**
	 * Sets the date pattern and adds validators and converters to convert to a
	 * java.util.Date in the model.
	 * 
	 * @param datePattern
	 *            The date pattern.
	 */
	void setFormat(String datePattern);

}
