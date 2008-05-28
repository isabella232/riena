/****************************************************************
 *                                                              *
 * Copyright (c) 2005 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/

package org.eclipse.riena.ui.core.resource;

import org.eclipse.riena.ui.core.resource.internal.IconManager;

/**
 * Retrieves the implementation of <code>IIconManager</code>.
 * 
 * @author Carsten Drossel
 */
public final class IconManagerAccessor {

	/** <code>ICON_MANAGER_ID</code> */
	public final static String ICON_MANAGER_ID = "spirit.core.client.IconManager";

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