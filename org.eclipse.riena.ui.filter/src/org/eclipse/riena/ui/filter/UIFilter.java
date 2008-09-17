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

/**
 * Implementation of {@link IUIFilter}.
 */
public class UIFilter implements IUIFilter {

	private Collection<IUIFilterAttribute> attributes;

	public UIFilter() {
		attributes = new HashSet<IUIFilterAttribute>();
	}

	private Collection<IUIFilterAttribute> getAttributes() {
		return attributes;
	}

	public void addFilterAttribute(IUIFilterAttribute item) {
		getAttributes().add(item);
	}

	public Collection<? extends IUIFilterAttribute> getFilterAttributes() {
		return getAttributes();
	}

	public void removeAllFilterAttributes() {
		getAttributes().clear();
	}

	public void removeFilterAttribute(IUIFilterAttribute item) {
		getAttributes().remove(item);
	}

}
