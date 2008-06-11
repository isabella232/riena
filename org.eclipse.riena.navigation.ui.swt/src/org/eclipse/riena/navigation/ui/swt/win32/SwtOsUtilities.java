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
package org.eclipse.riena.navigation.ui.swt.win32;

import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.Control;

/**
 * A collection of utility methods which are depending on the operation system.<br>
 * This is an implementation for Windows (win32).
 */
public final class SwtOsUtilities {

	/**
	 * This class contains only static methods. So it is not necessary to create
	 * an instance.
	 */
	private SwtOsUtilities() {
		throw new Error("SwtOsUtilities is just a container for static methods"); //$NON-NLS-1$
	}

	/**
	 * Hiddes the vertical and horizontal scroll bar of the given control.
	 */
	public static void hiddeSrollBars(Control control) {

		int hwnd = control.handle;
		OS.ShowScrollBar(hwnd, OS.SB_VERT, false);
		OS.ShowScrollBar(hwnd, OS.SB_HORZ, false);

	}

}
