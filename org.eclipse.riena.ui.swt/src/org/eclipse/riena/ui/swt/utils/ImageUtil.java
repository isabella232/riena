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
package org.eclipse.riena.ui.swt.utils;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.internal.ui.swt.Activator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;

/**
 * Utility class to manage images.
 */
public final class ImageUtil {

	private ImageUtil() {
		// utility class
	}

	/**
	 * Returns the image for the given path
	 * 
	 * @param fullPath
	 *            - path of the image
	 * @return image or <code>null</code> if no image was found.
	 */
	public static Image getImage(String fullPath) {

		if (StringUtils.isEmpty(fullPath)) {
			return null;
		}
		if (Activator.getDefault() == null) {
			return null;
		}

		ImageRegistry imageRegistry = Activator.getDefault().getImageRegistry();
		Image image = imageRegistry.get(fullPath);
		if ((image == null) || (image.isDisposed())) {
			ImageDescriptor descriptor = null;
			String[] parts = fullPath.split(":"); //$NON-NLS-1$
			if (parts.length < 2) {
				return null;
			} else {
				String pluginID = parts[0];
				String iconPath = parts[1];
				descriptor = AbstractUIPlugin.imageDescriptorFromPlugin(pluginID, iconPath);
				descriptor = Activator.imageDescriptorFromPlugin(pluginID, iconPath);
				if (descriptor == null) {
					return null;
				}
				image = descriptor.createImage();
			}
			imageRegistry.remove(fullPath);
			imageRegistry.put(fullPath, descriptor);
		}

		return image;

	}

	/**
	 * @param bundle
	 *            the bundle.
	 * @param subPath
	 *            the sub path of the image (i.e. the path relative to the
	 *            bundle directory).
	 * @return the icon path..
	 */
	public static String getIconPath(Bundle bundle, String subPath) {

		if (bundle == null) {
			return null;
		}
		StringBuilder builder = new StringBuilder(bundle.getSymbolicName());
		builder.append(":"); //$NON-NLS-1$
		builder.append(subPath);
		return builder.toString();
	}
}
