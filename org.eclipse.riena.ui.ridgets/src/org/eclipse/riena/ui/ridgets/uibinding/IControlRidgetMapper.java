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
package org.eclipse.riena.ui.ridgets.uibinding;

import org.eclipse.riena.ui.ridgets.IRidget;

/**
 * Interface for mapping UI control-classes to ridget-classes.
 */
public interface IControlRidgetMapper<C> {

	/**
	 * Returns the ridget-class for a UI control-class
	 * 
	 * @param controlClazz
	 *            is the class of the UI Control
	 * @return the mapped ridget-class
	 */
	Class<? extends IRidget> getRidgetClass(Class<? extends C> controlClazz);

	/**
	 * Returns the ridget-class for a UI control
	 * 
	 * @param control
	 *            the UI control
	 * @return the mapped ridget-class
	 */
	Class<? extends IRidget> getRidgetClass(C control);

	/**
	 * Adds a mapping of a UI control-class to a ridget-class
	 * 
	 * @param controlClazz -
	 *            the class of the UI control
	 * @param ridgetClazz -
	 *            the class of the ridget
	 */
	void addMapping(Class<? extends C> controlClazz, Class<? extends IRidget> ridgetClazz);

	/**
	 * Adds a special mapping of a specific UI-control to a Ridget class.
	 * 
	 * @param controlName
	 *            The name of the UI-control.
	 * @param ridgetClazz
	 *            The class of the ridget.
	 */
	void addSpecialMapping(String controlName, Class<? extends Object> ridgetClazz);

}
