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

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.tests.RienaTestCase;
import org.eclipse.riena.tests.collect.UITestCase;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;

/**
 * Tests the class {@link ImageStore}.
 */
@UITestCase
public class ImageStoreTest extends RienaTestCase {

	/**
	 * Tests the <i>private</i> method {@code getFullName}.
	 */
	public void testGetFullName() {

		ImageStore store = ImageStore.getInstance();
		String fullName = ReflectionUtils.invokeHidden(store, "getFullName", "abc", ImageState.HOVER,
				ImageFileExtension.JPG);
		assertEquals("abc_h_.jpg", fullName);
		fullName = ReflectionUtils.invokeHidden(store, "getFullName", "abc", ImageState.NORMAL, ImageFileExtension.GIF);
		assertEquals("abc.gif", fullName);

	}

	/**
	 * Tests the method {@code getImage(String)}.
	 */
	public void testGetImage() {

		Shell shell = new Shell();

		ImageStore store = ImageStore.getInstance();
		store.update(new IImagePathExtension[] {});
		Image image = store.getImage("spirit");
		assertNull(image);

		IImagePathExtension extension = new IImagePathExtension() {

			public Bundle getContributingBundle() {
				return Activator.getDefault().getBundle();
			}

			public String getPath() {
				return "icons";
			}

		};
		store.update(new IImagePathExtension[] { extension });
		image = store.getImage("spirit");
		assertNotNull(image);

		SwtUtilities.disposeWidget(shell);

	}

	/**
	 * Tests the method {@code getImage(String,ImageFileExtension)}.
	 */
	public void testGetImageImageFileExtension() {

		Shell shell = new Shell();
		ImageStore store = ImageStore.getInstance();

		IImagePathExtension extension = new IImagePathExtension() {

			public Bundle getContributingBundle() {
				return Activator.getDefault().getBundle();
			}

			public String getPath() {
				return "icons";
			}

		};
		store.update(new IImagePathExtension[] { extension });

		Image image = store.getImage("spirit", ImageFileExtension.PNG);
		assertNotNull(image);
		image = store.getImage("spirit", ImageFileExtension.GIF);
		assertNull(image);
		image = store.getImage("spirit", ImageFileExtension.JPG);
		assertNull(image);

		image = store.getImage("eclipse", ImageFileExtension.PNG);
		assertNull(image);
		image = store.getImage("eclipse", ImageFileExtension.GIF);
		assertNotNull(image);
		image = store.getImage("eclipse", ImageFileExtension.JPG);
		assertNull(image);

		SwtUtilities.disposeWidget(shell);

	}

	/**
	 * Tests the method {@code getImage(String,ImageState)}.
	 */
	public void testGetImageImageState() {

		Shell shell = new Shell();
		ImageStore store = ImageStore.getInstance();

		IImagePathExtension extension = new IImagePathExtension() {

			public Bundle getContributingBundle() {
				return Activator.getDefault().getBundle();
			}

			public String getPath() {
				return "icons";
			}

		};
		store.update(new IImagePathExtension[] { extension });

		Image image = store.getImage("spirit", ImageState.NORMAL);
		assertNotNull(image);
		image = store.getImage("spirit", ImageState.HOVER);
		assertNotNull(image);
		image = store.getImage("spirit", ImageState.HOVER_HAS_FOCUS);
		assertNull(image);

		SwtUtilities.disposeWidget(shell);

	}

}
