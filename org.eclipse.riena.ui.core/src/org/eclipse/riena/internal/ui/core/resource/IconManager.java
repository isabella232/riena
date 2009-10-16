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
package org.eclipse.riena.internal.ui.core.resource;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.ui.core.resource.IIconManager;
import org.eclipse.riena.ui.core.resource.IconSize;
import org.eclipse.riena.ui.core.resource.IconState;

/**
 * Maintains the icons used by the application.
 */
public final class IconManager implements IIconManager {

	private static final String EXTENSION_SEPERATOR = "."; //$NON-NLS-1$

	private Map<String, Icon> icons;

	/**
	 * Constructor.
	 */
	public IconManager() {
		icons = new HashMap<String, Icon>();
	}

	/**
	 * @see org.eclipse.riena.ui.core.resource#getIconID(java.lang.String,
	 *      org.eclipse.riena.ui.core.resource.IconSize)
	 */
	public String getIconID(String name, IconSize size) {

		return getIconID(name, size, IconState.NORMAL);
	}

	/**
	 * @see org.eclipse.riena.ui.core.resource#getIconID(java.lang.String,
	 *      org.eclipse.riena.ui.core.resource.IconSize,
	 *      org.eclipse.riena.ui.core.resource.IconState)
	 */
	public String getIconID(String name, IconSize size, IconState state) {

		if (name == null) {
			return null;
		}

		Icon icon = new Icon(name, size, state);
		String iconID = icon.getID();

		icons.put(iconID, icon);

		return iconID;
	}

	/**
	 * @see org.eclipse.riena.ui.core.resource.IIconManager#getName(java.lang.String)
	 */
	public String getName(String iconID) {

		Icon icon = icons.get(iconID);

		if (icon != null) {
			return icon.getName();
		}
		return null;
	}

	/**
	 * @see org.eclipse.riena.ui.core.resource.IIconManager#getSize(java.lang.String)
	 */
	public IconSize getSize(String iconID) {

		Icon icon = icons.get(iconID);

		if (icon != null) {
			return icon.getSize();
		}
		return null;
	}

	/**
	 * @see org.eclipse.riena.ui.core.resource.IIconManager#getState(java.lang.String)
	 */
	public IconState getState(String iconID) {

		Icon icon = icons.get(iconID);

		if (icon != null) {
			return icon.getState();
		}
		return null;
	}

	public boolean hasExtension(String iconID) {

		return iconID.indexOf(EXTENSION_SEPERATOR) != -1;
	}

	private static class Icon {

		private String name;
		private IconSize size;
		private IconState state;

		/**
		 * Creates an icon.
		 * 
		 * @param name
		 *            The name of the icon.
		 * @param size
		 *            The icon size.
		 * @param state
		 *            The icon state.
		 */
		public Icon(String name, IconSize size, IconState state) {
			this.name = name;
			this.size = size;
			this.state = state;
		}

		/**
		 * @return The ID of the icon.
		 */
		public String getID() {
			return name + state.getDefaultMapping() + size.getDefaultMapping();
		}

		/**
		 * @return The name of the icon.
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return The size of the icon.
		 */
		public IconSize getSize() {
			return size;
		}

		/**
		 * @return The state of the icon.
		 */
		public IconState getState() {
			return state;
		}
	}

}
