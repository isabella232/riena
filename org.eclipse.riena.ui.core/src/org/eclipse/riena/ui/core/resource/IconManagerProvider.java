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
package org.eclipse.riena.ui.core.resource;

import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.riena.core.wire.InjectExtension;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.ui.core.Activator;

/**
 * This class provides an {@link IIconManager}. The icon manager with the
 * highest order will be returned.
 */
public class IconManagerProvider {

	private static IconManagerProvider iconManagerProvider = new IconManagerProvider();
	private IIconManager iconManager;

	/**
	 * Creates a new instance of this provider and wires the extensions.
	 */
	private IconManagerProvider() {
		Wire.instance(this).andStart(Activator.getDefault().getContext());
	}

	/**
	 * Returns
	 * 
	 * @return instance of icon manger.
	 */
	public static IconManagerProvider getInstance() {
		return iconManagerProvider;
	}

	/**
	 * Returns an {@link IIconManager}. The icon manager with the highest order
	 * will be returned.
	 * 
	 * @return icon manger
	 */
	public IIconManager getIconManager() {
		return iconManager;
	}

	/**
	 * Injects the extension of the icon managers.
	 * 
	 * @param iconManagerExtensions
	 *            extension of the icon managers
	 */
	@InjectExtension
	public void update(IIconManagerExtension[] iconManagerExtensions) {
		if ((iconManagerExtensions != null) && (iconManagerExtensions.length > 0)) {
			Arrays.sort(iconManagerExtensions, new IconManagerComparator());
			iconManager = iconManagerExtensions[0].createIconManager();
		}
	}

	/**
	 * Comparator of {@code IIconManagerExtension}.
	 */
	private class IconManagerComparator implements Comparator<IIconManagerExtension> {

		/**
		 * {@inheritDoc}
		 * <p>
		 * The property {@code order} of the {@code IIconManagerExtension} are
		 * compared.
		 */
		public int compare(IIconManagerExtension im1, IIconManagerExtension im2) {
			return ((Integer) im2.getOrder()).compareTo(im1.getOrder());
		}

	}

}
