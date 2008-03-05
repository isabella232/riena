/*******************************************************************************
 * Copyright (c) 2007 2008 compeople AG and others.
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

import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.communication.core.hooks.CallContext;
import org.eclipse.riena.communication.core.hooks.ICallHook;
import org.eclipse.riena.communication.core.ssl.ISSLProperties;
import org.eclipse.riena.communication.core.ssl.SSLConfiguration;
import org.eclipse.riena.core.RienaActivator;
import org.eclipse.riena.core.extension.util.ExtensionUtility;
import org.eclipse.riena.core.service.ServiceId;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

/**
 * @author Alexander Ziegler
 * @author Christian Campo
 * 
 */
public class Activator extends RienaActivator {

	private static Activator plugin;

	private ServiceReference sslConfigServiceReference;

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
				logger.log(LogService.LOG_DEBUG, "after call (in hook) method=" + context.getMethodName()); //$NON-NLS-1$
				Map<String, List<String>> headers = context.getMessageContext().listResponseHeaders();
				if (headers != null) {
					for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
						logger.log(LogService.LOG_DEBUG, "header: name:" + entry.getKey() + " value: " //$NON-NLS-1$ //$NON-NLS-2$
								+ entry.getValue());
					}
				}
			}

			public void beforeCall(CallContext context) {
				context.getMessageContext().addRequestHeader("Cookie", "x-scpclient-test-sessionid=222"); //$NON-NLS-1$ //$NON-NLS-2$
				logger.log(LogService.LOG_DEBUG, "before call (in hook) method=" + context.getMethodName()); //$NON-NLS-1$
			}
		}, null);

		// context.registerService(ConfigurationPlugin.class.getName(), new
		// SymbolConfigPlugin(), null);

		// SSL configuration
		context.registerService(SSLConfiguration.class.getName(), new SSLConfiguration(), ServiceId
				.newDefaultServiceProperties());
		ISSLProperties[] sslProperties = ExtensionUtility.readExtensions(ISSLProperties.EXTENSION_POINT_ID,
				ISSLProperties.class);
		Assert.isTrue(sslProperties.length < 2, "SSL properties either 0 or 1"); //$NON-NLS-1$
		if (sslProperties.length == 1) {
			sslConfigServiceReference = context.getServiceReference(SSLConfiguration.class.getName());
			SSLConfiguration config = (SSLConfiguration) context.getService(sslConfigServiceReference);
			config.configure(sslProperties[0]);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		if (sslConfigServiceReference != null)
			context.ungetService(sslConfigServiceReference);
		plugin = null;

		super.stop(context);
	}

	public static Activator getDefault() {
		return plugin;
	}

}
