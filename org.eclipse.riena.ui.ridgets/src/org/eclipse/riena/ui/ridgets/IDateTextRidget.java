/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
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
public interface IDateTextRidget extends ITextRidget {

	/** <code>FORMAT_DDMMYYYY</code> */
	String FORMAT_DDMMYYYY = "dd.MM.yyyy"; //$NON-NLS-1$
	/** <code>FORMAT_DDMMYY</code> */
	String FORMAT_DDMMYY = "dd.MM.yy"; //$NON-NLS-1$
	/** <code>FORMAT_DDMM</code> */
	String FORMAT_DDMM = "dd.MM"; //$NON-NLS-1$
	/** <code>FORMAT_MMYYYY</code> */
	String FORMAT_MMYYYY = "MM.yyyy"; //$NON-NLS-1$
	/** <code>FORMAT_YYYY</code> */
	String FORMAT_YYYY = "yyyy"; //$NON-NLS-1$
	/** <code>FORMAT_HHMMSS</code> */
	String FORMAT_HHMMSS = "HH:mm:ss"; //$NON-NLS-1$
	/** <code>FORMAT_HHMM</code> */
	String FORMAT_HHMM = "HH:mm"; //$NON-NLS-1$
	/** <code>FORMAT_DDMMYYYYHHMM</code> */
	String FORMAT_DDMMYYYYHHMM = "dd.MM.yyyy HH:mm"; //$NON-NLS-1$

	/**
	 * Sets the date pattern and adds validators and converters to convert to a
	 * java.util.Date in the model.
	 * 
	 * @param datePattern
	 *            The date pattern; never null
	 */
	void setFormat(String datePattern);

}
