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
package org.eclipse.riena.navigation.ui.controllers;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;

/**
 * Utility methods used by controller implementations.
 */
public final class ControllerUtils {

	private ControllerUtils() {
		// prevent instanciation
	}

	/**
	 * Sets the blocked state of all given ridgets. A blocked ridget does not
	 * accept any user input an is not be focusable.
	 * 
	 * @param ridgets
	 *            the Ridgets to be blocked
	 * @param blocked
	 *            the blocked state
	 */
	public static void blockRidgets(Collection<? extends IRidget> ridgets, boolean blocked) {
		Iterator<? extends IRidget> iterator = ridgets.iterator();
		while (iterator.hasNext()) {
			IRidget object = iterator.next();
			object.setBlocked(blocked);
			if (object instanceof IRidgetContainer) {
				blockRidgets(((IRidgetContainer) object).getRidgets(), blocked);
			}
		}
	}

}
