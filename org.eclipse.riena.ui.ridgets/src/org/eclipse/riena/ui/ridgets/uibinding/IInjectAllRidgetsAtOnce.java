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
import org.eclipse.riena.ui.ridgets.IRidgetContainer;

/**
 * The {@link InjectAllAtOnceBindingManager} binding policy requires the
 * {@link IRidgetContainer} to implement this interface.
 */
public interface IInjectAllRidgetsAtOnce extends IRidgetContainer {

	/**
	 * Configure the ridgets of the receiver. This method should be called by
	 * the IBindingManager AFTER the ridget has been added the to collection of
	 * ridgets known to the receiver via
	 * {@link IRidgetContainer#addRidget(String, IRidget)} but is usually called
	 * BEFORE the ridget is bound to the ui control.
	 */
	void configureRidgets();
}
