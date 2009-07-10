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
package org.eclipse.riena.internal.navigation.ui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.ToolBar;

import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * Utility class for working with {@link CoolBar}s.
 */
public final class CoolbarUtils {

	/**
	 * Initializes the given cool bar.<br>
	 * E.g. sets the background of the cool bar and if the cool bar is empty
	 * adds necessary cool item with the height 1.
	 * 
	 * @param coolBar
	 *            - cool bar
	 * @return the first cool item of the given cool bar
	 */
	public static CoolItem initCoolBar(CoolBar coolBar) {
		if (coolBar.getItemCount() == 0) {
			CoolItem coolItem = new CoolItem(coolBar, SWT.DROP_DOWN);
			ToolBar toolBar = new ToolBar(coolBar, SWT.FLAT);
			coolItem.setControl(toolBar);
			// sets the default size of an empty menu bar or tool bar
			coolItem.setSize(new Point(0, 1));
		}

		coolBar.setBackground(getCoolbarBackground());
		coolBar.setBackgroundMode(SWT.INHERIT_FORCE);
		coolBar.setLocked(true);

		return coolBar.getItem(0);
	}

	// helping methods
	//////////////////

	/**
	 * Return the coolbar / menubar background color according to the
	 * look-and-feel.
	 */
	private static Color getCoolbarBackground() {
		return LnfManager.getLnf().getColor(LnfKeyConstants.COOLBAR_BACKGROUND);
	}

	private CoolbarUtils() {
		// prevent instantiation
	}
}
