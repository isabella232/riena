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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tracker;

/**
 * Mouse listener to be used together with a {@link GrabCorner}.
 * <p>
 * This implementation relies on a SWT {@link Tracker} for resize operations.
 */
final class GrabCornerListenerWithTracker extends MouseAdapter implements MouseTrackListener {

	private final GrabCorner control;

	private Cursor resizeCursor;
	private Cursor defaultCursor;

	GrabCornerListenerWithTracker(GrabCorner control) {
		control.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				SwtUtilities.disposeResource(resizeCursor);
				SwtUtilities.disposeResource(defaultCursor);
			}
		});
		control.addMouseListener(this);
		control.addMouseTrackListener(this);
		this.control = control;
	}

	@Override
	public void mouseDown(MouseEvent e) {
		Tracker tracker = createTracker();
		try {
			boolean doResize = tracker.open();
			if (doResize) {
				handleResize(tracker);
			}
		} finally {
			SwtUtilities.disposeWidget(tracker);
		}
	}

	public void mouseEnter(MouseEvent e) {
		showResizeCursor();
	}

	public void mouseExit(MouseEvent e) {
		showDefaultCursor();
	}

	public void mouseHover(MouseEvent e) {
		// unused
	}

	// helping methods
	// ////////////////

	/**
	 * Sets the resize cursor.
	 */
	private void showResizeCursor() {
		if (resizeCursor == null) {
			resizeCursor = new Cursor(control.getDisplay(), SWT.CURSOR_SIZENWSE);
		}
		control.setCursor(resizeCursor);
	}

	/**
	 * Sets the default cursor.
	 */
	private void showDefaultCursor() {
		if (defaultCursor == null) {
			defaultCursor = new Cursor(control.getDisplay(), SWT.CURSOR_ARROW);
		}
		control.setCursor(defaultCursor);
	}

	/**
	 * Create a tracker rectangle
	 */
	private Tracker createTracker() {
		Shell shell = control.getShell();
		Display display = shell.getDisplay();
		Tracker tracker = new Tracker(display, SWT.DOWN | SWT.RIGHT | SWT.RESIZE);
		Rectangle boundsDsp = display.map(shell, null, shell.getClientArea());
		tracker.setRectangles(new Rectangle[] { boundsDsp });
		tracker.setStippled(true);
		return tracker;
	}

	/**
	 * Resize the shell with the coordinates from the tracker
	 */
	private void handleResize(Tracker tracker) {
		Rectangle[] rectangles = tracker.getRectangles();
		if (rectangles.length > 0) {
			Rectangle boundsDsp = rectangles[0];
			control.getShell().setBounds(boundsDsp);
		}
	}

}
