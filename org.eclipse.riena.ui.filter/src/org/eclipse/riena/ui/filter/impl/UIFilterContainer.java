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
package org.eclipse.riena.ui.filter.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.riena.ui.filter.IUIFilter;
import org.eclipse.riena.ui.filter.IUIFilterContainer;
import org.eclipse.riena.ui.filter.extension.IFilterNodeId;

/**
 *
 */
public class UIFilterContainer implements IUIFilterContainer {

	Collection<String> nodeIds;
	IUIFilter filter;

	public UIFilterContainer(IUIFilter filter, IFilterNodeId[] ids) {

		nodeIds = new ArrayList<String>();
		for (int i = 0; i < ids.length; i++) {
			nodeIds.add((ids[i].getId()));
		}

		this.filter = filter;
	}

	public IUIFilter getFilter() {
		return filter;
	}

	public Collection<String> getFilterTargetNodeIds() {
		return nodeIds;
	}

}
