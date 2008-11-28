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
package org.eclipse.riena.ui.ridgets.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IWindowRidget;

/**
 * Controller for a view that is or has a window.
 */
public abstract class AbstractWindowController implements IController {

	/**
	 * The ridget id to use for the window ridget.
	 */
	public static final String RIDGET_ID_WINDOW = "windowRidget"; //$NON-NLS-1$

	private IWindowRidget windowRidget;
	private Map<String, IRidget> ridgets;
	private boolean blocked;

	public AbstractWindowController() {

		super();

		ridgets = new HashMap<String, IRidget>();
	}

	/**
	 * @return The window ridget.
	 */
	public IWindowRidget getWindowRidget() {
		return windowRidget;
	}

	/**
	 * Sets the window ridget.
	 * 
	 * @param windowRidget
	 *            The window ridget.
	 */
	public void setWindowRidget(IWindowRidget windowRidget) {
		this.windowRidget = windowRidget;
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidgetContainer#addRidget(java.lang.String,
	 *      org.eclipse.riena.ui.internal.ridgets.IRidget)
	 */
	public void addRidget(String id, IRidget ridget) {
		ridgets.put(id, ridget);
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidgetContainer#getRidget(java.lang.String)
	 */
	public IRidget getRidget(String id) {
		return ridgets.get(id);
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidgetContainer#getRidgets()
	 */
	public Collection<? extends IRidget> getRidgets() {
		return ridgets.values();
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.controller.IController#setBlocked(boolean)
	 */
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
		// TODO: ausimplementieren
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.controller.IController#isBlocked()
	 */
	public boolean isBlocked() {
		return this.blocked;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.ui.internal.ridgets.IRidgetContainer#configureRidgets()
	 */
	public void configureRidgets() {
		setWindowRidget((IWindowRidget) getRidget(RIDGET_ID_WINDOW));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.controller.IController#afterBind()
	 */
	public void afterBind() {
		getWindowRidget().updateFromModel();
	}
}
