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
package org.eclipse.riena.navigation.ui.swt.utils;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.internal.navigation.ui.swt.Activator;
import org.eclipse.swt.graphics.Image;

public class ImageUtil {

	public static Image getImage(String fullPath) {
		if (StringUtils.isEmpty(fullPath)) {
			return null;
		}
		ImageRegistry imageRegistry = Activator.getDefault().getImageRegistry();
		Image image = imageRegistry.get(fullPath);
		if (image == null) {
			ImageDescriptor descriptor = null;
			String parts[] = fullPath.split(":"); //$NON-NLS-1$
			if (parts.length < 2) {
				return null;
				// image = getImageWithClassLoader(fullPath);
				// descriptor = ImageDescriptor.createFromImage(image);
			} else {
				String pluginID = parts[0];
				String iconPath = parts[1];
				descriptor = Activator.imageDescriptorFromPlugin(pluginID, iconPath);
				image = descriptor.createImage();
			}
			imageRegistry.put(fullPath, descriptor);
		}
		return image;
	}
	//
	// private static Image getImageWithClassLoader(String fullPath) {
	//
	// Bundle bundle =
	// Platform.getBundle("org.eclipse.riena.navigation.ui.swt");
	// URL url = bundle.getResource(fullPath);
	// System.out.println(url);
	//
	// // Image image = new Image(Display.getCurrent(), fullPath);
	// // return image;
	// return null;
	//
	// }

}
