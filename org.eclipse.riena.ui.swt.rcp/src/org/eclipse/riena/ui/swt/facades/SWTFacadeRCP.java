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
package org.eclipse.riena.ui.swt.facades;

import java.util.EventListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;

import org.eclipse.riena.ui.swt.facades.internal.DisabledPainter;
import org.eclipse.riena.ui.swt.facades.internal.TreeItemEraserAndPainter;

/**
 * Implements {@link SWTFacade} for RCP.
 */
public final class SWTFacadeRCP extends SWTFacade {

	@Override
	public void addEraseItemListener(final Table table, final Listener listener) {
		table.addListener(SWT.EraseItem, listener);
	}

	@Override
	public void addEraseItemListener(final Tree tree, final Listener listener) {
		tree.addListener(SWT.EraseItem, listener);
	}

	@Override
	public void addMouseMoveListener(final Control control, final Object listener) {
		if (listener != null) {
			control.addMouseMoveListener((MouseMoveListener) listener);
		}
	}

	@Override
	public void addMouseTrackListener(final Control control, final MouseTrackListener listener) {
		control.addMouseTrackListener(listener);
	}

	@Override
	public void addPaintItemListener(final Tree tree, final Listener listener) {
		tree.addListener(SWT.PaintItem, listener);
	}

	@Override
	public void addPaintListener(final Control control, final EventListener listener) {
		if (listener != null) {
			control.addPaintListener((PaintListener) listener);
		}
	}

	@Override
	public Cursor createCursor(final Display display, final Image cursorImage, final int alternateStyle) {
		Cursor result;
		if (cursorImage != null) {
			final Rectangle imageBounds = cursorImage.getBounds();
			final int hotspotX = imageBounds.width / 2;
			final int hotspotY = imageBounds.height / 2;
			final ImageData imageData = cursorImage.getImageData();
			result = new Cursor(display, imageData, hotspotX, hotspotY);
		} else {
			result = new Cursor(display, alternateStyle);
		}
		return result;
	}

	@Override
	public EventListener createDisabledPainter() {
		return new DisabledPainter();
	}

	@Override
	public Listener createTreeItemEraserAndPainter() {
		return new TreeItemEraserAndPainter();
	}

	@Override
	public Control getCursorControl(final Display display) {
		return null;
	}

	@Override
	public boolean postEvent(final Display display, final Event event) {
		return display.post(event);
	}

	@Override
	public void removeEraseItemListener(final Table table, final Listener listener) {
		table.removeListener(SWT.EraseItem, listener);
	}

	@Override
	public void removeEraseItemListener(final Tree tree, final Listener listener) {
		tree.removeListener(SWT.EraseItem, listener);
	}

	@Override
	public void removeMouseMoveListener(final Control control, final Object listener) {
		if (control != null) {
			control.removeMouseMoveListener((MouseMoveListener) listener);
		}
	}

	@Override
	public void removeMouseTrackListener(final Control control, final MouseTrackListener listener) {
		control.removeMouseTrackListener(listener);
	}

	@Override
	public void removePaintItemListener(final Tree tree, final Listener listener) {
		tree.removeListener(SWT.PaintItem, listener);
	}

	@Override
	public void removePaintListener(final Control control, final EventListener listener) {
		if (listener != null) {
			control.removePaintListener((PaintListener) listener);
		}
	}

}
