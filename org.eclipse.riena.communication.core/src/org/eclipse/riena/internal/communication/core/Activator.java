/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.communication.core;

import java.util.List;
import java.util.Map;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.communication.core.hooks.CallContext;
import org.eclipse.riena.communication.core.hooks.ICallHook;
import org.eclipse.riena.core.RienaActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

/**
 * @author Alexander Ziegler
 * @author Christian Campo
 * 
 */
public class Activator extends RienaActivator {

	private static Activator plugin;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		final Logger logger = getLogger(Activator.class.getName());
		context.registerService(ICallHook.ID, new ICallHook() {

			public void afterCall(CallContext context) {
				logger.log(LogService.LOG_DEBUG, "after call (in hook) method=" + context.getMethodName());
				Map<String, List<String>> headers = context.getMessageContext().listResponseHeaders();
				if (headers != null) {
					for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
						logger.log(LogService.LOG_DEBUG, "header: name:" + entry.getKey() + " value: "
								+ entry.getValue());
					}
				}
			}

			public void beforeCall(CallContext context) {
				context.getMessageContext().addRequestHeader("Cookie", "x-scpclient-test-sessionid=222");
				logger.log(LogService.LOG_DEBUG, "before call (in hook) method=" + context.getMethodName());
			}
		}, null);

		// context.registerService(ConfigurationPlugin.class.getName(), new
		// SymbolConfigPlugin(), null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return plugin;
	}

}
