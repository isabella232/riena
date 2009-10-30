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
package org.eclipse.riena.internal.core.logging;

import org.osgi.service.log.LogListener;

import org.eclipse.equinox.log.LogFilter;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;
import org.eclipse.riena.internal.core.Activator;

/**
 *
 */
@ExtensionInterface
public interface ILogListenerExtension {

	String ID = Activator.PLUGIN_ID + ".logging.listeners,logListeners"; //$NON-NLS-1$

	String getName();

	@MapName("listener-class")
	LogListener createLogListener();

	@MapName("filter-class")
	LogFilter createLogFilter();

	@MapName("sync")
	boolean isSynchronous();
}
