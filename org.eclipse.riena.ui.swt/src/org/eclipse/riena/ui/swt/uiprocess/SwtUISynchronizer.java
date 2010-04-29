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
package org.eclipse.riena.ui.swt.uiprocess;

import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.log.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.ui.core.uiprocess.IUISynchronizer;

/**
 * Serializes a runnable to the SWT-Thread
 * 
 */
public class SwtUISynchronizer implements IUISynchronizer {

	/**
	 * @see IUISynchronizer#syncExec(Runnable)
	 */
	public void syncExec(Runnable runnable) {
		execute(new SyncExecutor(), runnable);
	}

	/**
	 * @see IUISynchronizer#asyncExec(Runnable)
	 */
	public void asyncExec(Runnable runnable) {
		execute(new ASyncExecutor(), runnable);
	}

	/*
	 * Executes the given runnable using the executor. First checks if there is
	 * a display available.
	 */
	private void execute(Executor executor, Runnable runnable) {
		if (!hasDisplay()) {
			waitForDisplay(15000);
		}
		Display display = getDisplay();
		if (executeOnDisplay(executor, runnable, display)) {
			return;
		}
		getLogger()
				.log(
						LogService.LOG_DEBUG,
						"Platform display not available for execution. " + display != null ? " Display is disposed" : " Display is null"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		if (Display.getDefault() != display) {
			if (executeOnDisplay(executor, runnable, Display.getDefault())) {
				return;
			}
		}

		getLogger()
				.log(
						LogService.LOG_DEBUG,
						"Default display not available for execution. " + display != null ? " Display is disposed" : " Display is null"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		getLogger().log(LogService.LOG_ERROR, "Could not obtain display for runnable"); //$NON-NLS-1$

	}

	private boolean executeOnDisplay(Executor executor, Runnable runnable, Display display) {
		if (null != display && !display.isDisposed()) {
			executor.execute(display, runnable);
			return true;
		}
		return false;
	}

	public Display getDisplay() {
		if (hasDisplay()) {
			return PlatformUI.getWorkbench().getDisplay();
		}
		return null;
	}

	private Logger getLogger() {
		return Log4r.getLogger(org.eclipse.riena.internal.ui.swt.Activator.getDefault(), SwtUISynchronizer.class);
	}

	protected boolean hasDisplay() {
		return PlatformUI.isWorkbenchRunning() && PlatformUI.getWorkbench().getDisplay() != null;
	}

	/**
	 * Wait for display in 500ms increments, up to timeoutMs
	 * 
	 * @param timeoutMs
	 *            time out in ms (positive)
	 */
	private void waitForDisplay(int timeoutMs) {
		Assert.isTrue(timeoutMs >= 0);
		int time = 0;
		do {
			try {
				Thread.sleep(500);
				time += 500;
			} catch (InterruptedException e) {
				return;
			}
		} while (time < timeoutMs && !hasDisplay());
	}

	private interface Executor {
		void execute(Display display, Runnable runnable);
	}

	private static class SyncExecutor implements Executor {
		public void execute(Display display, Runnable runnable) {
			display.syncExec(runnable);
		}
	}

	private static class ASyncExecutor implements Executor {
		public void execute(Display display, Runnable runnable) {
			display.asyncExec(runnable);
		}
	}
}
