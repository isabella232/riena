/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.swt.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.core.singleton.SessionSingletonProvider;
import org.eclipse.riena.internal.ui.swt.console.UIControlsStatisticConsole;

/**
 * Statistic Counter of {@link Control}s created by {@link UIControlsFactory}.
 * Used by {@link UIControlsStatisticConsole} to get statistics.
 */
public class UIControlsCounter {

	private final static String STAR = "*"; //$NON-NLS-1$
	private static final String HEADER = " Control Usage Statistics "; //$NON-NLS-1$
	private static final int NOS = 10;

	private static final SessionSingletonProvider<UIControlsCounter> UICC = new SessionSingletonProvider<UIControlsCounter>(
			UIControlsCounter.class);

	private final Map<Class<?>, Integer> counter = new HashMap<Class<?>, Integer>();

	/**
	 * @return - the Singleton instance of {@link UIControlsCounter}
	 */
	public static UIControlsCounter getUIControlsCounterSingleton() {
		return UICC.getInstance();
	}

	/**
	 * Register the given control for statistic reasons
	 * 
	 * @param control
	 *            - the control to register
	 */
	public void registerConstruction(final Object control) {
		final Class<?> clazz = control.getClass();
		if (!counter.containsKey(clazz)) {
			counter.put(clazz, 1);
		} else {
			counter.put(clazz, counter.get(clazz) + 1);

		}
	}

	/**
	 * Get a formatted String of Control usage statistics
	 * 
	 * @return - the statistic in String format
	 */
	public String getControlUsageStatistics() {
		final StringBuilder strb = new StringBuilder();
		appendNTimes(STAR, NOS, strb);
		strb.append(HEADER);
		appendNTimes(STAR, NOS, strb);
		newLine(strb);
		final Set<Class<?>> classes = counter.keySet();
		for (final Class<?> clazz : classes) {
			strb.append("Control Type: "); //$NON-NLS-1$
			strb.append(clazz.getName());
			strb.append("\tInstance Count: "); //$NON-NLS-1$
			strb.append(counter.get(clazz));
			newLine(strb);
		}
		appendNTimes(STAR, 2 * NOS + HEADER.length(), strb);
		return strb.toString();
	}

	///// Helping Methods

	private static void newLine(final StringBuilder strb) {
		strb.append("\n"); //$NON-NLS-1$
	}

	private static void appendNTimes(final String str, final int n, final StringBuilder strb) {
		for (int i = 0; i < n; i++) {
			strb.append(str);
		}
	}

}
