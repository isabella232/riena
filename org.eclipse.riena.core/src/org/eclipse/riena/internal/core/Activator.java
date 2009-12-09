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
package org.eclipse.riena.internal.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.RienaConstants;
import org.eclipse.riena.core.RienaPlugin;
import org.eclipse.riena.core.exception.IExceptionHandlerManager;
import org.eclipse.riena.core.util.VariableManagerUtil;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.core.exceptionmanager.SimpleExceptionHandlerManager;
import org.eclipse.riena.internal.core.ignore.IgnoreFindBugs;
import org.eclipse.riena.internal.core.logging.LoggerMill;

public class Activator extends RienaPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.core"; //$NON-NLS-1$

	// ´startup´ status of Riena
	private boolean active = false;

	private ServiceRegistration loggerMillServiceReg;
	private LoggerMill loggerMill;

	// The shared instance
	@IgnoreFindBugs(value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD", justification = "that is the eclipse way")
	private static Activator plugin;
	{
		plugin = this;
	}

	@Override
	@IgnoreFindBugs(value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD", justification = "that is the eclipse way")
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		Activator.plugin = this;
		startLogging();
		logStage(getLogger(Activator.class));
		startStartupListener();
		startExceptionHandling();
		populateSystemPropertiesAsVariables();
	}

	/**
	 * 
	 */
	private void startLogging() {
		loggerMill = new LoggerMill();
		Wire.instance(loggerMill).andStart(getContext());
		loggerMillServiceReg = getContext().registerService(LoggerMill.class.getName(), loggerMill,
				RienaConstants.newDefaultServiceProperties());
	}

	/**
	 * @param logger
	 */
	private void logStage(final Logger logger) {
		final IStringVariableManager variableManager = VariablesPlugin.getDefault().getStringVariableManager();
		String stage;
		try {
			stage = variableManager.performStringSubstitution("Riena is running in stage '${riena.stage}'."); //$NON-NLS-1$
		} catch (final CoreException e) {
			stage = "No stage information set."; //$NON-NLS-1$
		}
		logger.log(LogService.LOG_INFO, stage);
	}

	private void startStartupListener() {
		final BundleListener bundleListener = new StartupBundleListener();
		getContext().addBundleListener(bundleListener);
	}

	private void startExceptionHandling() {
		final SimpleExceptionHandlerManager handlerManager = new SimpleExceptionHandlerManager();
		Wire.instance(handlerManager).andStart(getContext());
		getContext().registerService(IExceptionHandlerManager.class.getName(), handlerManager,
				RienaConstants.newDefaultServiceProperties());
	}

	/**
	 * Populate all system properties plus a few synthetic properties as
	 * variables (the name is the key of the property prefixed with "riena.")
	 * into the variable manager.
	 * 
	 * @throws CoreException
	 */
	private void populateSystemPropertiesAsVariables() throws CoreException {
		final Properties properties = System.getProperties();
		final Map<String, String> variables = new HashMap<String, String>(properties.size());
		for (final Entry<Object, Object> entry : properties.entrySet()) {
			variables.put(RIENA + entry.getKey(), (String) entry.getValue());
		}
		addSyntheticProperties(variables);
		VariableManagerUtil.addVariables(variables);
	}

	@Override
	@IgnoreFindBugs(value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD", justification = "that is the eclipse way")
	public void stop(final BundleContext context) throws Exception {
		active = false;
		Activator.plugin = null;
		context.ungetService(loggerMillServiceReg.getReference());
		super.stop(context);
	}

	/**
	 * Get the plugin instance.
	 * 
	 * @return
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Riena status?
	 * 
	 * @return
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Prefix for "riena" variables.
	 */
	private static final String RIENA = "riena."; //$NON-NLS-1$

	/**
	 * ´Synthetic´ property that retrieves the hosts ip address, i.e. {@code
	 * InetAddress.getLocalHost().getHostAddress()}
	 */
	private static final String HOST_ADDRESS = RIENA + "host.address"; //$NON-NLS-1$

	/**
	 * ´Synthetic´ property that retrieves the host name, i.e. {@code
	 * InetAddress.getLocalHost().getHostName()}
	 */
	private static final String HOST_NAME = RIENA + "host.name"; //$NON-NLS-1$

	private void addSyntheticProperties(final Map<String, String> variables) {
		variables.put(HOST_NAME, getHostName());
		variables.put(HOST_ADDRESS, getHostAddress());
	}

	private static final String UNKNOWN = "?"; //$NON-NLS-1$

	private String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (final UnknownHostException e) {
			return UNKNOWN;
		}
	}

	private String getHostAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (final UnknownHostException e) {
			return UNKNOWN;
		}
	}

	/**
	 *
	 */
	private class StartupBundleListener implements BundleListener {

		public void bundleChanged(final BundleEvent event) {
			if (Activator.getDefault() == null) {
				return;
			}
			if (event.getBundle() == getContext().getBundle() && event.getType() == BundleEvent.STARTED) {
				active = true;
				final ISafeRunnable safeRunnable = new StartupsSafeRunnable();
				Wire.instance(safeRunnable).andStart(getContext());
				SafeRunner.run(safeRunnable);
				getContext().removeBundleListener(this);
			}
		}
	}

}
