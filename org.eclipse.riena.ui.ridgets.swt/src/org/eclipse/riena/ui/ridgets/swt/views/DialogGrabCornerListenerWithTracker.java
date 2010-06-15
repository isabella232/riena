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
package org.eclipse.riena.ui.ridgets.swt.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tracker;

import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Mouse listener to be used together with a {@link GrabCorner}.
 * <p>
 * This implementation relies on a SWT {@link Tracker} for resize operations.
 */
public final class DialogGrabCornerListenerWithTracker extends MouseAdapter implements MouseTrackListener {

	private final DialogGrabCorner control;

	private Cursor resizeCursor;
	private Cursor defaultCursor;

	public DialogGrabCornerListenerWithTracker(DialogGrabCorner control) {
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
		tracker.addControlListener(new TrackerListener());
		Rectangle boundsDsp = display.map(shell, null, shell.getClientArea());
		tracker.setRectangles(new Rectangle[] { boundsDsp });
		tracker.setStippled(true);
		return tracker;
	}

	/**
	 * Resize the shell with the coordinates from the tracker
	 */
	private void handleResize(Tracker tracker) {
		Rectangle bounds = getTrackerBounds(tracker);
		if (bounds != null) {
			control.getShell().setBounds(bounds);
		}
	}

	/**
	 * Returns the bounds of the given tracker.<br>
	 * <i>The tracker can have different bounds. This method returns the bounds
	 * of the first rectangle.</i>
	 * 
	 * @param tracker
	 * @return bounds; {@code null} if the tracker has no bounds.
	 */
	private Rectangle getTrackerBounds(Tracker tracker) {
		Rectangle[] rectangles = tracker.getRectangles();
		if (rectangles.length > 0) {
			return rectangles[0];
		} else {
			return null;
		}

	}

	/**
	 * If the size of the tracker is less than the minimum size of the shell,
	 * the tracker will be set the minimum shell bounds.
	 * 
	 * @param tracker
	 */
	private void setMinimumBounds(Tracker tracker) {

		Rectangle bounds = getTrackerBounds(tracker);
		Point miniSize = control.getShell().getMinimumSize();
		boolean setTrackerBounds = false;
		if (bounds.width < miniSize.x) {
			setTrackerBounds = true;
			bounds.width = miniSize.x;
		}
		if (bounds.height < miniSize.y) {
			setTrackerBounds = true;
			bounds.height = miniSize.y;
		}
		if (setTrackerBounds) {
			tracker.setRectangles(new Rectangle[] { bounds });
		}

	}

	/**
	 *
	 */
	private class TrackerListener implements ControlListener {

		/**
		 * @see org.eclipse.swt.events.ControlListener#controlMoved(org.eclipse.swt
		 *      .events.ControlEvent)
		 */
		public void controlMoved(ControlEvent e) {
			// nothing to do
		}

		/**
		 * @see org.eclipse.swt.events.ControlListener#controlResized(org.eclipse
		 *      .swt.events.ControlEvent)
		 */
		public void controlResized(ControlEvent e) {

			if (e.widget instanceof Tracker) {
				Tracker tracker = (Tracker) e.widget;
				setMinimumBounds(tracker);
			}

		}

	}

}
