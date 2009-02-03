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

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.wire.IWireWrap;
import org.eclipse.riena.internal.ui.filter.Activator;
import org.osgi.framework.BundleContext;

/**
 * Wire the {@code UIFilterProvider}
 */
public class UIFilterProviderWireWrap implements IWireWrap {

	private static final String EP_UIFILTER = "org.eclipse.riena.filter.uifilter"; //$NON-NLS-1$

	public void wire(Object bean, BundleContext context) {
		Inject.extension(EP_UIFILTER).into(bean).andStart(Activator.getDefault().getContext());
	}

}
