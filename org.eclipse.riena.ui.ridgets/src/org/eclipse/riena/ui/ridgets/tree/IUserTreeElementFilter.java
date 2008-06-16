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
package org.eclipse.riena.ui.ridgets.tree;

/**
 * Filter for <code>IUserTreeElement</code>.
 */
public interface IUserTreeElementFilter {

	/**
	 * Returns true, if the given object matches the criterion defined by this
	 * filter.
	 * 
	 * @param object -
	 *            the object to check.
	 * @return true, if the element matches; otherwise false.
	 */
	boolean filter(Object object);

}
