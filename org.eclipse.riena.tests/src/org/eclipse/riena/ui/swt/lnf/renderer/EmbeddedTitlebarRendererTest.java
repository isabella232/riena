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
package org.eclipse.riena.ui.swt.lnf.renderer;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.UIProcessFinishedMarker;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

/**
 * Tests of the class {@link EmbeddedTitlebarRenderer}.
 */
public class EmbeddedTitlebarRendererTest extends TestCase {

	/**
	 * Tests of the method <code>computeSize(GC, int, int) </code>
	 * 
	 * @throws Exception
	 *             - handled by JUnit
	 */
	public void testComputeSize() throws Exception {

		EmbeddedTitlebarRenderer renderer = new EmbeddedTitlebarRenderer();
		Shell shell = new Shell();
		GC gc = new GC(shell);
		int wHint = 12;
		int hHint = 24;
		Point size = renderer.computeSize(gc, wHint, hHint);
		assertEquals(wHint, size.x);
		Font font = LnfManager.getLnf().getFont(ILnfKeyConstants.EMBEDDED_TITLEBAR_FONT);
		gc.setFont(font);
		int expectedHeight = gc.getFontMetrics().getHeight() + 8;
		assertEquals(expectedHeight, size.y);

		font.dispose();
		gc.dispose();
		shell.dispose();

	}

	/**
	 * Tests the method {@code isProcessMarkerVisible()}.
	 */
	public void testIsProcessMarkerVisible() {

		EmbeddedTitlebarRenderer renderer = new EmbeddedTitlebarRenderer();
		renderer.setMarkers(null);
		boolean ret = ReflectionUtils.invokeHidden(renderer, "isProcessMarkerVisible", new Object[] {});
		assertFalse(ret);

		List<IMarker> markers = new ArrayList<IMarker>(2);
		ErrorMarker errorMarker = new ErrorMarker();
		markers.add(errorMarker);
		renderer.setMarkers(markers);
		ret = ReflectionUtils.invokeHidden(renderer, "isProcessMarkerVisible", new Object[] {});
		assertFalse(ret);

		UIProcessFinishedMarker finishedMarker = new UIProcessFinishedMarker();
		markers.add(finishedMarker);
		renderer.setMarkers(markers);
		finishedMarker.setOn(false);
		ret = ReflectionUtils.invokeHidden(renderer, "isProcessMarkerVisible", new Object[] {});
		assertFalse(ret);

		finishedMarker.setOn(true);
		ret = ReflectionUtils.invokeHidden(renderer, "isProcessMarkerVisible", new Object[] {});
		assertTrue(ret);

	}

}
