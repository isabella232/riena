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
package org.eclipse.riena.ui.ridgets.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.ui.core.context.IContext;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IWindowRidget;

/**
 * Controller for a view that is or has a window.
 * 
 */
public abstract class AbstractWindowController implements IController, IContext {

	/**
	 * The ridget id to use for the window ridget.
	 */
	public static final String RIDGET_ID_WINDOW = "windowRidget"; //$NON-NLS-1$

	private IWindowRidget windowRidget;
	private Map<String, IRidget> ridgets;
	private boolean blocked;
	private Map<String, Object> context;

	public AbstractWindowController() {
		super();
		ridgets = new HashMap<String, IRidget>();
		context = new HashMap<String, Object>();
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

	public void addRidget(String id, IRidget ridget) {
		ridgets.put(id, ridget);
	}

	public IRidget getRidget(String id) {
		return ridgets.get(id);
	}

	public Collection<? extends IRidget> getRidgets() {
		return ridgets.values();
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
		// TODO: ausimplementieren
	}

	public boolean isBlocked() {
		return this.blocked;
	}

	public void configureRidgets() {
		setWindowRidget((IWindowRidget) getRidget(RIDGET_ID_WINDOW));
	}

	public void afterBind() {
		getWindowRidget().updateFromModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.internal.ui.core.context.IContext#setContext(java.lang
	 * .String, java.lang.Object)
	 */
	/**
	 * @since 1.2
	 */
	public void setContext(String key, Object value) {
		context.put(key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.internal.ui.core.context.IContext#getContext(java.lang
	 * .String)
	 */
	/**
	 * @since 1.2
	 */
	public Object getContext(String key) {
		return context.get(key);
	}
}
