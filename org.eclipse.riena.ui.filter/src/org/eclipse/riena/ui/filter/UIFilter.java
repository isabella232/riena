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
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class UIFilter implements IUIFilter {

	private Set<IUIFilterAttribute> items;

	public UIFilter() {
		items = new HashSet<IUIFilterAttribute>();
	}

	private Set<IUIFilterAttribute> getItems() {
		return items;
	}

	public void addFilterItem(IUIFilterAttribute item) {
		getItems().add(item);
	}

	public Collection<? extends IUIFilterAttribute> getFilterItems() {
		return getItems();
	}

	public void removeAllFilterItems() {
		getItems().clear();
	}

	public void removeFilterItem(IUIFilterAttribute item) {
		getItems().remove(item);
	}

}
