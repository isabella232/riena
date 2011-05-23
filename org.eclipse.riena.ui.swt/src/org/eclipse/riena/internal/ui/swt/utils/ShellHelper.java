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
package org.eclipse.riena.internal.ui.swt.utils;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.Log4r;

/**
 * This class helps to maximize or restore a shell.
 */
public class ShellHelper {

	private final static Logger LOGGER = Log4r.getLogger(ShellHelper.class);

	private Rectangle resoreBounds;

	/**
	 * Maximizes or restores the shell of the active workbench.
	 */
	public void maximizeRestore() {
		final Shell shell = RcpUtilities.getWorkbenchShell();
		if (shell == null) {
			LOGGER.log(LogService.LOG_WARNING, "No shell of the application found! Maximize/restore of shell canceled."); //$NON-NLS-1$
			return;
		}
		if (isShellTitleless()) {
			if (isShellMaximzed()) {
				if (resoreBounds != null) {
					shell.setBounds(resoreBounds);
				} else {
					LOGGER.log(LogService.LOG_WARNING, "No restore bounds exists! Restore of shell canceled."); //$NON-NLS-1$
				}
			} else {
				resoreBounds = shell.getBounds();
				final Rectangle clientBounds = shell.getMonitor().getClientArea();
				shell.setBounds(clientBounds);
			}
		} else {
			shell.setMaximized(shell.getMaximized());
		}
	}

	/**
	 * Returns whether the shell is currently maximized or not.
	 * 
	 * @return {@code true} if the shell is maximized; {@code false} if the
	 *         shell isn't maximized.
	 */
	public static boolean isShellMaximzed() {
		final Shell shell = RcpUtilities.getWorkbenchShell();
		if (shell == null) {
			LOGGER.log(LogService.LOG_WARNING, "No shell of the application found!"); //$NON-NLS-1$
			return false;
		}
		return isMaximzed(shell);
	}

	/**
	 * Returns whether the given shell is currently maximized or not.
	 * 
	 * @param shell
	 *            shell to check
	 * @return {@code true} if the shell is maximized; {@code false} if the
	 *         shell isn't maximized.
	 */
	private static boolean isMaximzed(final Shell shell) {
		if ((shell == null) || shell.isDisposed()) {
			LOGGER.log(LogService.LOG_WARNING, "shell equals null or is disposed!"); //$NON-NLS-1$
			return false;
		}
		if (isShellTitleless()) {
			final Rectangle clientBounds = shell.getMonitor().getClientArea();
			return clientBounds.equals(shell.getBounds());
		} else {
			return shell.getMaximized();
		}
	}

	/**
	 * Returns whether the given shell has the default shell style with (title
	 * bar, border, system menu etc.) or not.
	 * 
	 * @param shell
	 *            shell to check
	 * @return {@code true} not default shell style (no title bar etc.);
	 *         {@code false] default shell style
	 */
	private static boolean isTitleless(final Shell shell) {
		if ((shell == null) || shell.isDisposed()) {
			LOGGER.log(LogService.LOG_WARNING, "shell equals null or is disposed!"); //$NON-NLS-1$
			return false;
		}
		final int style = shell.getStyle();
		return (style & SWT.NO_TRIM) == SWT.NO_TRIM;
	}

	/**
	 * Returns whether the shell has the default shell style with (title bar,
	 * border, system menu etc.) or not.
	 * 
	 * @return {@code true} not default shell style (no title bar etc.);
	 *         {@code false] default shell style
	 */
	private static boolean isShellTitleless() {
		final Shell shell = RcpUtilities.getWorkbenchShell();
		if (shell == null) {
			LOGGER.log(LogService.LOG_WARNING, "No shell of the application found!"); //$NON-NLS-1$
			return false;
		}
		return isTitleless(shell);
	}

}
