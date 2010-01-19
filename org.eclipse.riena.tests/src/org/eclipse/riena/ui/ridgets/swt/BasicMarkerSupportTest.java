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
package org.eclipse.riena.ui.ridgets.swt;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.internal.ui.ridgets.swt.TextRidget;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link BasicMarkerSupport}.
 */
@UITestCase
public class BasicMarkerSupportTest extends TestCase {

	private Display display;
	private Shell shell;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		display = Display.getDefault();
		shell = new Shell(display);
	}

	@Override
	protected void tearDown() {
		SwtUtilities.disposeWidget(shell);
		display = null;
	}

	/**
	 * Tests (parts of) the method {@code init(IBasicMarkableRidget,
	 * PropertyChangeSupport)}.
	 */
	public void testInit() {

		DefaultRealm realm = new DefaultRealm();
		try {
			Text control = new Text(shell, SWT.NONE);
			ITextRidget ridget = new TextRidget();
			ridget.setUIControl(control);
			ridget.addMarker(new ErrorMarker());
			ridget.addMarker(new MandatoryMarker());

			BasicMarkerSupport markerSupport = new BasicMarkerSupport();
			markerSupport.init(ridget, null);
			assertEquals(2, ridget.getMarkers().size());

			control.dispose();
			assertEquals(0, ridget.getMarkers().size());
		} finally {
			realm.dispose();
		}

	}

}
