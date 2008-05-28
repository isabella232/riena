/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.util.beans;

import java.util.Date;

/**
 * DateBean provides a Date value for simple adapter UI-Binding
 * 
 * @author Erich Achilles
 */

public class DateBean extends AbstractBean {

	/** Name of the date property (="value") */
	public static final String DATE_PROPERTY = "value"; //$NON-NLS-1$

	private Date date;

	/**
	 * Creates a date bean with a default null
	 */
	public DateBean() {
		date = null;
	}

	/**
	 * Creates a date bean with the given value
	 * 
	 * @param value
	 */
	public DateBean(Date value) {
		if (value != null) {
			this.date = (Date) value.clone();
		} else {
			this.date = null;
		}

	}

	/**
	 * Sets the value of this bean
	 * 
	 * @param value -
	 *            value to set
	 */
	public void setValue(Date value) {
		Object old = this.date;
		if (value != null) {
			this.date = (Date) value.clone();
		} else {
			this.date = null;
		}

		firePropertyChanged(DATE_PROPERTY, old, this.date);
	}

	/**
	 * Returns the value of this bean
	 * 
	 * @return value
	 */
	public Date getValue() {
		if (date != null) {
			return (Date) date.clone();
		}
		return null;

	}
}
