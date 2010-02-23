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
package org.eclipse.riena.ui.swt.facades;

import java.util.EventListener;

import org.eclipse.riena.ui.swt.facades.internal.DisabledPainter;
import org.eclipse.riena.ui.swt.facades.internal.TreeItemEraserAndPainter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;

/**
 * TODO [ev] docs
 */
public final class SWTFacadeImpl extends SWTFacade {

	@Override
	public void addEraseItemListener(Table table, Listener listener) {
		table.addListener(SWT.EraseItem, listener);
	}

	@Override
	public void addEraseItemListener(Tree tree, Listener listener) {
		tree.addListener(SWT.EraseItem, listener);
	}

	@Override
	public void addPaintItemListener(Tree tree, Listener listener) {
		tree.addListener(SWT.PaintItem, listener);
	}

	public void addPaintListener(Control control, EventListener listener) {
		control.addPaintListener((PaintListener) listener);
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
	public void removeEraseItemListener(Table table, Listener listener) {
		table.removeListener(SWT.EraseItem, listener);
	}

	@Override
	public void removeEraseItemListener(Tree tree, Listener listener) {
		tree.removeListener(SWT.EraseItem, listener);
	}

	@Override
	public void removePaintItemListener(Tree tree, Listener listener) {
		tree.removeListener(SWT.PaintItem, listener);
	}

	public void removePaintListener(Control control, EventListener listener) {
		control.removePaintListener((PaintListener) listener);
	}

	public void setDigits(Spinner control, int digits) {
		control.setDigits(digits);
	}

}
