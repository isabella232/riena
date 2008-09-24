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
package org.eclipse.riena.core.util;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.internal.core.Activator;
import org.osgi.service.log.LogService;

/**
 * The <code>VariableManagerUtil</code> is lightweight wrapper for the
 * <code>StringVariableManager</code>.
 */
public final class VariableManagerUtil {

	private final static Logger LOGGER = Activator.getDefault().getLogger(VariableManagerUtil.class.getName());

	private VariableManagerUtil() {
		// utility
	}

	/**
	 * Perform a string substitution on the given expression.
	 * 
	 * @param expression
	 * @return the substituted expression
	 * @throws CoreException
	 */
	public static String substitute(final String expression) throws CoreException {
		final IStringVariableManager variableManager = VariablesPlugin.getDefault().getStringVariableManager();
		return variableManager.performStringSubstitution(expression);
	}

	/**
	 * Add a new string variable to the <code>StringVariableManager</code>.
	 * 
	 * @param key
	 * @param value
	 * @throws CoreException
	 */
	public static void addVariable(final String key, final String value) throws CoreException {
		final IStringVariableManager variableManager = VariablesPlugin.getDefault().getStringVariableManager();
		final IValueVariable[] variables = new IValueVariable[] { variableManager.newValueVariable((String) key, null,
				true, (String) value) };
		try {
			variableManager.addVariables(variables);
		} catch (CoreException e) {
			final IValueVariable existingValue = variableManager.getValueVariable((String) key);
			if (existingValue.getValue().equals(value)) {
				LOGGER.log(LogService.LOG_WARNING, "Already defined: (" + key + "," + value + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			} else {
				throw e;
			}
		}
	}

}
