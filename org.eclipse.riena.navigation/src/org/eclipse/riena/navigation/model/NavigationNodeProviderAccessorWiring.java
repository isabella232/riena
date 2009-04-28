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
package org.eclipse.riena.navigation.model;

import org.osgi.framework.BundleContext;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.wire.AbstractWiring;

/**
 * Wire the {@code NavigationNodeProviderAccessor}.
 */
public class NavigationNodeProviderAccessorWiring extends AbstractWiring {

	private static final String EXTENSION_POINT_ID = "org.eclipse.riena.navigation.navigationNodeProvider"; //$NON-NLS-1$

	@Override
	public void wire(Object bean, BundleContext context) {
		Inject.extension(EXTENSION_POINT_ID).into(bean).andStart(context);
	}
}
