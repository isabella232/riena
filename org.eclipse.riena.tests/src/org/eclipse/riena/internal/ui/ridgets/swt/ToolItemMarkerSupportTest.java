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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * Tests of the class {@link ToolItemMarkerSupport}
 */
public class ToolItemMarkerSupportTest extends TestCase {

	/**
	 * Tests the method {@code updateMarkers()}.
	 */
	public void testUpdateMarkers() {

		Shell shell = new Shell();
		ToolBar toolbar = new ToolBar(shell, SWT.NONE);
		ToolItem item = new ToolItem(toolbar, SWT.NONE);
		ToolItemRidget ridget = new ToolItemRidget();

		ToolItemMarkerSupport markerSupport = new ToolItemMarkerSupport(ridget, null);
		ridget.setEnabled(false);
		assertTrue(item.isEnabled());

		ridget.setUIControl(item);
		markerSupport.updateMarkers();
		assertFalse(item.isEnabled());

		SwtUtilities.disposeWidget(shell);

	}

}
