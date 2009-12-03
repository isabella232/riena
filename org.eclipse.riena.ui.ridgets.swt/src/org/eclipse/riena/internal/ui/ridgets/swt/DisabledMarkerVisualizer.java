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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.ridgets.IListRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * Responsible for Rendering the disabled state of any control.
 */
public class DisabledMarkerVisualizer {

	private IRidget ridget;

	// the painter
	private final DisabledPainter disabledRepresenterPainter = new DisabledPainter();

	// control reconstruction info storage
	private final RenderMemento renderMemento = new RenderMemento();

	public DisabledMarkerVisualizer(IRidget ridget) {
		this.ridget = ridget;
	}

	private IRidget getRidget() {
		return ridget;
	}

	private Control getControl() {
		return (Control) getRidget().getUIControl();
	}

	/**
	 * This is the entry point for {@link MarkerSupport}
	 */
	public void updateDisabled() {
		Control control = getControl();
		control.setEnabled(getRidget().isEnabled());
		removePaintlistener(control);

		if (!getRidget().isEnabled()) {
			storeHeaderVisiblity();
			updateComplexControl(false);
			addPaintlistener(control);
		} else {
			updateComplexControl(renderMemento.headerVisible);
		}
		control.redraw();

	}

	/////
	// Control connect and disconnect of the Paintlistener

	protected void removePaintlistener(Control control) {
		// consider the special case ChoiceComposite
		if (ChoiceComposite.class.isAssignableFrom(control.getClass())) {
			ChoiceComposite choice = ChoiceComposite.class.cast(control);
			for (Control child : choice.getChildren()) {
				removePaintlistener(child);
			}
		} else {
			control.removePaintListener(disabledRepresenterPainter);
		}
	}

	protected void addPaintlistener(Control control) {
		if (ChoiceComposite.class.isAssignableFrom(control.getClass())) {
			ChoiceComposite choice = ChoiceComposite.class.cast(control);
			for (Control child : choice.getChildren()) {
				addPaintlistener(child);
			}
		} else {
			control.addPaintListener(disabledRepresenterPainter);
		}
	}

	/*
	 * Store the visibility for later reconstruction
	 */
	private void storeHeaderVisiblity() {
		if (getControl() instanceof Tree) {
			Tree tree = (Tree) getControl();
			renderMemento.headerVisible = tree.getHeaderVisible();
		} else if (getControl() instanceof Table) {
			Table table = (Table) getControl();
			renderMemento.headerVisible = table.getHeaderVisible();
		}

	}

	/*
	 * A memento for storing some construction info
	 */
	private static class RenderMemento {
		boolean headerVisible = true;
	}

	/*
	 * throws away the model as long as the control is disabled. This influences
	 * scrolling behavior.
	 */
	private void updateComplexControl(boolean headerVisible) {
		if (getControl() instanceof Tree) {
			Tree tree = (Tree) getControl();
			tree.setHeaderVisible(headerVisible);
			if (!getRidget().isEnabled()) {
				tree.removeAll();
			} else {
				ITreeRidget treeRidget = (ITreeRidget) getRidget();
				treeRidget.updateFromModel();
			}

		} else if (getControl() instanceof Table) {
			Table table = (Table) getControl();
			table.setHeaderVisible(headerVisible);
			if (!getRidget().isEnabled()) {
				table.removeAll();
			} else {
				ITableRidget tableRidget = (ITableRidget) getRidget();
				tableRidget.updateFromModel();
			}
		} else if (getControl() instanceof List) {
			List list = (List) getControl();
			if (!getRidget().isEnabled()) {
				list.removeAll();
			} else {
				IListRidget tableRidget = (IListRidget) getRidget();
				tableRidget.updateFromModel();
			}
		}

	}

	/**
	 * The actual renderer of the {@link DisabledMarker}-State. Colors and Alpha
	 * values are configurable. See {@link LnfManager} for more details on this.
	 */
	private class DisabledPainter implements PaintListener {

		public void paintControl(PaintEvent e) {
			GC gc = e.gc;
			int alpha = LnfManager.getLnf().getIntegerSetting(LnfKeyConstants.DISABLED_MARKER_STANDARD_ALPHA);
			Widget widget = e.widget;
			if (widget instanceof Table || widget instanceof Tree || widget instanceof List || widget instanceof Combo
					|| widget instanceof DateTime) {
				// these controls have a special disabled background color. We need a special alpha value here
				alpha = LnfManager.getLnf().getIntegerSetting(LnfKeyConstants.DISABLED_MARKER_COMPLEX_ALPHA);
			}

			gc.setAlpha(alpha);
			Color color = LnfManager.getLnf().getColor(LnfKeyConstants.DISABLED_MARKER_BACKGROUND);
			gc.setBackground(color);
			// overdraws the content area
			gc.fillRectangle(0, 0, getControl().getParent().getBounds().width,
					getControl().getParent().getBounds().height);

		}
	}
}
