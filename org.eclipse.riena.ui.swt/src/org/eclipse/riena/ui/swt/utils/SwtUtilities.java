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

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.widgets.Widget;

/**
 * A collection of utility methods for SWT.
 */
public final class SwtUtilities {

	/**
	 * This class contains only static methods. So it is not necessary to create
	 * an instance.
	 */
	private SwtUtilities() {
		throw new Error("SwtUtilities is just a container for static methods"); //$NON-NLS-1$
	}

	/**
	 * The text will be clipped, if the width of the given text is greater than
	 * the maximum width.<br>
	 * The clipped text always ends with three dots ("...").
	 * 
	 * @param gc
	 *            graphics context
	 * @param text
	 *            text
	 * @param maxWidth
	 *            maximum of the text
	 * @return truncated text
	 */
	public static String clipText(final GC gc, final String text, int maxWidth) {
		int textwidth = calcTextWidth(gc, text);
		if (textwidth > maxWidth) {
			StringBuffer shortText = new StringBuffer(text);
			shortText.append("..."); //$NON-NLS-1$
			while (textwidth > maxWidth) {
				if (shortText.length() <= 3) {
					break;
				}
				shortText = shortText.deleteCharAt(shortText.length() - 4);
				textwidth = calcTextWidth(gc, shortText);
			}
			return shortText.toString();
		}
		return text;
	}

	/**
	 * Calculates the width of the given text based on the current settings of
	 * the given graphics context.
	 * 
	 * @param gc
	 *            graphics context
	 * @param text
	 *            text
	 * @return width of text
	 */
	public static int calcTextWidth(final GC gc, final String text) {
		int result = 0;
		if (text != null) {
			result = calcTextWidth(gc, new StringBuffer(text));
		}
		return result;
	}

	/**
	 * Calculates the width of the given text based on the current settings of
	 * the given graphics context.
	 * 
	 * @param gc
	 *            graphics context
	 * @param text
	 *            text
	 * @return width of text
	 */
	public static int calcTextWidth(final GC gc, final StringBuffer text) {
		int width = 0;
		if (text == null) {
			return width;
		}
		for (int i = 0; i < text.length(); i++) {
			width += gc.getAdvanceWidth(text.charAt(i));
		}
		return width;
	}

	/**
	 * Creates a new instance of <code>Color</code> that is a brighter version
	 * of the given color.
	 * 
	 * @param color
	 *            the color to make brighter.
	 * @param f
	 *            the factor.
	 * @return a new <code>Color</code> object that is a brighter version of
	 *         this given color.
	 * 
	 * @pre color != null;
	 */
	public static Color makeBrighter(final Color color, float f) {

		Assert.isNotNull(color);
		Assert.isTrue(f >= 0.0);

		float[] hsb = color.getRGB().getHSB();
		float h = hsb[0];
		float s = hsb[1];
		float b = hsb[2];

		b = b * f;
		if (b > 1.0f) {
			b = 1.0f;
		}

		RGB rgb = new RGB(h, s, b);

		return new Color(color.getDevice(), rgb);
	}

	/**
	 * Disposes the given resource, if the the resource is not null and isn't
	 * already disposed.
	 * 
	 * @param resource
	 *            resource to dispose
	 */
	public static void disposeResource(final Resource resource) {
		if (!isDisposed(resource)) {
			resource.dispose();
		}
	}

	/**
	 * Disposes the given widget, if the the widget is not {@code null} and
	 * isn't already disposed.
	 * 
	 * @param widget
	 *            widget to dispose
	 */
	public static void disposeWidget(final Widget widget) {
		if (!isDisposed(widget)) {
			widget.dispose();
		}
	}

	/**
	 * Returns {@code true}, if the given widget is disposed or {@code null}.
	 * 
	 * @param widget
	 *            widget to check
	 * @return {@code true}, if the widget is disposed or {@code null};
	 *         otherwise {@code false}.
	 */
	public static boolean isDisposed(Widget widget) {
		return widget == null || widget.isDisposed();
	}

	/**
	 * Returns {@code true}, if the given resource is disposed or {@code null}.
	 * 
	 * @param resource
	 *            resource to check
	 * @return {@code true}, if the resource is disposed or {@code null};
	 *         otherwise {@code false}.
	 */
	public static boolean isDisposed(Resource resource) {
		return !((resource != null) && (!resource.isDisposed()));
	}

}
