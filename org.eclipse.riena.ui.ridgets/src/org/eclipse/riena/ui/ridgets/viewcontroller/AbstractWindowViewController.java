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
package org.eclipse.riena.ui.ridgets.viewcontroller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.ui.core.uiprocess.IProgressVisualizerLocator;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IWindowRidget;

/**
 * Controller for a view that is or has a window.
 */
public abstract class AbstractWindowViewController implements IViewController {

	private IWindowRidget windowRidget;
	private Map<String, IRidget> ridgets;
	private boolean blocked;

	public AbstractWindowViewController() {

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
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#addRidget(java.lang.String,
	 *      org.eclipse.riena.ui.ridgets.IRidget)
	 */
	public void addRidget(String id, IRidget ridget) {
		ridgets.put(id, ridget);
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#getRidget(java.lang.String)
	 */
	public IRidget getRidget(String id) {
		return ridgets.get(id);
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#getRidgets()
	 */
	public Collection<? extends IRidget> getRidgets() {
		return ridgets.values();
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.viewcontroller.IViewController#setUICallbackDispatcherFactory(org.eclipse.riena.ui.core.uiprocess.IProgressVisualizerLocator)
	 */
	public void setUICallbackDispatcherFactory(IProgressVisualizerLocator uiprocessCallBackDispatcherFactory) {
		// do nothing by default
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.viewcontroller.IViewController#setBlocked(boolean)
	 */
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
		// TODO: ausimplementieren
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.viewcontroller.IViewController#isBlocked()
	 */
	public boolean isBlocked() {
		return this.blocked;
	}
}
