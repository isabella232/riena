/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.util.beans;

import java.util.Arrays;
import java.util.Collection;

/**
 * 
 * @author Frank Schepp
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