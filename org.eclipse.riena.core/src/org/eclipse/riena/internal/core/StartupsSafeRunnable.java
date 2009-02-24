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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.exception.IExceptionHandlerManager;
import org.eclipse.riena.core.util.PropertiesUtils;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.core.wire.WireWith;
import org.eclipse.riena.internal.core.exceptionmanager.ExceptionHandlerManagerAccessor;
import org.eclipse.riena.internal.core.ignore.IgnoreFindBugs;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.service.log.LogService;

/**
 * Execute the startup actions
 */
@WireWith(StartupsSafeRunnableWiring.class)
public class StartupsSafeRunnable implements ISafeRunnable {

	private IRienaStartupExtension[] startups;

	private final static Logger LOGGER = Activator.getDefault().getLogger(StartupsSafeRunnable.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.ISafeRunnable#handleException(java.lang.
	 * Throwable)
	 */
	public void handleException(Throwable exception) {
		IExceptionHandlerManager manager = ExceptionHandlerManagerAccessor.getExceptionHandlerManager();
		if (manager != null) {
			manager.handleException(exception, "Error activating bundels.", LOGGER); //$NON-NLS-1$
			return;
		}
		LOGGER.log(LogService.LOG_ERROR, "Error activating bundels.", exception); //$NON-NLS-1$
	}

	/*
	 * Execute all startup actions
	 * 
	 * @see org.eclipse.core.runtime.ISafeRunnable#run()
	 */
	public void run() throws Exception {
		if (startups == null) {
			return;
		}
		// handle required bundles first!
		for (IRienaStartupExtension startup : startups) {
			String[] bundleNames = PropertiesUtils.asArray(startup.getRequiredBundles());
			for (String bundleName : bundleNames) {
				if (StringUtils.isEmpty(bundleName)) {
					continue;
				}
				Bundle bundle = Platform.getBundle(bundleName);
				if (bundle != null) {
					if (bundle.getState() != Bundle.ACTIVE) {
						start(bundle);
					} else {
						LOGGER.log(LogService.LOG_INFO, "Startup required bundle: '" + bundleName //$NON-NLS-1$
								+ "' already started."); //$NON-NLS-1$
					}
				} else {
					LOGGER.log(LogService.LOG_WARNING, "Startup required bundle: '" + bundleName //$NON-NLS-1$
							+ "' not found."); //$NON-NLS-1$
				}
			}
		}
		// handle bundle ´self´ activation either by creating and executing the ´load class´ or by activating the bundle 
		for (IRienaStartupExtension startup : startups) {
			Bundle bundle = startup.getContributingBundle();
			String runClassName = startup.getRunClassName();
			if (StringUtils.isGiven(runClassName)) {
				// try to load and execute the ´starter´ class
				try {
					startup.createRunner().run();
					LOGGER.log(LogService.LOG_INFO, "Startup: '" + bundle.getSymbolicName() + "' with starter '" //$NON-NLS-1$ //$NON-NLS-2$
							+ runClassName + "' succesful."); //$NON-NLS-1$
				} catch (Exception e) {
					LOGGER.log(LogService.LOG_ERROR, "Startup: '" + bundle.getSymbolicName() + "' with starter '" //$NON-NLS-1$ //$NON-NLS-2$
							+ runClassName + "' failed.", e); //$NON-NLS-1$
				}
			}
			if (!startup.isActivateSelf()) {
				continue;
			}
			if (bundle.getState() == Bundle.RESOLVED) {
				start(bundle);
			} else if (bundle.getState() == Bundle.STARTING
					&& Constants.ACTIVATION_LAZY.equals(bundle.getHeaders().get(Constants.BUNDLE_ACTIVATIONPOLICY))) {
				if (!new BundleActivationWaiter(bundle).waitFor()) {
					start(bundle);
				}
				LOGGER.log(LogService.LOG_INFO, "Startup <<lazy>>: '" + bundle.getSymbolicName() + "' succesful."); //$NON-NLS-1$ //$NON-NLS-2$
			} else if (bundle.getState() == Bundle.INSTALLED) {
				LOGGER.log(LogService.LOG_ERROR, "Startup: '" + bundle.getSymbolicName() //$NON-NLS-1$
						+ "' failed. Startup extension is set but is only in state INSTALLED (not RESOLVED)."); //$NON-NLS-1$
			} else if (bundle.getState() == Bundle.ACTIVE) {
				LOGGER.log(LogService.LOG_DEBUG, "Startup: '" + bundle.getSymbolicName() + "' is already ACTIVE."); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

	}

	private void start(Bundle bundle) throws BundleException {
		if (bundle == null) {
			return;
		}
		try {
			bundle.start();
			LOGGER.log(LogService.LOG_INFO, "Startup: '" + bundle.getSymbolicName() + "' succesful."); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (RuntimeException rte) {
			LOGGER.log(LogService.LOG_ERROR, "Startup: '" + bundle.getSymbolicName() //$NON-NLS-1$
					+ "' failed with exception.", rte); //$NON-NLS-1$
		}
	}

	@IgnoreFindBugs(value = "EI_EXPOSE_REP2", justification = "deep cloning the ´startups´ is too expensive")
	public void update(IRienaStartupExtension[] startups) {
		this.startups = startups;
	}

	/**
	 * This helper allows waiting for bundle until it gets activated or a
	 * timeout occurred while waiting.
	 */
	private final class BundleActivationWaiter implements SynchronousBundleListener {
		private final Bundle bundle;
		private final CountDownLatch latch = new CountDownLatch(1);

		private BundleActivationWaiter(Bundle bundle) {
			Assert.isTrue(bundle != null, "bundle must not be null."); //$NON-NLS-1$
			this.bundle = bundle;
		}

		/**
		 * Wait for bundle getting started.
		 * 
		 * @return false if waiting ended with a timeout or {@code
		 *         InterruptedException}; otherwise true
		 */
		private boolean waitFor() {
			try {
				Activator.getDefault().getBundle().getBundleContext().addBundleListener(this);
				if (bundle.getState() == Bundle.ACTIVE) {
					return true;
				}
				if (!latch.await(1, TimeUnit.SECONDS)) {
					LOGGER.log(LogService.LOG_DEBUG, "Waiting for bundle " + bundle.getSymbolicName() //$NON-NLS-1$
							+ " elapsed timeout."); //$NON-NLS-1$
					return false;
				}
			} catch (InterruptedException e) {
				LOGGER.log(LogService.LOG_WARNING, "Waiting for bundle " + bundle.getSymbolicName() //$NON-NLS-1$
						+ " got interruped.", e); //$NON-NLS-1$
				return false;
			} finally {
				Activator activator = Activator.getDefault();
				if (activator != null) {
					Activator.getDefault().getBundle().getBundleContext().removeBundleListener(this);
				}
			}
			return true;
		}

		public void bundleChanged(BundleEvent event) {
			if (event.getType() == BundleEvent.STARTED && event.getBundle() == bundle) {
				latch.countDown();
			}
		}

	}
}