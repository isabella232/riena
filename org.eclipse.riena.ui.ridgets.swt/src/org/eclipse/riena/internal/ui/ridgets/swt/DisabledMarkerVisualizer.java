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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;

import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.MarkerSupport;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * Responsible for Rendering the disabled state of any control.
 */
public class DisabledMarkerVisualizer {

	// the painter
	private static final DisabledPainter DISABLED_MARKER_PAINTER = new DisabledPainter();
	// the ridget
	private final IRidget ridget;
	// control reconstruction info storage
	private RenderMemento renderMemento;

	public DisabledMarkerVisualizer(IRidget ridget) {
		this.ridget = ridget;
	}

	/**
	 * This is the entry point for {@link MarkerSupport}
	 */
	public void updateDisabled() {
		updateDisabled(getControl(), getRidget().isEnabled());
	}

	private void updateDisabled(Control control, boolean enabled) {
		control.setEnabled(enabled);
		removePaintlistener(control);

		if (!enabled) {
			// TODO [ev] Disabled because of: 298024 Table - setHeaderVisible will reset scroll bars to top,left
			// renderMemento = storeState();
			// setHeaderVisible(false);
			addPaintlistener(control);
		} else {
			//			if (renderMemento != null) {
			//				setHeaderVisible(renderMemento.headerVisible);
			//			}
		}

		if (control instanceof Composite) {
			Composite composite = (Composite) control;
			Control[] children = composite.getChildren();
			for (Control child : children) {
				updateDisabled(child, enabled);
			}
		}

		control.redraw();
	}

	/**
	 * Control connect and disconnect the Paintlistener.
	 */
	protected void removePaintlistener(Control control) {
		control.removePaintListener(DISABLED_MARKER_PAINTER);
	}

	protected void addPaintlistener(Control control) {
		control.addPaintListener(DISABLED_MARKER_PAINTER);
	}

	// helping methods
	//////////////////

	private Control getControl() {
		return (Control) getRidget().getUIControl();
	}

	private IRidget getRidget() {
		return ridget;
	}

	/**
	 * Store the visibility for later reconstruction
	 */
	private RenderMemento storeState() {
		RenderMemento result = new RenderMemento();
		Control control = getControl();
		if (control instanceof Tree) {
			Tree tree = (Tree) control;
			result.headerVisible = tree.getHeaderVisible();
		} else if (control instanceof Table) {
			Table table = (Table) control;
			result.headerVisible = table.getHeaderVisible();
		}
		return result;
	}

	private void setHeaderVisible(boolean headerVisible) {
		Control control = getControl();
		if (control instanceof Tree) {
			Tree tree = (Tree) control;
			tree.setHeaderVisible(headerVisible);
		} else if (control instanceof Table) {
			Table table = (Table) control;
			table.setHeaderVisible(headerVisible);
		}
	}

	// helping classes
	//////////////////

	/**
	 * A memento for storing some construction info
	 */
	private static final class RenderMemento {
		boolean headerVisible;
	}

	/**
	 * The actual renderer of the {@link DisabledMarker}-State. Colors and Alpha
	 * values are configurable. See {@link LnfManager} for more details on this.
	 */
	private static final class DisabledPainter implements PaintListener {
		public void paintControl(PaintEvent e) {
			GC gc = e.gc;
			Control control = (Control) e.widget;
			int alpha = LnfManager.getLnf().getIntegerSetting(LnfKeyConstants.DISABLED_MARKER_STANDARD_ALPHA);
			gc.setAlpha(alpha);
			Color color = LnfManager.getLnf().getColor(LnfKeyConstants.DISABLED_MARKER_BACKGROUND);
			gc.setBackground(color);
			// overdraws the content area
			Rectangle bounds = control.getBounds();
			gc.fillRectangle(0, 0, bounds.width, bounds.height);
		}
	}
}
