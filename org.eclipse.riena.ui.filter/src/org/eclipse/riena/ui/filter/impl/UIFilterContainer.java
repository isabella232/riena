/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.filter.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.riena.ui.filter.IUIFilter;
import org.eclipse.riena.ui.filter.IUIFilterContainer;
import org.eclipse.riena.ui.filter.extension.IFilterNodeIdExtension;

/**
 * This container stores an UI filter and a collection of node ID on which the
 * filter maybe applied.
 */
public class UIFilterContainer implements IUIFilterContainer {

	private Collection<String> nodeIds;
	private IUIFilter filter;

	/**
	 * Creates a container to stores the given filter and th according node IDs.
	 * 
	 * @param filter
	 *            the UI filter of this container
	 * @param idsS
	 *            array of navigation-node IDs
	 */
	public UIFilterContainer(IUIFilter filter, IFilterNodeIdExtension[] ids) {

		nodeIds = new ArrayList<String>();
		for (int i = 0; i < ids.length; i++) {
			nodeIds.add((ids[i].getId()));
		}

		this.filter = filter;
	}

	/**
	 * {@inheritDoc}
	 */
	public IUIFilter getFilter() {
		return filter;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<String> getFilterTargetNodeIds() {
		return nodeIds;
	}

}
