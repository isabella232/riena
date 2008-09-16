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
 * Standard implementation for the {@link IUIFilterable} interface, can be used
 * by other objects to delegate
 */
public class UIFilterable implements IUIFilterable {

	private Set<IUIFilter> filters;

	/**
	 * Creates a new instance of {@code UIFilterable} and an empty set of
	 * {@link IUIFilter}s.
	 */
	public UIFilterable() {
		filters = new HashSet<IUIFilter>();
	}

	public void addFilter(IUIFilter filter) {
		filters.add(filter);
	}

	public Collection<? extends IUIFilter> getFilters() {
		return filters;
	}

	public void removeAllFilters() {
		filters.clear();
	}

	public void removeFilter(IUIFilter filter) {
		filters.remove(filter);
	}

}
