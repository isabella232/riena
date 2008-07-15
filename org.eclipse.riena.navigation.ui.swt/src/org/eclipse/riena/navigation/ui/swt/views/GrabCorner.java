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

import org.eclipse.riena.navigation.ui.swt.lnf.renderer.ShellBorderRenderer;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * Area to grab so that the shell can be resized.
 */
public class GrabCorner extends Composite {

	private Cursor resizeCursor;
	private Cursor defaultCursor;

	/**
	 * @param shell
	 * @param style
	 */
	public GrabCorner(Shell shell, int style) {

		super(shell, style);

		setLayoutData();

		addListeners();

	}

	/**
	 * Sets the (form) layout data for the grab corner.
	 */
	private void setLayoutData() {

		Point grabCornerSize = getGrabCornerSize();
		int borderWidth = getShellBorderWidth();

		FormData grabFormData = new FormData();
		grabFormData.width = grabCornerSize.y;
		grabFormData.height = grabCornerSize.x;
		grabFormData.bottom = new FormAttachment(100, -borderWidth);
		grabFormData.right = new FormAttachment(100, -borderWidth);
		setLayoutData(grabFormData);

	}

	/**
	 * Adds listeners to the grab corner.
	 */
	private void addListeners() {

		addPaintListener(new GrabPaintListener());
		GrabMouseListener mouseListener = new GrabMouseListener();
		addMouseListener(mouseListener);
		addMouseMoveListener(mouseListener);
		addMouseTrackListener(mouseListener);

	}

	/**
	 * Returns the size of the grab corner (including the border (right,bottom)
	 * of the shell)
	 * 
	 * @return size of grab corner
	 */
	public static Point getGrabCornerSize() {

		Point grabCornerSize = new Point(getShellBorderWidth(), getShellBorderWidth());
		Image grabCorner = getGrabCornerImage();
		if ((grabCorner != null) && isResizeable()) {
			ImageData imageData = grabCorner.getImageData();
			int width = grabCornerSize.x + imageData.width;
			int height = grabCornerSize.y + imageData.height;
			grabCornerSize = new Point(width, height);
		}
		return grabCornerSize;

	}

	/**
	 * Returns if the shell is resizeable or not.
	 * 
	 * @return true if shell is resizeable; otherwise false
	 */
	public static boolean isResizeable() {
		return LnfManager.getLnf().getBooleanSetting(ILnfKeyConstants.TITLELESS_SHELL_RESIZEABLE);
	}

	/**
	 * Returns the image of the grab corner.
	 * 
	 * @return grab corner image
	 */
	private static Image getGrabCornerImage() {
		return LnfManager.getLnf().getImage(ILnfKeyConstants.TITLELESS_SHELL_GRAB_CORNER_IMAGE);
	}

	/**
	 * Returns the width of the shell border.
	 * 
	 * @return border width
	 */
	private static int getShellBorderWidth() {

		ShellBorderRenderer borderRenderer = (ShellBorderRenderer) LnfManager.getLnf().getRenderer(
				ILnfKeyConstants.TITLELESS_SHELL_BORDER_RENDERER);
		return borderRenderer.getBorderWidth();

	}

	/**
	 * Sets the resize cursor.
	 */
	private void showResizeCursor() {
		if (resizeCursor == null) {
			resizeCursor = new Cursor(getDisplay(), SWT.CURSOR_SIZENWSE);
		}
		setCursor(resizeCursor);

	}

	/**
	 * Sets the default cursor.
	 */
	private void showDefaultCursor() {
		if (defaultCursor == null) {
			defaultCursor = new Cursor(getDisplay(), SWT.CURSOR_ARROW);
		}
		setCursor(defaultCursor);
	}

	/**
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		SwtUtilities.disposeResource(resizeCursor);
		SwtUtilities.disposeResource(defaultCursor);
	}

	/**
	 * This Listener paint the grab corner.
	 */
	private static class GrabPaintListener implements PaintListener {

		/**
		 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
		 */
		public void paintControl(PaintEvent e) {

			GC gc = e.gc;
			Image grabCornerImage = getGrabCornerImage();
			gc.drawImage(grabCornerImage, 0, 0);

		}

	}

	/**
	 * After any mouse operation a method of this listener is called.
	 */
	private class GrabMouseListener implements MouseListener, MouseTrackListener, MouseMoveListener {

		private boolean resize;
		private Point startPoint;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse
		 * .swt.events.MouseEvent)
		 */
		public void mouseDoubleClick(MouseEvent e) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events
		 * .MouseEvent)
		 */
		public void mouseDown(MouseEvent e) {
			resize = true;
			startPoint = new Point(e.x, e.y);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events
		 * .MouseEvent)
		 */
		public void mouseUp(MouseEvent e) {
			resize = false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.swt.events.MouseTrackListener#mouseEnter(org.eclipse.
		 * swt.events.MouseEvent)
		 */
		public void mouseEnter(MouseEvent e) {
			showResizeCursor();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.swt.events.MouseTrackListener#mouseExit(org.eclipse.swt
		 * .events.MouseEvent)
		 */
		public void mouseExit(MouseEvent e) {
			showDefaultCursor();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.swt.events.MouseTrackListener#mouseHover(org.eclipse.
		 * swt.events.MouseEvent)
		 */
		public void mouseHover(MouseEvent e) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt
		 * .events.MouseEvent)
		 */
		public void mouseMove(MouseEvent e) {

			if (resize) {
				Point endPoint = new Point(e.x, e.y);
				Point size = getShell().getSize();
				size.x -= startPoint.x - endPoint.x;
				size.y -= startPoint.y - endPoint.y;
				getShell().setSize(size);
			}

		}
	}

}
