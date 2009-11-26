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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;

import org.eclipse.riena.ui.ridgets.IListRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.swt.ChoiceComposite;

/**
 * Responsible for Rendering the disabled state of any control.
 */
public class DisabledMarkerVisualizer {

	private IRidget ridget;

	private final DisabledPainter disabledRepresenterPainter = new DisabledPainter();
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

	public void updateDisabled() {
		Control control = getControl();
		control.setEnabled(getRidget().isEnabled());

		if (isSimpleControl(control)) {
			// those controls just get a special foreground
			updateControlColors(control);
			return;
		}
		control.removePaintListener(disabledRepresenterPainter);

		if (!getRidget().isEnabled()) {
			storeHeaderVisiblity();
			updateComplexControl(false);
			control.addPaintListener(disabledRepresenterPainter);
		} else {
			updateComplexControl(renderMemento.headerVisible);
		}
		control.redraw();

	}

	private void storeHeaderVisiblity() {
		if (getControl() instanceof Tree) {
			Tree tree = (Tree) getControl();
			renderMemento.headerVisible = tree.getHeaderVisible();
		} else if (getControl() instanceof Table) {
			Table table = (Table) getControl();
			renderMemento.headerVisible = table.getHeaderVisible();
		}

	}

	static class RenderMemento {
		boolean headerVisible = true;
		Color foreGround;
	}

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

	private void updateControlColors(Control control) {
		Color currentColor = null;
		if (!getRidget().isEnabled()) {
			renderMemento.foreGround = control.getForeground();
			currentColor = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
		} else {
			currentColor = renderMemento.foreGround;
		}
		control.setForeground(currentColor);
	}

	private boolean isSimpleControl(Control control) {
		return control instanceof Label
				|| (control instanceof Button && (((control.getStyle() & SWT.RADIO) > 0) || (control.getStyle() & SWT.CHECK) > 0))
				|| control instanceof ChoiceComposite;
	}

	private class DisabledPainter implements PaintListener {

		public void paintControl(PaintEvent e) {
			GC gc = e.gc;
			gc.setAlpha(190);
			gc.setBackground(new Color(null, 255, 255, 255));
			gc.fillRectangle(0, 0, getControl().getParent().getBounds().width,
					getControl().getParent().getBounds().height);

		}
	}
}
