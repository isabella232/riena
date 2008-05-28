/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.tree;

/**
 * Filter for <code>IUserTreeElement</code>.
 * 
 * @author Thorsten Schenkel
 */
public interface IUserTreeElementFilter {

	/**
	 * Returns true, if the given object matches the criterion defined by this filter.
	 * 
	 * @param object - the object to check.
	 * @return true, if the element matches; otherwise false.
	 */
	boolean filter( Object object );

}
