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
package org.eclipse.riena.ui.swt;

import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.renderer.EmbeddedTitlebarRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * Title bar of an embedded window (e.g. view of the current sub-module).
 */
public class EmbeddedTitleBar extends Canvas {

	private boolean active;
	private boolean pressed;
	private boolean hover;
	private boolean closeable;
	private Image image;
	private String title;

	/**
	 * Constructs a new instance of {@code EmbeddedTitleBar} given its parent
	 * and a style value describing its behavior and appearance.
	 * 
	 * @param parent
	 *            - a composite control which will be the parent of the new
	 *            instance (cannot be null)
	 * @param style
	 *            - the style of control to construct
	 */
	public EmbeddedTitleBar(Composite parent, int style) {
		super(parent, style | SWT.DOUBLE_BUFFERED);
		addListeners();
	}

	/**
	 * Adds a {@code PaintListener} to this {@code EmbeddedTitleBar}.
	 */
	private void addListeners() {

		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				onPaint(e);
			}
		});

	}

	/**
	 * Paints the title bar.<br>
	 * Configures and calls the renderer that paints the title bar.
	 * 
	 * @param e
	 *            - an event containing information about the paint
	 */
	private void onPaint(PaintEvent e) {

		GC gc = e.gc;

		// title bar
		getLnfTitlebarRenderer().setActive(isActive());
		getLnfTitlebarRenderer().setCloseable(isCloseable());
		getLnfTitlebarRenderer().setPressed(isPressed());
		getLnfTitlebarRenderer().setHover(isHover());
		getLnfTitlebarRenderer().setImage(getImage());
		Point titlebarSize = getLnfTitlebarRenderer().computeSize(gc, getBounds().width, 0);
		Rectangle titlebarBounds = new Rectangle(getBounds().x, getBounds().y, titlebarSize.x, titlebarSize.y);
		getLnfTitlebarRenderer().setBounds(titlebarBounds);
		getLnfTitlebarRenderer().paint(gc, getTitle());

	}

	/**
	 * Returns the renderer of the title bar.
	 * 
	 * @return renderer
	 */
	private EmbeddedTitlebarRenderer getLnfTitlebarRenderer() {

		EmbeddedTitlebarRenderer renderer = (EmbeddedTitlebarRenderer) LnfManager.getLnf().getRenderer(
				ILnfKeyConstants.SUB_MODULE_VIEW_TITLEBAR_RENDERER);
		if (renderer == null) {
			renderer = new EmbeddedTitlebarRenderer();
		}
		return renderer;

	}

	/**
	 * @see org.eclipse.swt.widgets.Control#getSize()
	 */
	@Override
	public Point getSize() {
		Point size = super.getSize();
		GC gc = new GC(this);
		size = getLnfTitlebarRenderer().computeSize(gc, size.x, size.y);
		gc.dispose();
		return size;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the pressed
	 */
	public boolean isPressed() {
		return pressed;
	}

	/**
	 * @param pressed
	 *            the pressed to set
	 */
	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

	/**
	 * @return the hover
	 */
	public boolean isHover() {
		return hover;
	}

	/**
	 * @param hover
	 *            the hover to set
	 */
	public void setHover(boolean hover) {
		this.hover = hover;
	}

	/**
	 * @return the closeable
	 */
	public boolean isCloseable() {
		return closeable;
	}

	/**
	 * @param closeable
	 *            the closeable to set
	 */
	public void setCloseable(boolean closeable) {
		this.closeable = closeable;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(Image image) {
		this.image = image;
	}

	/**
	 * @return the image
	 */
	public Image getImage() {
		return image;
	}

}
