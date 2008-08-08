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
package org.eclipse.riena.ui.core.resource;

import org.eclipse.riena.ui.core.resource.internal.IconManager;

/**
 * Retrieves the implementation of <code>IIconManager</code>.
 */
public final class IconManagerAccessor {

	/** <code>ICON_MANAGER_ID</code> */
	public final static String ICON_MANAGER_ID = "riena.core.client.IconManager"; //$NON-NLS-1$

	private static IIconManager manager;

	private IconManagerAccessor() {
		super();
	}

	/**
	 * Returns the icon manager.
	 * 
	 * @return The implementation of <code>IIconManager</code>.
	 */
	public static IIconManager fetchIconManager() {

		// TODO: scp RegistryAccessor
		// return RegistryAccessor.fetchRegistry().getService( ICON_MANAGER_ID,
		// IIconManager.class );
		if (manager == null) {
			manager = new IconManager();
		}

		return manager;
	}
}