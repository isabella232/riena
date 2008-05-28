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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Provides access to a set of shared colors. The colors can be accessed using
 * one of the predifined string constants.
 * <p>
 * Example:
 * 
 * <pre>
 * Color color = Activator.getSharedColor(SharedColors.COLOR_MANDATORY);
 * control.setBackground(color);
 * </pre>
 */
public final class SharedColors {

	public static final String COLOR_MANDATORY = "COLOR_MANDATORY"; //$NON-NLS-1$
	public static final String COLOR_OUTPUT = "COLOR_OUTPUT"; //$NON-NLS-1$
	public static final String COLOR_MANDATORY_OUTPUT = "COLOR_MANDATORY_OUTPUT"; //$NON-NLS-1$

	private Map<String, Color> sharedColors = new HashMap<String, Color>();

	SharedColors(Display display) {
		Object[] values = { COLOR_MANDATORY, new RGB(255, 255, 175), COLOR_OUTPUT, new RGB(231, 233, 245),
				COLOR_MANDATORY_OUTPUT, new RGB(242, 243, 210), };
		for (int i = 0; i < values.length; i = i + 2) {
			String key = (String) values[i];
			RGB rgb = (RGB) values[i + 1];
			Color color = new Color(display, rgb);
			sharedColors.put(key, color);
		}
	}

	Color getSharedColor(String colorKey) {
		Assert.isNotNull(sharedColors, "Shared colors are disposed"); //$NON-NLS-1$
		Color result = sharedColors.get(colorKey);
		Assert.isNotNull(result, "Could not find color with key: " + colorKey); //$NON-NLS-1$
		return result;
	}

	void dispose() {
		if (sharedColors != null) {
			for (Color color : sharedColors.values()) {
				color.dispose();
			}
			sharedColors = null;
		}
	}

}
