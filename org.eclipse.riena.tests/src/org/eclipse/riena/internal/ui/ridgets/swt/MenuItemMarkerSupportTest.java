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

import junit.framework.TestCase;

import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 * Tests of the class {@link MenuItemMarkerSupport}.
 */
public class MenuItemMarkerSupportTest extends TestCase {

	/**
	 * Tests the method {@code updateMarkers()}.
	 */
	public void testUpdateMarkers() {

		Shell shell = new Shell();
		Menu menu = new Menu(shell, SWT.NONE);
		MenuItem item = new MenuItem(menu, SWT.NONE);
		MenuItemRidget ridget = new MenuItemRidget();

		MenuItemMarkerSupport markerSupport = new MenuItemMarkerSupport(ridget, null);
		ridget.setEnabled(false);
		assertTrue(item.isEnabled());

		ridget.setUIControl(item);
		markerSupport.updateMarkers();
		assertFalse(item.isEnabled());

		SwtUtilities.disposeWidget(shell);

	}

}
