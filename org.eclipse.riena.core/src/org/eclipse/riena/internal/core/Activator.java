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
package org.eclipse.riena.internal.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.RienaConstants;
import org.eclipse.riena.core.RienaPlugin;
import org.eclipse.riena.core.exception.IExceptionHandlerManager;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.core.exceptionmanager.ExceptionHandlerManagerAccessor;
import org.eclipse.riena.internal.core.exceptionmanager.IExceptionHandlerDefinition;
import org.eclipse.riena.internal.core.exceptionmanager.SimpleExceptionHandlerManager;
import org.eclipse.riena.internal.core.logging.LoggerMill;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.log.LogService;

public class Activator extends RienaPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.core"; //$NON-NLS-1$

	// ´startup´ status of Riena
	private boolean active = false;

	private ServiceRegistration loggerMillServiceReg;
	private LoggerMill loggerMill;
	private ExceptionHandlerManagerAccessor exceptionHandlerManagerAccessor;

	// The shared instance
	private static Activator plugin;
	{
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		Activator.plugin = this;
		startLogging();
		Logger logger = getLogger(Activator.class);

		logStage(logger);
		startStartupListener();

		//		SimpleExceptionHandler handler = new SimpleExceptionHandler();
		//		context.registerService(IExceptionHandler.class.getName(), handler, RienaConstants
		//				.newDefaultServiceProperties());

		// create simple exceptionhandler manager, inject all IExceptionHandler extensions and register it as service
		SimpleExceptionHandlerManager handlerManager = new SimpleExceptionHandlerManager();
		//		String handlerId = IExceptionHandler.class.getName();
		//
		//		Inject.service(handlerId).into(handlerManager).andStart(getContext());
		Inject.extension(IExceptionHandlerDefinition.EXTENSION_POINT).into(handlerManager).andStart(context);

		context.registerService(IExceptionHandlerManager.class.getName(), handlerManager, RienaConstants
				.newDefaultServiceProperties());

		// create some instance of ExceptionHandlerManagerAccessor and inject ONE ExceptionHandlerManger
		this.exceptionHandlerManagerAccessor = new ExceptionHandlerManagerAccessor();
		Inject.service(IExceptionHandlerManager.class).useRanking().into(exceptionHandlerManagerAccessor).andStart(
				context);
	}

	/**
	 * 
	 */
	private void startLogging() {
		loggerMill = new LoggerMill(getContext());
		loggerMillServiceReg = getContext().registerService(LoggerMill.class.getName(), loggerMill,
				RienaConstants.newDefaultServiceProperties());
	}

	/**
	 * @param logger
	 */
	private void logStage(Logger logger) {
		IStringVariableManager variableManager = VariablesPlugin.getDefault().getStringVariableManager();
		String stage;
		try {
			stage = variableManager.performStringSubstitution("Riena is running in stage '${riena.stage}'."); //$NON-NLS-1$
		} catch (CoreException e) {
			stage = "No stage information set."; //$NON-NLS-1$
		}
		logger.log(LogService.LOG_INFO, stage);
	}

	private void startStartupListener() {
		BundleListener bundleListener = new StartupBundleListener();
		getContext().addBundleListener(bundleListener);
	}

	/*
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
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
	 *
	 */
	private class StartupBundleListener implements BundleListener {

		public void bundleChanged(BundleEvent event) {
			if (Activator.getDefault() == null) {
				return;
			}
			if (event.getBundle() == Activator.getDefault().getContext().getBundle()
					&& event.getType() == BundleEvent.STARTED) {
				active = true;
				final ISafeRunnable safeRunnable = new StartupsSafeRunnable();
				Inject.extension("org.eclipse.riena.core.startups").into(safeRunnable).andStart( //$NON-NLS-1$
						Activator.getDefault().getContext());
				SafeRunner.run(safeRunnable);
			}
		}
	}

}
