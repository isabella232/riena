/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.core.logging;

import org.osgi.framework.BundleContext;

import org.eclipse.equinox.log.ExtendedLogReaderService;
import org.eclipse.equinox.log.ExtendedLogService;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.wire.AbstractWiring;

/**
 * Wires the {@codeLoggerMill}.
 */
public class LoggerMillWiring extends AbstractWiring {

	@Override
	public void wire(Object bean, BundleContext context) {
		// get log catchers
		Inject.extension(ILogCatcherExtension.ID).useType(ILogCatcherExtension.class).into(bean).andStart(context);

		// get log listeners
		Inject.extension(ILogListenerExtension.ID).useType(ILogListenerExtension.class).into(bean).andStart(context);

		Inject.service(ExtendedLogReaderService.class).useRanking().into(bean).andStart(context);
		Inject.service(ExtendedLogService.class).useRanking().into(bean).andStart(context);
	}

}
