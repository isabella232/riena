/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.tree;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * Element used inside a dynamic tree.
 * 
 * @author Thorsten Schenkel
 */
public interface IUserTreeElement extends Serializable {

	/**
	 * Indicates whether the element is a leaf in the tree. If not, it will be displayed as
	 * a folder and it may have children but not necessarily.
	 * 
	 * @return true if the element is a leaf, false otherwise.
	 */
	boolean isLeaf();

	/**
	 * Returns an Iterator with all children of the element. The children are returned in
	 * successive and disjunctive collections of child elements. The children may be returned
	 * in a separate collection each, all in one collection or anything inbetween. If the
	 * element does not have any children, either an empty Iterator or null may be returned.
	 *  
	 * @return An Iterator with all children of the element.
	 */
	Iterator<? extends Collection<? extends IUserTreeElement>> getChildren();

	/**
	 * Returns the value that will be displayed as a placeholder for a child that is in
	 * the process of being loaded.
	 * 
	 * @return The value of a child element that is being loaded.
	 */
	Object getLoadingChildValue();

	/**
	 * Returns a <code>String</code> representation of the tree element.
	 * 
	 * @return the <code>String</code> representation.
	 */
	String toString();

	/**
	 * Sets current filter. The filter is used to filter out tree elements. <br>
	 * 
	 * @param filter - the new filter to use; null, if no filter should be used.
	 */
	void setFilter( IUserTreeElementFilter filter );

	/**
	 * Returns the currently used file filter. <br>
	 * Returns null if no filter is used.
	 * 
	 * @return the current filter.
	 */
	IUserTreeElementFilter getFilter();

	/**
	 * Returns whether the user element has a filter or not.
	 * 
	 * @return true, if element has filter.
	 */
	boolean hasFilter();

}
