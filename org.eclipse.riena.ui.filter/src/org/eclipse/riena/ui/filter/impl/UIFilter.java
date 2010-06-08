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
package org.eclipse.riena.ui.filter.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.eclipse.riena.ui.filter.IUIFilter;
import org.eclipse.riena.ui.filter.IUIFilterRule;

/**
 * Implementation of {@link IUIFilter}.
 */
public class UIFilter implements IUIFilter {

	private String filterID;
	private Collection<IUIFilterRule> rules;

	public UIFilter() {
		rules = new HashSet<IUIFilterRule>();
	}

	public UIFilter(String id) {
		this();
		filterID = id;
	}

	public UIFilter(Collection<? extends IUIFilterRule> rules) {
		this();
		this.rules.addAll(rules);
	}

	public UIFilter(String id, Collection<? extends IUIFilterRule> rule) {
		this();
		filterID = id;
		this.rules.addAll(rule);
	}

	private Collection<IUIFilterRule> getRules() {
		return rules;
	}

	public Collection<? extends IUIFilterRule> getFilterRules() {
		return Collections.unmodifiableCollection(getRules());
	}

	public String getFilterID() {

		return filterID;
	}

	public void setFilterID(String id) {
		filterID = id;
	}

}
