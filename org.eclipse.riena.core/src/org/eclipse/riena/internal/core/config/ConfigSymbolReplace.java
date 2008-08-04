/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.core.config;

import java.util.Dictionary;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.util.Iter;
import org.eclipse.riena.internal.core.Activator;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ConfigurationPlugin;
import org.osgi.service.log.LogService;

/**
 * This <code>ConfigurationPlugin</code> replaces variable references of the
 * form <i>${var}</i>.
 * <p>
 * Internally it uses the <code>StringVariableManager</code> to perform the
 * replacements. All configuration has to be done by the facilities of the
 * <code>StringVariableManager</code>.
 */
public class ConfigSymbolReplace implements ConfigurationPlugin {

	private IStringVariableManager variableManager = VariablesPlugin.getDefault().getStringVariableManager();

	private final static Logger LOGGER = Activator.getDefault().getLogger(ConfigSymbolReplace.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.service.cm.ConfigurationPlugin#modifyConfiguration(org.osgi.
	 * framework.ServiceReference, java.util.Dictionary)
	 */
	@SuppressWarnings("unchecked")
	public void modifyConfiguration(ServiceReference reference, Dictionary properties) {
		// don't do symbol source replace for configurations of myself
		if (reference != null && Activator.getDefault().getContext().getService(reference) == this)
			return;

		for (Object key : Iter.able(properties.keys())) {
			Object value = properties.get(key);
			if (value.getClass() != String.class)
				continue;
			try {
				properties.put(key, variableManager.performStringSubstitution((String) value));
			} catch (CoreException e) {
				// TODO maybe more specific exception type here
				throw new RuntimeException("Error while modifying (" + key + "," + value + ").", e);
			}
		}
	}

	/**
	 * Add a new string variable to the <code>StringVariableManager</code>.
	 * 
	 * @param key
	 * @param value
	 * @throws ConfigurationException
	 */
	public void addVariable(String key, String value) throws ConfigurationException {
		IValueVariable[] variables = new IValueVariable[] { variableManager.newValueVariable((String) key, null, true,
				(String) value) };
		try {
			variableManager.addVariables(variables);
		} catch (CoreException e) {
			IValueVariable existingValue = variableManager.getValueVariable((String) key);
			if (existingValue.getValue().equals(value))
				LOGGER.log(LogService.LOG_WARNING, "Already defined: (" + key + "," + value + ")");
			else
				throw new ConfigurationException((String) key, "Could not add a value variable " + variables[0]
						+ " to variable manager.", e);
		}
	}

}
