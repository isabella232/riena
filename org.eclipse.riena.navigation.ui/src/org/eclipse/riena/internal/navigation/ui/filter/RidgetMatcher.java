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
package org.eclipse.riena.internal.navigation.ui.filter;

import org.eclipse.riena.core.util.StringMatcher;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.NavigationNodeUtility;
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
		setId(id);
	}

	/**
	 * This method compares the ID of this matcher and the given ID of a ridget
	 * or the combined ID of navigation nod and ridget.
	 * 
	 * @param args
	 *            - object to check
	 * 
	 * @return {@code true} if IDs match; otherwise {@code false}
	 */
	public boolean matches(Object... args) {

		if ((args == null) || (args.length <= 0)) {
			return false;
		}
		if (!(args[0] instanceof IRidget)) {
			return false;
		}

		IRidget ridget = (IRidget) args[0];
		String ridgetId = ridget.getID();
		if (args.length == 1) {
			return StringUtils.equals(ridgetId, getId());
		} else {
			if (args[1] instanceof INavigationNode) {
				String nodeId = NavigationNodeUtility.getNodeLongId((INavigationNode) args[1]);
				String longRidgetId = nodeId + "/" + ridgetId; //$NON-NLS-1$
				StringMatcher matcher = new StringMatcher(getId());
				return matcher.match(longRidgetId);
			}
		}

		return false;

	}

	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	protected String getId() {
		return id;
	}

}
