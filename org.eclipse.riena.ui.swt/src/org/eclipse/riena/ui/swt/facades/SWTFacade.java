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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;

/**
 * Single-sourced access to SWT methods that are not available in RAP.
 * <p>
 * <b>Note:</b> The RCP implementation delegates to the appropriate SWT methods.
 * The RAP implementation does nothing (as this functionality is missing in
 * RAP).
 * 
 * @since 2.0
 */
public abstract class SWTFacade {

	private static final SWTFacade INSTANCE = (SWTFacade) FacadeFactory.newFacade(SWTFacade.class);

	/**
	 * The applicable implementation of this class.
	 */
	public static final SWTFacade getDefault() {
		return INSTANCE;
	}

	/**
	 * Returns true if running on the RAP platform, false otherwise.
	 */
	public static final boolean isRAP() {
		return "rap".equals(SWT.getPlatform()); //$NON-NLS-1$
	}

	/**
	 * Returns true if running on the RCP platform, false otherwise.
	 */
	public static final boolean isRCP() {
		return !SWTFacade.isRAP();
	}

	/**
	 * Adds an SWT.EraseItem listener to the given table.
	 */
	public abstract void addEraseItemListener(Table table, Listener listener);

	/**
	 * Adds an SWT.EraseItem listener to the given tree.
	 */
	public abstract void addEraseItemListener(Tree tree, Listener listener);

	/**
	 * Adds a MouseTrackListener to the given control.
	 */
	public abstract void addMouseTrackListener(Control control, MouseTrackListener listener);

	/**
	 * Adds an SWT.PaintItem listener to the given tree.
	 */
	public abstract void addPaintItemListener(Tree tree, Listener listener);

	/**
	 * Adds a PaintListener to the given control.
	 * 
	 * @param listener
	 *            an EventListener that implements the PaintListener interface,
	 *            or null
	 */
	public abstract void addPaintListener(Control control, EventListener listener);

	/**
	 * Returns a paint listener for modifying the disabled look of a control.
	 * 
	 * @return a PaintListener or null (in RAP)
	 */
	public abstract EventListener createDisabledPainter();

	/**
	 * Returns an SWT.EraseItem / SWT.PaintItem listener, that will paint all
	 * tree cells empty when the tree is disabled.
	 * 
	 * @return a Listener or null (in RAP)
	 */
	public abstract Listener createTreeItemEraserAndPainter();

	/**
	 * Removes an SWT.EraseItem listener from the given table.
	 */
	public abstract void removeEraseItemListener(Table table, Listener listener);

	/**
	 * Removes an SWT.EraseItem listener from the given tree.
	 */
	public abstract void removeEraseItemListener(Tree tree, Listener listener);

	/**
	 * Removes a MouseTrackListener from the given control.
	 */
	public abstract void removeMouseTrackListener(Control control, MouseTrackListener listener);

	/**
	 * Removes an SWT.PaintItem listener from the given tree.
	 */
	public abstract void removePaintItemListener(Tree tree, Listener listener);

	/**
	 * Removes a PaintListener from the given control.
	 * 
	 * @param listener
	 *            an EventListener that implements the PaintListener interface,
	 *            or null
	 */
	public abstract void removePaintListener(Control control, EventListener listener);

	/**
	 * Sets the numbers of decimal digits in the given spinner.
	 * <p>
	 * <b>Note:</b> throws a RuntimeException if invoked in RAP.
	 * 
	 * @param digits
	 *            the number of decimal digits (zero or greater)
	 * @throws RuntimeException
	 *             if invoked in RAP. The RAP Spinner does not support decimal
	 *             digits at this time.
	 */
	public abstract void setDigits(Spinner spinner, int digits);
}
