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
package org.eclipse.riena.ui.ridgets.filter;

import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.ui.ridgets.IRidget;

/**
 * The matcher compares the ID of this class with the ID of a ridget.
 */
public class RidgetMatcher {

	private String id;

	/**
	 * Creates a new instance of {@code RidgetMatcher}.
	 * 
	 * @param id
	 *            - ID
	 */
	public RidgetMatcher(String id) {
		this.id = id;
	}

	/**
	 * This method compares the ID of this matcher and the given ID of a ridget.
	 * 
	 * @param object
	 *            - object to check
	 * 
	 * @return {@code true} if the object is an ridget and the IDs match;
	 *         otherwise {@code false}
	 */
	public boolean matches(Object object) {

		if (object instanceof IRidget) {
			IRidget ridget = (IRidget) object;
			String ridgetId = ridget.getID();
			return StringUtils.equals(ridgetId, id);
		}

		return false;

	}

	/**
	 * @param id2
	 */
	public void setId(String id) {
		this.id = id;

	}

}
