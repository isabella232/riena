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
package org.eclipse.riena.ui.filter;

import java.util.Collection;

/**
 * TODO
 */
public interface IUIFilter {

	/**
	 * Adds an item to the filter.
	 * 
	 * @param item
	 *            - the item to add.
	 */
	void addFilterItem(IUIFilterAttribute item);

	/**
	 * Removes an item from the filter.
	 * 
	 * @param item
	 *            the item to remove.
	 */
	void removeFilterItem(IUIFilterAttribute item);

	/**
	 * Removes all items from the filter.
	 */
	void removeAllFilterItems();

	/**
	 * Returns the items of the filter
	 * 
	 * @return The items of the filter.
	 */
	Collection<? extends IUIFilterAttribute> getFilterItems();

}
