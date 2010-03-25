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
 * serializes a runnable to the SWT-Thread - if possible
 */
public class SwtUISynchronizer implements IUISynchronizer {

	public void synchronize(Runnable runnable) {
		if (!hasDisplay()) {
			waitForDisplay(15000);
		}
		Display display = getDisplay();
		if (null != display) {
			if (!display.isDisposed()) {
				display.syncExec(runnable);
				return;
			}
		}
		getLogger().log(LogService.LOG_ERROR, "Could not obtain display for runnable"); //$NON-NLS-1$
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

	private boolean hasDisplay() {
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

}
