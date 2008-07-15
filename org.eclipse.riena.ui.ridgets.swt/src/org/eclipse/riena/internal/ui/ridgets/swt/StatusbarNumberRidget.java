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

import org.eclipse.riena.ui.ridgets.IStatusbarNumberRidget;
import org.eclipse.riena.ui.swt.StatusbarNumber;

/**
 * Ridget for composite of the status bar to display a number (
 * {@link StatusbarNumber}).
 */
public class StatusbarNumberRidget extends AbstractSWTRidget implements IStatusbarNumberRidget {

	private Integer number;
	private String numberString;

	/**
	 * @see org.eclipse.riena.ui.ridgets.IStatusbarNumberRidget#getNumber()
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IStatusbarNumberRidget#getNumberString()
	 */
	public String getNumberString() {
		return numberString;
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IStatusbarNumberRidget#setNumber(java.lang
	 *      .Integer)
	 */
	public void setNumber(Integer number) {
		this.number = number;

		if (this.number == null) {
			numberString = ""; //$NON-NLS-1$
		} else {
			// TODO Numberformatter nutzen
			numberString = number.toString();
		}
		getUIControl().setNumber(number);
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IStatusbarNumberRidget#setNumberString(java
	 *      .lang.String)
	 */
	public void setNumberString(String numberStrg) {
		this.numberString = numberStrg;

		if (this.numberString == null) {
			number = null;
		} else {
			// TODO Numberformatter nutzen
			number = Integer.getInteger(numberString);
		}
		getUIControl().setNumber(numberString);
	}

	/**
	 * @see org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidget#getUIControl()
	 */
	@Override
	public StatusbarNumber getUIControl() {
		return (StatusbarNumber) super.getUIControl();
	}

	/**
	 * @see org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidget#checkUIControl
	 *      (java.lang.Object)
	 */
	@Override
	protected void checkUIControl(Object uiControl) {
		AbstractSWTRidget.assertType(uiControl, StatusbarNumber.class);
	}

	/**
	 * @see org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidget#bindUIControl
	 *      ()
	 */
	@Override
	protected void bindUIControl() {
		// unused
	}

	/**
	 * @see org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidget#unbindUIControl()
	 */
	@Override
	protected void unbindUIControl() {
		// unused
	}

}
