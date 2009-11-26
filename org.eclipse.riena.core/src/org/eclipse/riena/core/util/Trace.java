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
package org.eclipse.riena.core.util;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import org.eclipse.core.runtime.Platform;

/**
 * Tracing support.
 */
public final class Trace {

	private static final String SLASH = "/"; //$NON-NLS-1$

	private Trace() {
		// utility
	}

	/**
	 * This checks whether debug is requested for the debug option string
	 * created from the method parameters as follows:
	 * 
	 * <pre>
	 * &lt;symbolic bundle name of bundle containing clazz&gt; { "/" &lt;simple name of clazz&gt; } "/" &lt;option&gt;
	 * </pre>
	 * 
	 * If {@code clazz} is null the option string misses the { } portion.
	 * 
	 * @param clazz
	 *            the class requesting trace support or null
	 * @param option
	 *            the option string
	 * @return
	 * 
	 * @see Platform.getDebugOption()
	 */
	public static boolean isOn(Class<?> clazz, String option) {
		if (!Platform.inDebugMode()) {
			return false;
		}
		final Bundle bundle = FrameworkUtil.getBundle(clazz);
		if (bundle == null) {
			return false;
		}
		final StringBuilder bob = new StringBuilder(bundle.getSymbolicName());
		if (clazz != null) {
			bob.append(SLASH).append(clazz.getSimpleName());
		}
		bob.append(SLASH).append(option);
		final String debug = Platform.getDebugOption(bob.toString());
		return Boolean.parseBoolean(debug);
	}
}
