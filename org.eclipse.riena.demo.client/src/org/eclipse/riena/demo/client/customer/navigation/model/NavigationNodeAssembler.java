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
package org.eclipse.riena.demo.client.customer.navigation.model;

import org.eclipse.riena.internal.demo.customer.client.Activator;
import org.eclipse.riena.navigation.AbstractNavigationAssembler;
import org.eclipse.riena.navigation.INavigationAssembler;

import org.osgi.framework.Bundle;

public abstract class NavigationNodeAssembler extends AbstractNavigationAssembler implements INavigationAssembler {

	protected String createIconPath(String subPath) {
		Bundle bundle = Activator.getDefault().getBundle();

		if (bundle == null) {
			return null;
		}
		StringBuilder builder = new StringBuilder(bundle.getSymbolicName());
		builder.append(":"); //$NON-NLS-1$
		builder.append(subPath);
		return builder.toString();
	}

}
