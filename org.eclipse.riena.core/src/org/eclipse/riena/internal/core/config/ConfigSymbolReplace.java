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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.riena.internal.core.Activator;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ConfigurationPlugin;
import org.osgi.service.cm.ManagedService;

/**
 * This <code>ConfigurationPlugin</code> gets notified for each and every
 * configuration that ConfigAdmin applies. ConfigSymbolReplace itself is
 * configurable by sending configuration to SERVICE_PID =
 * "org.eclipse.riena.config.symbols".<br>
 * <code>ConfigSymbolReplace</code> replaces variable references of the form
 * <i>${var}</i> and replaces them with the replacements found in it´s
 * dictionary. It also is possible to have variable references within the
 * dictionary itself.
 */
public class ConfigSymbolReplace implements ConfigurationPlugin, ManagedService {

	private HashMap<String, String> symbols = new HashMap<String, String>();

	private static final char VARIBALE_POSTFIX = '}';
	private static final String VARIABLE_PREFIX = "${"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.service.cm.ConfigurationPlugin#modifyConfiguration(org.osgi.framework.ServiceReference,
	 *      java.util.Dictionary)
	 */
	public void modifyConfiguration(ServiceReference reference, Dictionary properties) {
		// don't do symbol source replace for configurations of myself
		if (reference != null && Activator.getContext().getService(reference) == this) {
			return;
		}
		Enumeration<?> enumeration = properties.keys();
		while (enumeration.hasMoreElements()) {
			Object key = enumeration.nextElement();
			Object value = properties.get(key);
			if (value instanceof String && ((String) value).contains(VARIABLE_PREFIX)) {
				properties.put(key, replaceSymbol((String) value));
			}
		}
	}

	/**
	 * looks for symbols in a specific property value
	 * 
	 * @param value
	 * @return
	 */
	private String replaceSymbol(final String value) {
		StringBuilder sb = new StringBuilder(value);
		int i;
		while ((i = sb.indexOf(VARIABLE_PREFIX)) > -1) {
			int x = sb.substring(i).indexOf(VARIBALE_POSTFIX);
			if (x > 0) {
				String key = sb.substring(i + 2, i + x);
				sb.replace(i, i + x + 1, lookupSymbol(key));
			} else
				throw new RuntimeException("Incomplete symbol within " + sb + ". The closeing brace '}' is missing.");
		}
		return sb.toString();
	}

	/**
	 * looks up a symbol in its table
	 * 
	 * @param symbol
	 * @return
	 */
	private String lookupSymbol(String symbol) {
		String value = symbols.get(symbol);
		if (value != null)
			return value;
		throw new RuntimeException("symbol " + symbol + " not found.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.service.cm.ManagedService#updated(java.util.Dictionary)
	 */
	public void updated(Dictionary properties) throws ConfigurationException {
		if (properties == null) {
			return;
		}
		// TODO Is it really necessary to create a copy? The interface says that
		// there is already a copy passed in!
		HashMap<String, String> tempSymbols = new HashMap<String, String>(properties.size());
		Enumeration<String> enumeration = properties.keys();
		while (enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			String value = (String) properties.get(key);
			tempSymbols.put(key, value);
		}
		assertLoopFreeness(tempSymbols);
		this.symbols = tempSymbols;
	}

	/**
	 * @param tempSymbols
	 */
	private static void assertLoopFreeness(Map<String, String> dictionary) {
		for (String symbol : dictionary.keySet()) {
			Set<String> walked = new HashSet<String>();
			walkAlong(symbol, dictionary, walked);
		}
	}

	/**
	 * @param symbol
	 * @param dictionary
	 * @param walked
	 */
	private static void walkAlong(String symbol, Map<String, String> dictionary, Set<String> walked) {
		if (walked.contains(symbol))
			throw new RuntimeException("There is a loop " + walked + " within the dictionary " + dictionary
					+ "  referencing back to " + symbol + ".");
		walked.add(symbol);
		List<String> symbolsForSymbol = getSymbolsForSymbol(dictionary.get(symbol));
		for (String deeperSymbol : symbolsForSymbol) {
			walkAlong(deeperSymbol, dictionary, walked);
		}
	}

	/**
	 * @param string
	 * @return
	 */
	private static List<String> getSymbolsForSymbol(String string) {
		int i = string.indexOf(VARIABLE_PREFIX);
		if (i == -1)
			return Collections.emptyList();
		List<String> symbols = new ArrayList<String>();
		int x = 0;
		while (i > -1) {
			x = string.substring(i).indexOf(VARIBALE_POSTFIX);
			if (x > 0) {
				String key = string.substring(i + 2, i + x);
				symbols.add(key);
			} else
				throw new RuntimeException("Incomplete symbol within " + string
						+ " from the dictionary. The closeing brace '}' is missing.");
			i = string.indexOf(VARIABLE_PREFIX, i + x);
		}
		return symbols;
	}
}
