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
package org.eclipse.riena.internal.core.exceptionmanager;

import org.eclipse.riena.core.exception.IExceptionHandlerManager;
import org.eclipse.riena.core.util.ServiceAccessor;
import org.eclipse.riena.core.wire.WireWrap;
import org.eclipse.riena.internal.core.Activator;

/**
 * returns the current ExceptionHandlerManger
 */
@WireWrap(ExceptionHandlerManagerAccessorWireWrap.class)
public class ExceptionHandlerManagerAccessor extends ServiceAccessor<IExceptionHandlerManager> {

	private final static ExceptionHandlerManagerAccessor EXCEPTION_HANDLER_MANAGER_ACCESSOR = new ExceptionHandlerManagerAccessor();

	/**
	 * @param context
	 */
	public ExceptionHandlerManagerAccessor() {
		super(Activator.getDefault().getContext());
	}

	public static IExceptionHandlerManager getExceptionHandlerManager() {
		return EXCEPTION_HANDLER_MANAGER_ACCESSOR.getService();
	}

}
