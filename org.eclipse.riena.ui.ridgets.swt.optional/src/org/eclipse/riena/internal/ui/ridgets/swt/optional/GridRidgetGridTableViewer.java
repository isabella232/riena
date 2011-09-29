/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt.optional;

import org.eclipse.nebula.jface.gridviewer.GridTableViewer;

/**
 * 
 */
public class GridRidgetGridTableViewer extends GridTableViewer {

	private final GridRidget gridRidget;

	public GridRidgetGridTableViewer(final GridRidget gridRidget) {
		super(gridRidget.getUIControl());
		this.gridRidget = gridRidget;
	}

	public GridRidget getGridRidget() {
		return gridRidget;
	}

}
