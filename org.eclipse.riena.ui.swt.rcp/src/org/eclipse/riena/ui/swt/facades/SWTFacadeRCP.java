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

import org.eclipse.riena.ui.swt.facades.internal.DisabledPainter;
import org.eclipse.riena.ui.swt.facades.internal.TreeItemEraserAndPainter;
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

/**
 * Implements {@link SWTFacade} for RCP.
 */
public final class SWTFacadeRCP extends SWTFacade {

	@Override
	public void addEraseItemListener(Table table, Listener listener) {
		table.addListener(SWT.EraseItem, listener);
	}

	@Override
	public void addEraseItemListener(Tree tree, Listener listener) {
		tree.addListener(SWT.EraseItem, listener);
	}

	@Override
	public void addMouseMoveListener(Control control, Object listener) {
		if (listener != null) {
			control.addMouseMoveListener((MouseMoveListener) listener);
		}
	}

	@Override
	public void addMouseTrackListener(Control control, MouseTrackListener listener) {
		control.addMouseTrackListener(listener);
	}

	@Override
	public void addPaintItemListener(Tree tree, Listener listener) {
		tree.addListener(SWT.PaintItem, listener);
	}

	@Override
	public void addPaintListener(Control control, EventListener listener) {
		if (listener != null) {
			control.addPaintListener((PaintListener) listener);
		}
	}

	@Override
	public Cursor createCursor(Display display, Image cursorImage, int alternateStyle) {
		Cursor result;
		if (cursorImage != null) {
			Rectangle imageBounds = cursorImage.getBounds();
			int hotspotX = imageBounds.width / 2;
			int hotspotY = imageBounds.height / 2;
			ImageData imageData = cursorImage.getImageData();
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
	public Control getCursorControl(Display display) {
		return null;
	}

	@Override
	public boolean postEvent(Display display, Event event) {
		return display.post(event);
	}

	@Override
	public void removeEraseItemListener(Table table, Listener listener) {
		table.removeListener(SWT.EraseItem, listener);
	}

	@Override
	public void removeEraseItemListener(Tree tree, Listener listener) {
		tree.removeListener(SWT.EraseItem, listener);
	}

	@Override
	public void removeMouseMoveListener(Control control, Object listener) {
		if (control != null) {
			control.removeMouseMoveListener((MouseMoveListener) listener);
		}
	}

	@Override
	public void removeMouseTrackListener(Control control, MouseTrackListener listener) {
		control.removeMouseTrackListener(listener);
	}

	@Override
	public void removePaintItemListener(Tree tree, Listener listener) {
		tree.removeListener(SWT.PaintItem, listener);
	}

	@Override
	public void removePaintListener(Control control, EventListener listener) {
		if (listener != null) {
			control.removePaintListener((PaintListener) listener);
		}
	}

}
