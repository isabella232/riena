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

import java.util.Date;

import org.eclipse.core.databinding.observable.value.IObservableValue;

/**
 * Ridget for a date / time field.
 */
public interface IDateTimeRidget extends IMarkableRidget {

	// TODO [ev] extend IValueRidget ?

	/**
	 * TODO [ev] docs
	 */
	public void bindToModel(IObservableValue observableValue);

	/**
	 * TODO [ev] docs
	 */
	public void bindToModel(Object pojo, String propertyName);

	/**
	 * TODO [ev] docs
	 */
	Date getDate();

	/**
	 * TODO [ev] docs
	 */
	void setDate(Date date);

}
