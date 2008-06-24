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
package org.eclipse.riena.navigation.ui.swt.lnf;

import org.eclipse.riena.navigation.ui.swt.utils.ImageUtil;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Resource;

/**
 * Wrapper for resource image.
 */
public class ImageLnfResource extends AbstractLnfResource {

	private String imagePath;

	/**
	 * @param image -
	 *            image to wrap
	 */
	public ImageLnfResource(String imagePath) {
		super();
		this.imagePath = imagePath;
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.AbstractLnfResource#getResource()
	 */
	@Override
	public Image getResource() {
		return (Image) super.getResource();
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.lnf.ILnfResource#createResource()
	 */
	public Resource createResource() {
		return ImageUtil.getImage(imagePath);
	}

}
