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
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.riena.ui.swt.AbstractRienaUIPlugin;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractRienaUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.ui.ridgets.swt"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	// Fake images helper class (for unit tests)
	private static FakeImages fakeImages;

	// Helper class for shared colors
	private SharedColors sharedColors;

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		if (sharedColors != null) {
			sharedColors.dispose();
		}
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		SharedImages.initializeImageRegistry(reg);
	}

	/**
	 * Return a "shared" image instance using the given colorKey. Shared images
	 * are managed automatically and must not be disposed by client code.
	 * 
	 * @param imageKey
	 *            a non-null String; see {@link SharedImages} for valid keys
	 * @return a non-null Image instance
	 */
	public static Image getSharedImage(final String imageKey) {
		if (getDefault() == null) { // are we unit testing?
			if (fakeImages == null) {
				fakeImages = new FakeImages();
			}
			return fakeImages.getImage(imageKey);
		}
		return getDefault().getImageRegistry().get(imageKey);
	}

	/**
	 * Return a "shared" color instance using the given colorKey. Shared colors
	 * are managed automatically and must not be disposed by client code.
	 * 
	 * @param colorKey
	 *            a non-null String; see {@link SharedColors} for valid keys
	 * @return a non-null Color instance
	 */
	public static Color getSharedColor(String colorKey) {
		if (getDefault() == null || Workbench.getInstance() == null) {
			// this is just for plain junit testing / headless testing
			return null;
		}
		return getDefault().internalGetSharedColor(colorKey);
	}

	// /**
	// * Log an error message with the given throwable.
	// *
	// * @param throwable
	// * a non-null Throwable
	// */
	// public static void log(Throwable throwable) {
	// Assert.isNotNull(throwable);
	// String msg = throwable.getMessage();
	// if (msg == null) {
	//			msg = "Unexpected error."; //$NON-NLS-1$
	// }
	// IStatus status = new Status(IStatus.ERROR, PLUGIN_ID, msg, throwable);
	// getDefault().getLog().log(status);
	// }
	//
	// helping methods
	// ////////////////

	private Color internalGetSharedColor(String colorKey) {
		if (sharedColors == null) {
			Display display = PlatformUI.getWorkbench().getDisplay();
			sharedColors = new SharedColors(display);
		}
		return sharedColors.getSharedColor(colorKey);
	}

	/**
	 * Return mock images (for unit tests).
	 */
	private static final class FakeImages {

		private final static Map<String, Image> IMAGES = new HashMap<String, Image>();

		/**
		 * Returns the an image for the given key. Multiple invocation return
		 * the same instance for the same key.
		 * 
		 * @param imageKey
		 *            a non-null string
		 * @return an Image instance (never null)
		 */
		Image getImage(String imageKey) {
			Assert.isNotNull(imageKey);
			Image result = IMAGES.get(imageKey);
			if (result == null) {
				result = ImageDescriptor.getMissingImageDescriptor().createImage();
				IMAGES.put(imageKey, result);
			}
			return result;
		}

	}

}
