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
package org.eclipse.riena.ui.swt.utils;

import java.net.URL;

import org.eclipse.equinox.log.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.internal.ui.swt.Activator;
import org.eclipse.swt.graphics.Image;
import org.osgi.service.log.LogService;

/**
 * The ImageStore returns the images for given names. The images are loaded form
 * and cached. The ImageStore extends the images name, if a state (@see
 * {@link ImageState}) like pressed of hover is given. If the image name has no
 * file extension, the extension ".png" will be added.
 */
public final class ImageStore {

	private static final Logger LOGGER = Activator.getDefault().getLogger(ImageStore.class);
	private static final String IMAGE_PATH_EXTENSION_ID = "org.eclipse.riena.ui.swt.imagepath"; //$NON-NLS-1$

	private static ImageStore store;
	private static Image missingImage;
	private IImagePathExtension[] iconPathes;

	private ImageStore() {
		// utility class
	}

	/**
	 * Returns an instance (always the same) of this class.
	 * 
	 * @return instance of {@code ImageStore}
	 */
	public static ImageStore getInstance() {
		if (store == null) {
			store = new ImageStore();
			if (Activator.getDefault() != null) {
				Inject.extension(IMAGE_PATH_EXTENSION_ID).into(store).andStart(Activator.getDefault().getContext());
			}
		}
		return store;
	}

	/**
	 * Returns the image for the given image name.
	 * 
	 * @param imageName
	 *            - name (ID) of the image
	 * @return image or {@code null} if no image exists for the given name.
	 */
	public Image getImage(String imageName) {
		return getImage(imageName, ImageState.NORMAL);
	}

	/**
	 * Returns the image for the given image name and with the given file
	 * extension.
	 * 
	 * @param imageName
	 *            - name (ID) of the image
	 * @param fileExtension
	 *            - extension of the image file (@see ImageFileExtension)
	 * @return image or {@code null} if no image exists for the given name.
	 */
	public Image getImage(String imageName, ImageFileExtension fileExtension) {
		return getImage(imageName, ImageState.NORMAL, fileExtension);
	}

	/**
	 * Returns the image for the given image name and given state.
	 * 
	 * @param imageName
	 *            - name (ID) of the image
	 * @param state
	 *            - state of the image (@see ImageState)
	 * @return image or {@code null} if no image exists for the given name.
	 */
	public Image getImage(String imageName, ImageState state) {
		return getImage(imageName, state, ImageFileExtension.PNG);
	}

	/**
	 * Returns the image for the given image name, given state and with the
	 * given file extension.
	 * 
	 * @param imageName
	 *            - name (ID) of the image
	 * @param state
	 *            - state of the image (@see ImageState)
	 * @param fileExtension
	 *            - extension of the image file (@see ImageFileExtension)
	 * @return image or {@code null} if no image exists for the given name.
	 */
	public Image getImage(String imageName, ImageState state, ImageFileExtension fileExtension) {
		String fullName = getFullName(imageName, state, fileExtension);
		return loadImage(fullName);
	}

	/**
	 * Returns the full name of the image.
	 * 
	 * @param imageName
	 *            - name (ID) of the image
	 * @param state
	 *            - state of the image (@see ImageState)
	 * @param fileExtension
	 *            - extension of the image file (@see ImageFileExtension)
	 * @return full name of the image (file name).
	 */
	private String getFullName(String imageName, ImageState state, ImageFileExtension fileExtension) {

		if (StringUtils.isEmpty(imageName)) {
			return null;
		}

		String fullName = imageName;
		if (state != null) {
			fullName += state.getStateNameExtension();
		}
		// scaling ?!?

		if (imageName.indexOf('.') < 0) {
			if (fileExtension != null) {
				fullName += "." + fileExtension.getFileNameExtension(); //$NON-NLS-1$
			}
		}

		return fullName;

	}

	/**
	 * Returns the image for the given name. If the image isn't cached, the
	 * image is loaded form the resources and stores in the cache of the {@code
	 * ImageStore}.
	 * 
	 * @param fullName
	 *            - full name of the image (file name)
	 * @return image or {@code null} if no image exists for the given name.
	 */
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

	/**
	 * Returns a descriptor of the image for the given name. The file of the
	 * image is searched in every given bundle + icon path. The icon paths are
	 * define via extension points.
	 * 
	 * @param fullName
	 *            - full name of the image (file name)
	 * @return image descriptor or {@code null} if file does not exists.
	 */
	private ImageDescriptor getImageDescriptor(String fullName) {

		for (IImagePathExtension iconPath : iconPathes) {
			String fullPath = iconPath.getPath() + '/' + fullName;
			URL url = iconPath.getContributingBundle().getResource(fullPath);
			if (url != null) {
				return ImageDescriptor.createFromURL(url);
			}
		}

		LOGGER.log(LogService.LOG_WARNING, "Image resource \"" + fullName + "\" not found!"); //$NON-NLS-1$ //$NON-NLS-2$
		return null;

	}

	/**
	 * Returns the missing image.
	 * 
	 * @return missing image
	 */
	public synchronized Image getMissingImage() {
		if (missingImage == null) {
			missingImage = ImageDescriptor.getMissingImageDescriptor().createImage();
		}
		return missingImage;
	}

	public void update(IImagePathExtension[] iconPathes) {
		this.iconPathes = iconPathes;
	}

}
