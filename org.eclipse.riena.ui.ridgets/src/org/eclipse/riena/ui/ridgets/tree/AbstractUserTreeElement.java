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
 * Abstract implementation of <code>IUserTreeElement</code>.
 */
public abstract class AbstractUserTreeElement implements IUserTreeElement {

	private IUserTreeElementFilter filter;

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.IUserTreeElement#setFilter(de.compeople.spirit.core.client.uibinding.adapter.tree.IUserTreeElementFilter)
	 */
	public void setFilter(IUserTreeElementFilter filter) {
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
		return (filter != null);
	}

	/**
	 * Returns a default value that will be displayed as a placeholder for a
	 * child that is in the process of being loaded. May be overridden by
	 * subclasses to return some more specific information about what is loaded
	 * like "Loading files...".
	 * 
	 * @see de.compeople.spirit.core.client.uibinding.adapter.tree.IUserTreeElement#getLoadingChildValue()
	 */
	public Object getLoadingChildValue() {
		return "...";
	}

}
