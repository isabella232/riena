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

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.ui.filter.IMarkerAttribute;
import org.eclipse.riena.ui.filter.IUIFilterAttribute;
import org.eclipse.riena.ui.filter.IUIFilterContainer;
import org.eclipse.riena.ui.filter.IUIFilterExtension;
import org.eclipse.riena.ui.filter.IUIFilterNavigationMarkerAttribute;
import org.eclipse.riena.ui.filter.IUIFilterProvider;
import org.eclipse.riena.ui.filter.IUIFilterRidgetMarkerAttribute;
import org.eclipse.riena.ui.internal.Activator;

/**
 *
 */
public class UIFilterProvider implements IUIFilterProvider {

	private final static Logger LOGGER = Activator.getDefault().getLogger(UIFilterProvider.class.getName());

	private static final String EP_UIFILTER = "org.eclipse.riena.filter.uifilter"; //$NON-NLS-1$
	private UIFilterExtensionInjectionHelper filterHelper;

	/**
	 * 
	 */
	public UIFilterProvider() {

		filterHelper = new UIFilterExtensionInjectionHelper();
		Inject.extension(EP_UIFILTER).useType(IUIFilterExtension.class).into(filterHelper).andStart(
				Activator.getDefault().getContext());

	}

	/**
	 * @param targetId
	 * @return
	 */
	protected IUIFilterExtension getUIFilterDefinition(String filterID) {
		if (filterHelper == null || filterHelper.getData().length == 0 || filterID == null) {
			return null;
		} else {
			IUIFilterExtension[] data = filterHelper.getData();
			for (int i = 0; i < data.length; i++) {
				if (data[i].getFilterId() != null && data[i].getFilterId().equals(filterID)) {
					return data[i];
				}

			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.ui.filter.IUIFilterProvider#provideFilter(java.lang
	 * .String)
	 */
	public IUIFilterContainer provideFilter(String filterID) {

		IUIFilterExtension filterExtension = getUIFilterDefinition(filterID);

		Collection<IUIFilterAttribute> attributes = new ArrayList<IUIFilterAttribute>(1);

		for (int i = 0; i < filterExtension.getMarkerAttributes().length; i++) {
			IMarkerAttribute type = filterExtension.getMarkerAttributes()[i];

			IUIFilterAttribute attr = type.getAttributeClass();

			if (attr instanceof IUIFilterRidgetMarkerAttribute) {
				((IUIFilterRidgetMarkerAttribute) attr).setId(type.getTargetId());
			} else if (attr instanceof IUIFilterNavigationMarkerAttribute) {
				((IUIFilterNavigationMarkerAttribute) attr).setNode(type.getTargetId());
			}
			attributes.add(attr);
		}

		UIFilter filterResult = new UIFilter(filterID, attributes);

		return new UIFilterContainer(filterResult, filterExtension.getNodeIds());
	}

	public class UIFilterExtensionInjectionHelper {
		private IUIFilterExtension[] data;

		public void update(IUIFilterExtension[] data) {
			this.data = data.clone();

		}

		public IUIFilterExtension[] getData() {
			return data.clone();
		}
	}

}
