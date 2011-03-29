/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
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

import org.eclipse.riena.ui.ridgets.IRidget;

/**
 * Utility methods used by controller implementations.
 */
public final class ControllerUtils {

	private ControllerUtils() {
		// prevent instanciation
	}

	/**
	 * This was never implemented.
	 * 
	 * @deprecated never implemented - do not call
	 */
	@Deprecated
	public static void blockRidgets(final Collection<? extends IRidget> ridgets, final boolean blocked) {
	}

}
