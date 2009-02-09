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

import java.io.File;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.internal.ui.swt.Activator;
import org.eclipse.swt.graphics.Image;

/**
 * 
 */
public class ImageStore {

	private static final String IMAGE_PATH_EXTENSION_ID = "org.eclipse.riena.ui.swt.imagepath"; //$NON-NLS-1$

	private static ImageStore store;
	private static Image missingImage;
	private IImagePathExtension[] iconPathes;

	private ImageStore() {
		// TODO
	}

	public static ImageStore getInstance() {
		if (store == null) {
			store = new ImageStore();
			if (Activator.getDefault() != null) {
				Inject.extension(IMAGE_PATH_EXTENSION_ID).into(store).andStart(Activator.getDefault().getContext());
			}
		}
		return store;
	}

	public final Image getImage(String imageName) {
		return getImage(imageName, ImageState.NORMAL);
	}

	public final Image getImage(String imageName, ImageFileExtension fileExtension) {
		return getImage(imageName, ImageState.NORMAL, fileExtension);
	}

	public final Image getImage(String imageName, ImageState state) {
		return getImage(imageName, ImageState.NORMAL, ImageFileExtension.PNG);
	}

	public final Image getImage(String imageName, ImageState state, ImageFileExtension fileExtension) {
		String fullName = getFullName(imageName, state, fileExtension);
		return loadImage(fullName);
	}

	private String getFullName(String imageName, ImageState state, ImageFileExtension fileExtension) {

		if (StringUtils.isEmpty(imageName)) {
			return null;
		}

		String fullName = imageName + state.getStateNameExtension();
		// scaling

		if (imageName.indexOf('.') < 0) {
			fullName += "." + fileExtension; //$NON-NLS-1$
		}

		return fullName;

	}

	private synchronized Image loadImage(String fullName) {

		if (StringUtils.isEmpty(fullName)) {
			return null;
		}

		if (Activator.getDefault() == null) {
			return null;
		}

		ImageRegistry imageRegistry = Activator.getDefault().getImageRegistry();
		Image image = imageRegistry.get(fullName);
		if ((image == null) || (image.isDisposed())) {
			ImageDescriptor descriptor = getImageDescriptor(fullName);
			if (descriptor == null) {
				return null;
			}
			image = descriptor.createImage();
			imageRegistry.remove(fullName);
			imageRegistry.put(fullName, descriptor);
		}

		return image;

	}

	private ImageDescriptor getImageDescriptor(String fullName) {

		for (IImagePathExtension iconPath : iconPathes) {
			String fullPath = iconPath.getPath() + File.separator + fullName;
			URL url = iconPath.getContributingBundle().getResource(fullPath);
			if (url != null) {
				return ImageDescriptor.createFromURL(url);
			}
		}

		return null;

	}

	public final synchronized Image getMissingImage() {
		if (missingImage == null) {
			missingImage = ImageDescriptor.getMissingImageDescriptor().createImage();
		}
		return missingImage;
	}

	public void update(IImagePathExtension[] iconPathes) {
		this.iconPathes = iconPathes;
	}

}
