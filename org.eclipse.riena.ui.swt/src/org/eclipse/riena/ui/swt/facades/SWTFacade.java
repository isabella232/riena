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

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;

/**
 * TODO [ev] javadoc
 * 
 * @since 2.0
 */
public abstract class SWTFacade {

	private static final SWTFacade INSTANCE = (SWTFacade) FacadeFactory.newFacade(SWTFacade.class);

	public static final SWTFacade getDefault() {
		return INSTANCE;
	}

	public abstract void addEraseItemListener(Table table, Listener listener);

	public abstract void addEraseItemListener(Tree tree, Listener listener);

	public abstract void addPaintItemListener(Tree tree, Listener listener);

	public abstract void addPaintListener(Control control, EventListener listener);

	public abstract EventListener createDisabledPainter();

	public abstract Listener createTreeItemEraserAndPainter();

	public abstract void removeEraseItemListener(Table table, Listener listener);

	public abstract void removeEraseItemListener(Tree tre, Listener listener);

	public abstract void removePaintItemListener(Tree tree, Listener listener);

	public abstract void removePaintListener(Control control, EventListener listener);
}
