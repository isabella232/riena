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
package org.eclipse.riena.ui.swt.utils;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.internal.ui.swt.Activator;

/**
 * Helper class for setting 'rienaid' on widgets.
 */
public class WidgetIdentificationSupport {

	public static final String RIENA_ID = "rienaid"; //$NON-NLS-1$
	private static Logger LOGGER = Activator.getDefault().getLogger(WidgetIdentificationSupport.class);

	/**
	 * Sets rienaid for shell. If 'riena.testing.widgetid.mainshell' system
	 * property is set - it's value if used. otherwise rienaid is set to value
	 * 'default'
	 * 
	 * @param aShell
	 *            - shell
	 */
	public static void setIdentification(Shell aShell) {
		String id = System.getProperty("riena.testing.widgetid.mainshell"); //$NON-NLS-1$

		aShell.setData(RIENA_ID, id != null ? id : "default"); //$NON-NLS-1$
	}

	/**
	 * Sets rienaid for widget. dot-separated concatenated id parts are used as
	 * a value. i.e. parts: ['a', 'b', 'c'], value: 'a.b.c'
	 * 
	 * @param aWidget
	 *            - widget
	 * @param aParts
	 *            - sequence of id parts
	 */
	public static void setIdentification(Widget aWidget, String... aParts) {
		StringBuffer fullId = new StringBuffer();

		for (String part : aParts) {
			if (fullId.length() != 0)
				fullId.append('.');

			fullId.append(part);
		}

		LOGGER.log(LogService.LOG_DEBUG, String
				.format("registering widget %s, (class: %s)", fullId, aWidget.getClass())); //$NON-NLS-1$
		aWidget.setData(RIENA_ID, fullId.toString());
	}

	/**
	 * Sets default rienaid for a widget. String representation of widget class
	 * name is used as a value
	 * 
	 * @param aWidget
	 *            - widget
	 */
	public static void setDefaultIdentification(Widget aWidget) {
		setIdentification(aWidget, aWidget.getClass().getName());
	}
}
