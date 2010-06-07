/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.beans.common;

import java.util.Arrays;
import java.util.Collection;

/**
 * 
 */
public class StringManager {

	private Collection<String> items;
	private String selectedItem;

	/**
	 * constructor.
	 * 
	 * @param items
	 */
	public StringManager(String... items) {

		super();

		setItems(Arrays.asList(items));
	}

	/**
	 * @return items
	 */
	public Collection<String> getItems() {

		return items;
	}

	/**
	 * @param items
	 */
	public void setItems(Collection<String> items) {

		this.items = items;
	}

	/**
	 * @return Returns the selectedItem.
	 */
	public String getSelectedItem() {
		return selectedItem;
	}

	/**
	 * @param selectedItem
	 *            The selectedItem to set.
	 */
	public void setSelectedItem(String selectedItem) {
		this.selectedItem = selectedItem;
	}
}
