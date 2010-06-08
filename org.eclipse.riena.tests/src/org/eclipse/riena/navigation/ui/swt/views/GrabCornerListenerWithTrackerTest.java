/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.ui.swt.views;

import junit.framework.TestCase;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tracker;

/**
 * Tests of the class {@code GrabCornerListenerWithTracker}.
 */
@UITestCase
public class GrabCornerListenerWithTrackerTest extends TestCase {

	private Shell shell;
	private Tracker tracker;
	private GrabCornerListenerWithTracker grabCornerListener;
	private GrabCorner corner;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {

		shell = new Shell();
		corner = new GrabCorner(shell, SWT.NONE);
		grabCornerListener = new GrabCornerListenerWithTracker(corner);
		tracker = new Tracker(shell, SWT.DOWN | SWT.RIGHT | SWT.RESIZE);

	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {

		SwtUtilities.disposeWidget(corner);
		grabCornerListener = null;
		SwtUtilities.disposeWidget(tracker);
		SwtUtilities.disposeWidget(shell);

	}

	/**
	 * Test of the method {@code getTrackerBounds}
	 */
	public void testGetTrackerBounds() {

		Rectangle rec1 = new Rectangle(1, 2, 3, 4);
		Rectangle rec2 = new Rectangle(10, 20, 30, 40);
		Rectangle[] recs = new Rectangle[] { rec1, rec2 };
		tracker.setRectangles(recs);
		Rectangle bounds = ReflectionUtils.invokeHidden(grabCornerListener, "getTrackerBounds", tracker);
		assertEquals(rec1, bounds);

		recs = new Rectangle[] {};
		tracker.setRectangles(recs);
		bounds = ReflectionUtils.invokeHidden(grabCornerListener, "getTrackerBounds", tracker);
		assertNull(bounds);

	}

	/**
	 * Test of the method {@code setMinimumBounds}
	 */
	public void testSetMinimumBounds() {

		Point miniSize = new Point(200, 300);
		shell.setMinimumSize(miniSize);

		Rectangle rec1 = new Rectangle(1, 2, 3, 4);
		Rectangle[] recs = new Rectangle[] { rec1 };
		tracker.setRectangles(recs);
		ReflectionUtils.invokeHidden(grabCornerListener, "setMinimumBounds", tracker);
		Rectangle bounds = ReflectionUtils.invokeHidden(grabCornerListener, "getTrackerBounds", tracker);
		assertEquals(miniSize.x, bounds.width);
		assertEquals(miniSize.y, bounds.height);

		rec1 = new Rectangle(1, 2, 300, 400);
		recs = new Rectangle[] { rec1 };
		tracker.setRectangles(recs);
		ReflectionUtils.invokeHidden(grabCornerListener, "setMinimumBounds", tracker);
		bounds = ReflectionUtils.invokeHidden(grabCornerListener, "getTrackerBounds", tracker);
		assertEquals(rec1.width, bounds.width);
		assertEquals(rec1.height, bounds.height);

		rec1 = new Rectangle(1, 2, 30, 400);
		recs = new Rectangle[] { rec1 };
		tracker.setRectangles(recs);
		ReflectionUtils.invokeHidden(grabCornerListener, "setMinimumBounds", tracker);
		bounds = ReflectionUtils.invokeHidden(grabCornerListener, "getTrackerBounds", tracker);
		assertEquals(miniSize.x, bounds.width);
		assertEquals(rec1.height, bounds.height);

		rec1 = new Rectangle(1, 2, 300, 40);
		recs = new Rectangle[] { rec1 };
		tracker.setRectangles(recs);
		ReflectionUtils.invokeHidden(grabCornerListener, "setMinimumBounds", tracker);
		bounds = ReflectionUtils.invokeHidden(grabCornerListener, "getTrackerBounds", tracker);
		assertEquals(rec1.width, bounds.width);
		assertEquals(miniSize.y, bounds.height);

	}

}
