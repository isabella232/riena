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
package org.eclipse.riena.core.exception;

import org.eclipse.equinox.log.Logger;

/**
 * Test class for ExceptionHandler
 */
public class TestExceptionHandler implements IExceptionHandler {

	private final String name;
	private final String before;
	private IExceptionHandler.Action action = IExceptionHandler.Action.NOT_HANDLED;
	private Throwable throwable;

	public TestExceptionHandler(final String name, final String before, final IExceptionHandler.Action action) {
		this.name = name;
		this.before = before;
		if (action != null) {
			this.action = action;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.core.exception.IExceptionHandler#getBefore()
	 */
	public String getBefore() {
		return before;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.core.exception.IExceptionHandler#getName()
	 */
	public String getName() {
		return name;
	}

	Throwable getThrowable() {
		return throwable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.core.exception.IExceptionHandler#handleCaught(java.
	 * lang.Throwable, java.lang.Object, org.eclipse.equinox.log.Logger)
	 */
	public IExceptionHandler.Action handleException(final Throwable t, final String msg, final Logger logger) {
		throwable = t;
		return action;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.core.exception.IExceptionHandler#handleUncaught(java
	 * .lang.Throwable, java.lang.Object, org.eclipse.equinox.log.Logger)
	 */
	public IExceptionHandler.Action handleUncaught(final Throwable t, final String msg, final Logger logger) {
		throwable = t;
		return action;
	}

}
