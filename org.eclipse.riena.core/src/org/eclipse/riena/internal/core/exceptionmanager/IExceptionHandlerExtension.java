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
package org.eclipse.riena.internal.core.exceptionmanager;

import org.eclipse.riena.core.exception.IExceptionHandler;
import org.eclipse.riena.core.injector.extension.DefaultValue;
import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;

/**
 * Definition for the ExceptionHandlers that are defined
 * <p>
 * <b>Note:</b> The "org.eclipse.riena.core.exception.handlers" is @deprecated.
 */
@ExtensionInterface(id = "exceptionHandlers,org.eclipse.riena.core.exception.handlers")
public interface IExceptionHandlerExtension {

	String getName();

	@MapName("class")
	IExceptionHandler createExceptionHandler();

	@MapName("class")
	String getExceptionHandler();

	@DefaultValue("*")
	String getBefore();

}
