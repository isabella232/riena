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
package org.eclipse.riena.internal.core.exceptionmanager;

import org.eclipse.riena.core.exception.IExceptionHandlerManager;
import org.eclipse.riena.core.service.Service;
import org.eclipse.riena.internal.core.Activator;

/**
 * returns the current ExceptionHandlerManger
 */
public final class ExceptionHandlerManagerAccessor {

	/**
	 * @param context
	 */
	private ExceptionHandlerManagerAccessor() {
		// utility
	}

	/**
	 * @return
	 * @deprecated This should be replaced with
	 *             {@code
	 *             Service.get(Activator.getDefault().getContext(),
	 *             IExceptionHandlerManager.class);} or with
	 *             {@code With.service(..).doo(...);}
	 */
	@Deprecated
	public static IExceptionHandlerManager getExceptionHandlerManager() {
		return Service.get(Activator.getDefault().getContext(), IExceptionHandlerManager.class);
	}
}
