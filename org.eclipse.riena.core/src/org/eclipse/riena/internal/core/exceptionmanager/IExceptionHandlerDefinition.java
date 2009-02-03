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

import org.eclipse.riena.core.exception.IExceptionHandler;
import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;
import org.eclipse.riena.internal.core.Activator;

/**
 * Definition for the ExceptionHandlers that are defined
 */
@ExtensionInterface
public interface IExceptionHandlerDefinition {

	String EXTENSION_POINT = Activator.PLUGIN_ID + ".exception.handlers"; //$NON-NLS-1$

	String getName();

	@MapName("class")
	IExceptionHandler createExceptionHandler();

	@MapName("class")
	String getExceptionHandler();

	String getBefore();

}
