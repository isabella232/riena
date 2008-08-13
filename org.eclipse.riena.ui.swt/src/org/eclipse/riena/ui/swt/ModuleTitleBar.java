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

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

/**
 * Title bar of a module (view).
 */
public class ModuleTitleBar extends EmbeddedTitleBar {

	/**
	 * @param parent
	 * @param style
	 */
	public ModuleTitleBar(Composite parent, int style) {

		super(parent, style);

	}

	/**
	 * @see org.eclipse.riena.ui.swt.EmbeddedTitleBar#addListeners()
	 */
	@Override
	protected void addListeners() {

		super.addListeners();

		TitlebarMouseListener mouseListener = new TitlebarMouseListener();
		addMouseListener(mouseListener);
		addMouseMoveListener(mouseListener);
		addMouseTrackListener(mouseListener);

	}

	/**
	 * TODO
	 * 
	 * @param event
	 */
	private void fireActivated(MouseEvent event) {
		for (IEmbeddedTitleBarListener listener : titleBarListeners.getListeners()) {
			listener.windowActivated(event);
		}
	}

	/**
	 * TODO
	 * 
	 * @param event
	 */
	private void fireClosed(MouseEvent event) {
		for (IEmbeddedTitleBarListener listener : titleBarListeners.getListeners()) {
			listener.windowClosed(event);
		}
	}

	/**
	 * After any mouse operation a method of this listener is called. The item
	 * under the current mouse position is selected, pressed or "hovered".
	 */
	private class TitlebarMouseListener implements MouseListener, MouseTrackListener, MouseMoveListener {

		/**
		 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseUp(MouseEvent e) {
			if (!shouldIgnore(e)) {
				Point point = new Point(e.x, e.y);
				if (isOverClose(point)) {
					fireClosed(e);
				} else {
					fireActivated(e);
					setPressed(false);
				}
			}
		}

		/**
		 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseDown(MouseEvent e) {
			if (!shouldIgnore(e)) {
				setPressed(true);
			}
		}

		/**
		 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseDoubleClick(MouseEvent e) {
			// nothing to do
		}

		/**
		 * @see org.eclipse.swt.events.MouseTrackListener#mouseEnter(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseEnter(MouseEvent e) {
			setHover(true);
		}

		/**
		 * @see org.eclipse.swt.events.MouseTrackListener#mouseExit(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseExit(MouseEvent e) {
			setHover(false);
		}

		/**
		 * @see org.eclipse.swt.events.MouseTrackListener#mouseHover(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseHover(MouseEvent e) {
		}

		/**
		 * @see org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events.MouseEvent)
		 */
		public void mouseMove(MouseEvent e) {
		}

		/**
		 * Ignore mouse events if the component is null, not enabled, or the
		 * event is not associated with the left mouse button.
		 */
		protected boolean shouldIgnore(MouseEvent e) {
			return e.button != 1;
		}

	}

}
