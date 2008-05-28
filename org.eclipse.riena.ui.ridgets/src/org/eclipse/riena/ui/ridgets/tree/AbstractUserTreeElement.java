/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.tree;

/**
 * Abstract implementation of <code>IUserTreeElement</code>.
 *
 * @author Thorsten Schenkel
 */
public abstract class AbstractUserTreeElement implements IUserTreeElement {

	private IUserTreeElementFilter filter;

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.IUserTreeElement#setFilter(de.compeople.spirit.core.client.uibinding.adapter.tree.IUserTreeElementFilter)
	 */
	public void setFilter( IUserTreeElementFilter filter ) {
		this.filter = filter;
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.IUserTreeElement#getFilter()
	 */
	public IUserTreeElementFilter getFilter() {
		return filter;
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.IUserTreeElement#hasFilter()
	 */
	public boolean hasFilter() {
		return ( filter != null );
	}

	/**
	 * Returns a default value that will be displayed as a placeholder for a child that is in
	 * the process of being loaded. May be overridden by subclasses to return some more
	 * specific information about what is loaded like "Loading files...".
	 * 
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.IUserTreeElement#getLoadingChildValue()
	 */
	public Object getLoadingChildValue() {
		return "...";
	}

}
