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
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

/**
 * Mouse listener to be used together with a {@link GrabCorner}.
 * <p>
 * This implementation resizes by using
 * {@link Shell#setSize(org.eclipse.swt.graphics.Point)} during the drag-phase.
 * <p>
 */
// TODO [ev] delete later?
final class GrabCornerListener implements MouseListener, MouseTrackListener, MouseMoveListener {

	private final GrabCorner control;

	private Cursor resizeCursor;
	private Cursor defaultCursor;
	private boolean resize;
	private Point startPoint;

	GrabCornerListener(GrabCorner control) {
		control.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				SwtUtilities.disposeResource(resizeCursor);
				SwtUtilities.disposeResource(defaultCursor);
			}
		});
		control.addMouseListener(this);
		control.addMouseMoveListener(this);
		control.addMouseTrackListener(this);
		this.control = control;
	}

	public void mouseDoubleClick(MouseEvent e) {
	}

	public void mouseDown(MouseEvent e) {
		resize = true;
		startPoint = new Point(e.x, e.y);
	}

	public void mouseUp(MouseEvent e) {
		resize = false;
	}

	public void mouseEnter(MouseEvent e) {
		showResizeCursor();
	}

	public void mouseExit(MouseEvent e) {
		showDefaultCursor();
	}

	public void mouseHover(MouseEvent e) {
	}

	public void mouseMove(MouseEvent e) {

		if (resize) {
			Point size = control.getShell().getSize();
			size.x -= startPoint.x - e.x;
			size.y -= startPoint.y - e.y;
			control.getShell().setSize(size);
			control.getShell().update();
		}
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

}
