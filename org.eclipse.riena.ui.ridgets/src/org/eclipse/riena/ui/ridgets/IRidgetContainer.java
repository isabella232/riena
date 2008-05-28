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

import java.util.Collection;

/**
 * A container for ridgets.
 */
public interface IRidgetContainer {
	/**
	 * Adds the ridget with id to the ridgets of this ridget container.
	 * 
	 * @param id
	 *            the ridget id.
	 * @param ridget
	 *            the ridget.
	 */
	void addRidget(String id, IRidget ridget);

	/**
	 * Return the ridget with id from the ridgets of this ridget container.
	 * 
	 * @param id
	 *            the ridget id.
	 * @return the ridget.
	 */
	IRidget getRidget(String id);

	/**
	 * Return the ridgets of this ridget container.
	 * 
	 * @return the ridgets.
	 */
	Collection<? extends IRidget> getRidgets();
}
