/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.util.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.Assert;

/**
 * List bean provides a list value for simple adapter UI-Binding
 * 
 * @author Alexander Ziegler
 */
public class ListBean extends AbstractBean {
	private List<?> values;

	/**
	 * Creates a list bean with an empty list
	 */
	public ListBean() {
		values = new ArrayList<Object>();
	}

	/**
	 * Creates a list bean with the given value;
	 * 
	 * @param values
	 *            The values.
	 * @pre values != null
	 */
	public ListBean(List<?> values) {
		Assert.isNotNull(values, "Expected a list");
		this.values = values;
	}

	/**
	 * Creates a list bean with the given value;
	 * 
	 * @param values
	 *            The values.
	 * @pre values != null
	 */
	public ListBean(Object[] values) {
		Assert.isNotNull(values, "Expected a list");
		this.values = Arrays.asList(values);
	}

	/**
	 * @return Returns the value list
	 */
	public List<?> getValues() {
		return values;
	}

	/**
	 * @param values
	 *            The values list to set.
	 */
	public void setValues(List<?> values) {
		Object old = this.values;
		this.values = values;
		firePropertyChanged("values", old, this.values);

	}
}