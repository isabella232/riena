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
package org.eclipse.riena.internal.core.logging;

import org.eclipse.equinox.log.ExtendedLogReaderService;
import org.eclipse.equinox.log.ExtendedLogService;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.wire.IWireWrap;
import org.osgi.framework.BundleContext;

/**
 * Wires the {@codeLoggerMill}.
 */
public class LoggerMillWireWrap implements IWireWrap {

	public void wire(Object bean, BundleContext context) {
		// get log catchers
		Inject.extension(ILogCatcherDefinition.EXTENSION_POINT).useType(ILogCatcherDefinition.class).into(bean)
				.andStart(context);

		// get log listeners
		Inject.extension(ILogListenerDefinition.EXTENSION_POINT).useType(ILogListenerDefinition.class).into(bean)
				.andStart(context);

		Inject.service(ExtendedLogReaderService.class).useRanking().into(bean).andStart(context);
		Inject.service(ExtendedLogService.class).useRanking().into(bean).andStart(context);
	}

}
