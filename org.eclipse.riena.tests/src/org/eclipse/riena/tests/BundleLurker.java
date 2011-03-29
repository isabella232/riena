/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.tests;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

import org.eclipse.riena.internal.tests.Activator;

/**
 * The {@code BundleLurker} waits until the bundle specified by the symbolic
 * name is active.
 * 
 * @Deprecated This class is no longer be used! Can be removed in the future.
 */
@Deprecated
public class BundleLurker {

	private final String bundleName;
	private final CountDownLatch latch = new CountDownLatch(1);

	/**
	 * Create a lurker.
	 * 
	 * @param bundleName
	 *            symbolic bundle name
	 */
	public BundleLurker(final String bundleName) {
		this.bundleName = bundleName;
		Activator.getDefault().getBundle().getBundleContext().addBundleListener(new StartedListener());
		final Bundle[] bundles = Activator.getDefault().getBundle().getBundleContext().getBundles();
		for (final Bundle bundle : bundles) {
			if (bundle.getSymbolicName().equals(bundleName) && bundle.getState() == Bundle.ACTIVE) {
				latch.countDown();
				return;
			}
		}
	}

	/**
	 * Wait until the bundle this lurker is watching for gets active within the
	 * given timeout.
	 * 
	 * @param timeout
	 *            the duration when the lurker stops waiting
	 * @param unit
	 *            the time unit of the timeout
	 * @return true everything ok; otherwise false (e.g. timeout,
	 *         InterruptedException)
	 */
	public boolean awaitActive(final long timeout, final TimeUnit unit) {
		try {
			return latch.await(timeout, unit);
		} catch (final InterruptedException e) {
			return false;
		}
	}

	/**
	 * Wait until the bundle this lurker is watching for gets active.
	 * 
	 * @return true everything ok; otherwise false (InterruptedException)
	 */
	public boolean awaitActive() {
		try {
			latch.await();
			return true;
		} catch (final InterruptedException e) {
			return false;
		}
	}

	private class StartedListener implements BundleListener {

		public void bundleChanged(final BundleEvent event) {
			if (event.getBundle().getSymbolicName().equals(bundleName) && event.getType() == BundleEvent.STARTED) {
				Activator.getDefault().getBundle().getBundleContext().removeBundleListener(this);
				latch.countDown();
			}
		}

	}
}
